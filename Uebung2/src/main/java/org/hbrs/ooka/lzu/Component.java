package org.hbrs.ooka.lzu;

import org.hbrs.ooka.component.state.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Component implements Deployable {
    private static final Logger LOG = LoggerFactory.getLogger(Component.class);
    private final String id;
    private State state;
    private final String name;
    private final String url;

    public Component(String id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    @Override
    public void start() {
        //TODO_done iterate over all classes in jar and find start and stop method

        // TODO only method.invoke in here

        //TODO ensure own classloader if duplicate class from component
        Thread thread = Lzu.getInstance().getThread(id);
        if (thread == null) {
            LOG.warn("Unable to start component {}: {}; Component was not deployed in Lzu.", id, name);
            return;
        }
        thread.notify();
    }

    protected void invokeStart() {
        URL[] urls = null;
        try {
            urls = new URL[]{URI.create("file:///" + url).toURL()};
        } catch (MalformedURLException e) {
            LOG.error("Given url was not a valid URL", e);
            return;
        }
        try (JarFile jarFile = new JarFile(url); URLClassLoader cl = URLClassLoader.newInstance(urls)) {
            Enumeration<JarEntry> e = jarFile.entries();

            while (e.hasMoreElements()) {
                JarEntry je = e.nextElement();
                if (je.isDirectory() || !je.getName().endsWith(".class")) {
                    continue;
                }
                String className = je.getName().substring(0, je.getName().length() - 6); // -6 because of ".class"
                className = className.replace('/', '.');
                Class<?> startClass = cl.loadClass(className);
                Object object = startClass.getConstructor(null).newInstance(null);

//                System.out.println("className" + className);
                if (runStartMethod(startClass, object)) return;
            }
            System.err.println("Method 'start()' was not found in component.");
        } catch (IOException | ClassNotFoundException | InvocationTargetException | IllegalAccessException |
                 NoSuchMethodException | InstantiationException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static boolean runStartMethod(Class<?> c, Object o) throws IllegalAccessException, InvocationTargetException {
        for (Method declaredMethod : c.getDeclaredMethods()) {
            if (declaredMethod.getName().equals("start")) {
                System.out.println("Invoked " + c.getName() + "." + declaredMethod.getName() + "()");
                declaredMethod.invoke(o);
                return true;
            }
        }
        return false;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public State getStateInformation() {
        return state;
    }

    @Override
    public void stop() {
        state.stop(this);
    }

    @Override
    public String toString() {
        return super.getClass().getClassLoader() + "[id=" + id + ", name=" + name + "]";
    }

    public String getUrl() {
        return url;
    }
}

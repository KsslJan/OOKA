package org.hbrs.ooka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Path to jar not specified as argument");
        }
        String pathToJar = args[0];
        try (JarFile jarFile = new JarFile(pathToJar)) {
            Enumeration<JarEntry> e = jarFile.entries();
            URL[] urls = null;
            try {
                urls = new URL[]{new URL("jar:file:" + pathToJar + "!/")};
            } catch (MalformedURLException ex) {
                logger.error("Url was not set right.", ex);
            }
            URLClassLoader cl = URLClassLoader.newInstance(urls);

            while (e.hasMoreElements()) {
                JarEntry jarEntry = e.nextElement();
                if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
                    continue;
                }
                String className = jarEntry.getName().substring(0, jarEntry.getName().length() - 6); // -6 because of 6 chars for ".class"
                className = className.replace('/', '.');
                try {
                    Class c = cl.loadClass(className);
                } catch (ClassNotFoundException ex) {
                    logger.error("Class " + className + " not found", ex);
                }
            }
        } catch (IOException ex) {
            logger.error("Jar file not accessible.", ex);
        }
    }
}
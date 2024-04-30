package org.hbrs.ooka.classloading;

public class MyClassLoader extends ClassLoader implements ClassLoading {
    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return null;
    }
}

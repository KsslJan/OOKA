package org.hbrs.ooka.classloading;

public interface ClassLoading {
    ClassLoader getParent();

    Class<?> findClass(String name) throws ClassNotFoundException;

    Class<?> loadClass(String name) throws ClassNotFoundException;
}

package com.github.ljl.jerrymouse.support.classloader;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-06 22:59
 **/

public class LocalClassLoader extends ClassLoader {
    @Override
    public Class loadClass(String className) {
        try {
            // 默认直接加载本地的 class
            return Class.forName(className);
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            return null;
        }
    }
}

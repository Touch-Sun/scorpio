package com.touchsun.scorpio;

import org.junit.Test;

public class TestDemo {

    @Test
    public void demoClassLoader() {
        System.out.println(" Object的类加载器： " + Object.class.getClassLoader());
        System.out.println(" TestDemo的类加载器： " + TestDemo.class.getClassLoader());
    }
}

package com.touchsun.scorpio;

import lombok.NoArgsConstructor;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author lee
 */
public class DiyUrlClassLoader extends URLClassLoader {

    public DiyUrlClassLoader(URL[] urls) {
        super(urls);
    }

    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        URL url = new URL("file:c:/Users/lee/Desktop/test.jar");
        // 实例化一个URL数组
        URL[] urls = new URL[] { url };
        // 实例化自定义URL类加载器
        DiyUrlClassLoader loader = new DiyUrlClassLoader(urls);
        // 在这个jar包中去加载这个类
        Class<?> clazz = loader.loadClass("cn.how2j.diytomcat.test.HOW2J");
        // 执行方法
        Object obj = clazz.newInstance();
        Method method = clazz.getMethod("hello");
        method.invoke(obj);

        System.out.println("URL类加载器执行了类加载方法" + clazz.getClassLoader());
    }
}

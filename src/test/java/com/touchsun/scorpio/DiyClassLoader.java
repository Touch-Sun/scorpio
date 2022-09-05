package com.touchsun.scorpio;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 自定义ClassLoader
 *
 * @author lee
 */
public class DiyClassLoader extends ClassLoader {

    /**
     * class文件所在文件夹
     */
    private File clazzFolder = new File(System.getProperty("user.dir") + "/src/test", "java");

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 读取类文件的字节数组
        byte[] classData = loadClassData(name);
        // 返回CLass的对象定义,通过字节数组
        return defineClass(name, classData, 0, classData.length);
    }

    /**
     * 读取类文件的字节数据
     */
    private byte[] loadClassData(String name) throws ClassNotFoundException {
        // 将全限定包名的"."替换成“/”,定位到真实文件
        String fileName = StrUtil.replace(name, ".", "/") + ".class";
        // 实例化文件对象
        File clazzFile = new File(clazzFolder, fileName);
        System.out.println(clazzFile.getAbsolutePath());
        // 文件是否存在
        if (clazzFile.exists()) {
            // 返回文件的字节数组
            return FileUtil.readBytes(clazzFile);
        } else {
            // 不存在抛出ClassNorFound异常
            throw new ClassNotFoundException(name);
        }
    }

    /**
     * 测试类加载器
     */
    @Test
    public void testClassLoader() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        DiyClassLoader diyClassLoader = new DiyClassLoader();

        // 得到预期类
        Class<?> testClazz = diyClassLoader.loadClass("com.touchsun.scorpio.Hello");

        // 实例化对象
        Object obj = testClazz.newInstance();
        // 得到方法
        Method helloMethod = testClazz.getMethod("sayHello");
        // 执行方法
        helloMethod.invoke(obj);

        System.out.println("方法由" + testClazz.getClassLoader() + "执行");
    }
}

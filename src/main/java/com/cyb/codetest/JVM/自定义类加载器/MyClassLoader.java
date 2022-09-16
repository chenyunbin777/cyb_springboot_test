package com.cyb.codetest.JVM.自定义类加载器;

import com.alibaba.fastjson.JSON;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author cyb
 * @date 2021/1/8 4:06 下午
 */
public class MyClassLoader extends ClassLoader {

    //指定路径
    private String path;


    public MyClassLoader(String classPath) {
        path = classPath;
    }

    /**
     * 重写findClass方法
     *
     * @param name 是我们这个类的全路径
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        System.out.println("name：" + name);
        Class myClassLoader = null;
        // 获取该class文件字节码数组
        byte[] classData = getData();
        System.out.println("classData：" + JSON.toJSONString(classData));
        if (classData != null) {
            // 将class的字节码数组转换成Class类的实例
            myClassLoader = this.defineClass(name, classData, 0, classData.length);
        }
        return myClassLoader;
    }

    /**
     * 将class文件转化为字节码数组
     *
     * @return
     */
    private byte[] getData() {

        File file = new File(path);
        if (file.exists()) {
            FileInputStream in = null;
            ByteArrayOutputStream out = null;
            try {
                in = new FileInputStream(file);
                out = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = in.read(buffer)) != -1) {
                    out.write(buffer, 0, size);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
            return out.toByteArray();
        } else {
            return null;
        }

    }

    /**
     * 1 这时候其父类AppClassLoader是能够加载到classpath下的ClassLoaderClassFile.class的,并没有打破双亲委派模型
     * 2 如果想要打破双亲委派模型，需要重新loadClass()方法。
     *
     * @param args
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        //这个类class的路径
        String classPath = "/Users/chenyunbin/Documents/我的idea项目/cyb_springboot_test/src/main/java异常.md/com/cyb/codetest/JVM/自定义类加载器/ClassLoaderClassFile.class";

        MyClassLoader myClassLoader = new MyClassLoader(classPath);
        //类的全称
        String packageNamePath = "com.cyb.codetest.JVM.自定义类加载器.ClassLoaderClassFile";

        //加载Log这个class文件
        Class<?> ClassLoaderClassFile = myClassLoader.loadClass(packageNamePath);

        System.out.println("类加载器是:" + ClassLoaderClassFile.getClassLoader());

        //利用反射获取main方法
        Method method = ClassLoaderClassFile.getDeclaredMethod("testClassLoaderMethod");
        Object object = ClassLoaderClassFile.newInstance();
        method.invoke(object);
    }


}

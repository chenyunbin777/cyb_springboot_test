package com.cyb.codetest.JVM;

import java.util.List;

/**
 * 如何理解打印出来的三种类加载器?
 * BootStrap->ExtClassLoader->AppClassLoader->开发者自定义类加载器. 可认为BootStrap为祖先加载器，开发者自定义类加载器为底层加载器。 不过多数情况，我们并不会自定义类加载器，所以大多数情况，AppClassLoader就是JVM中的底层类加载器了。
 *
 * 注意BootStrap是用c++代码编写的，后面2个类加载器则是java类编写 这就解释了为什么BootStrap加载器会返回null了，
 * 因为这个祖先类加载器在 java里根本找不到吗
 *
 *
 * 类加载的委托机制原则
 * 由下到上加载，顶层加载不了再交给下层加载，如果回到底层位置加载 还加载不到，那就会报ClassNotFound错误了。
 * 如同一开始我们的例子一样，JVMClassLoader 这个类 为什么输出的类加载器名称是AppClassLoader呢，原因就是先找到顶层的Boot类加载器发现找不到这个类，然后继续找ext类加载器还是找不到，最后在AppClassLoder中找到这个类。所以这个类的加载器就是AppClassLoader了。
 * 为什么System和List类的类加载器是Boot类加载器？因为Boot类加载器加载的默认路径就是/jre/lib 这个目录下的rt.jar 。ext加载器的默认路径是 /jre/lib/ext/*.jar.这2个目录下面当然无法找到我们的JVMClassLoader类了 注意这里的根目录是你jdk的安装目录
 * @author cyb
 * @date 2021/1/3 10:04 上午
 */
public class JVMClassLoader {

    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println("JVMClassLoader类的加载器的名称:"+JVMClassLoader.class.getClassLoader().getClass().getName());
        System.out.println("System类的加载器的名称:"+System.class.getClassLoader());
        System.out.println("List类的加载器的名称:"+ List.class.getClassLoader());

        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println("systemClassLoader:"+systemClassLoader);

        //可以加载自定义的类加载器，如果没有则是默认使用systemClassLoader来加载类。
        systemClassLoader.loadClass("");
        ClassLoader cl = JVMClassLoader.class.getClassLoader();
        while(cl != null){
            System.out.print(cl.getClass().getName()+"->");
            cl = cl.getParent();
        }
        System.out.println(cl);
    }

}

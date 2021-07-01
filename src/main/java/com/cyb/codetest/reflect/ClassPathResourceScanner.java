package com.cyb.codetest.reflect;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * 扫描包下所有的类
 *
 * @author cyb
 * @date 2021/1/3 2:01 下午
 */
public class ClassPathResourceScanner {
    public static final String CLASS_SUFFIX = ".class";

    private static final Pattern INNER_PATTERN = java.util.regex.Pattern.compile("\\$(\\d+).", java.util.regex.Pattern.CASE_INSENSITIVE);

    public static Set<String> findCandidateClasses(String packageName) throws IOException {
        if (packageName.endsWith(".")) {
            packageName = packageName.substring(0, packageName.length() - 1);
        }
        Map<String, String> classMap = new HashMap<>(32);
        String path = packageName.replace(".", "/");
        Enumeration<URL> urls = findAllClassPathResources(path);
        while (urls != null && urls.hasMoreElements()) {
            URL url = urls.nextElement();
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                String file = URLDecoder.decode(url.getFile(), "UTF-8");
                parseClassFile(new File(file), packageName, classMap);
            } else if ("jar".equals(protocol)) {
                parseJarFile(url, classMap);
            }
        }
        return new HashSet<>(classMap.values());
    }

    public static void parseClassFile(File classfile, String packageName, Map<String, String> classMap) {
        if (!classfile.exists()) {
            return;
        }
        if (classfile.isDirectory()) {
            File[] files = classfile.listFiles();
            for (File file : files) {
                parseClassFile(file, packageName + "." + classfile.getName(), classMap);
            }
        } else if (classfile.getName().endsWith(CLASS_SUFFIX)) {
            String name = packageName + "." + classfile.getName();
            System.out.println("file:" + classfile + "\t class:" + name);
            addToClassMap(name, classMap);
        }
    }

    public static void parseJarFile(URL url, Map<String, String> classMap) throws IOException {
        JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.isDirectory()) {
                continue;
            }
            String name = entry.getName();
            if (name.endsWith(CLASS_SUFFIX)) {
                addToClassMap(name.replace("/", "."), classMap);
            }
        }
    }

    public static boolean addToClassMap(String name, Map<String, String> classMap) {

        if (INNER_PATTERN.matcher(name).find()) { //过滤掉匿名内部类
            System.out.println("anonymous inner class:" + name);
            return false;
        }
        System.out.println("class:" + name);
        if (name.indexOf("$") > 0) { //内部类
            System.out.println("inner class:" + name);
        }
        if (!classMap.containsKey(name)) {
            classMap.put(name, name.substring(0, name.length() - 6)); //去掉.class
        }
        return true;
    }

    public static Enumeration<URL> findAllClassPathResources(String path) throws IOException {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        Enumeration<URL> urls = getClassLoader().getResources(path);

        return urls;
    }

    public static ClassLoader getClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back to system class loader...
        }
        Class<?> cls = ClassPathResourceScanner.class;
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = cls.getClassLoader();
        }
        return cl;
    }


    public static void main(String[] args) throws IOException {
        ClassPathResourceScanner scanner = new ClassPathResourceScanner();
        //要扫描的包名
        String packageName = "com.cyb.codetest.reflect";
        //获取该包下所有的类名称
        Set<String> set = scanner.findCandidateClasses(packageName);
        System.out.println(set);
    }
}

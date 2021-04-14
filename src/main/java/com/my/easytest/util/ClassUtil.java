package com.my.easytest.util;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassUtil {
    public static final String ARRAY_SUFFIX = "[]";
    public static final String ARRAY_CLASS_NAME_REGEX = "\\[L(.*)\\;";
    private static final Map<String, Class<?>> COMMON_TYPES = new HashMap();

    static {
        COMMON_TYPES.put("boolean", Boolean.TYPE);
        COMMON_TYPES.put("byte", Byte.TYPE);
        COMMON_TYPES.put("char", Character.TYPE);
        COMMON_TYPES.put("double", Double.TYPE);
        COMMON_TYPES.put("float", Float.TYPE);
        COMMON_TYPES.put("int", Integer.TYPE);
        COMMON_TYPES.put("long", Long.TYPE);
        COMMON_TYPES.put("short", Short.TYPE);
        COMMON_TYPES.put("[Z", boolean[].class);
        COMMON_TYPES.put("[B", byte[].class);
        COMMON_TYPES.put("[C", char[].class);
        COMMON_TYPES.put("[D", double[].class);
        COMMON_TYPES.put("[F", float[].class);
        COMMON_TYPES.put("[I", int[].class);
        COMMON_TYPES.put("[J", long[].class);
        COMMON_TYPES.put("[S", short[].class);
        COMMON_TYPES.put("Boolean", Boolean.class);
        COMMON_TYPES.put("BigDecimal", BigDecimal.class);
        COMMON_TYPES.put("Number", Number.class);
        COMMON_TYPES.put("Short", Short.class);
        COMMON_TYPES.put("Integer", Integer.class);
        COMMON_TYPES.put("Long", Long.class);
        COMMON_TYPES.put("Float", Float.class);
        COMMON_TYPES.put("Double", Double.class);
        COMMON_TYPES.put("Byte", Byte.class);
        COMMON_TYPES.put("Character", Character.class);
        COMMON_TYPES.put("String", String.class);
        COMMON_TYPES.put("List", List.class);
        COMMON_TYPES.put("ArrayList", ArrayList.class);
        COMMON_TYPES.put("LinkedList", LinkedList.class);
        COMMON_TYPES.put("Map", Map.class);
        COMMON_TYPES.put("HashMap", HashMap.class);
        COMMON_TYPES.put("Hashtable", Hashtable.class);
        COMMON_TYPES.put("ConcurrentMap", ConcurrentMap.class);
        COMMON_TYPES.put("ConcurrentHashMap", ConcurrentHashMap.class);
        COMMON_TYPES.put("Set", Set.class);
        COMMON_TYPES.put("HashSet", HashSet.class);
        COMMON_TYPES.put("Date", Date.class);
        COMMON_TYPES.put("Object", Object.class);
        COMMON_TYPES.put("void", Void.TYPE);
        COMMON_TYPES.put("Void", Void.class);
    }

    public ClassUtil() {
    }

    private static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;

        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable var2) {
        }

        if (cl == null) {
            cl = ClassUtil.class.getClassLoader();
        }

        return cl;
    }

    public static Class<?> forName(String name) throws ClassNotFoundException {
        return forName(name, (ClassLoader)null);
    }

    public static Class<?> forName(String name, ClassLoader classLoader) throws ClassNotFoundException {
        if (name.endsWith("[]")) {
            String componentClassName = name.substring(0, name.length() - "[]".length());
            Class<?> componentClass = forName(componentClassName);
            return Array.newInstance(componentClass, 0).getClass();
        } else {
            Pattern pattern = Pattern.compile("\\[L(.*)\\;");
            Matcher matcher = pattern.matcher(name);
            if (matcher.find()) {
                String componentClassName = matcher.group(1);
                Class<?> componentClass = forName(componentClassName);
                return Array.newInstance(componentClass, 0).getClass();
            } else {
                Class<?> clazz = (Class)COMMON_TYPES.get(name);
                if (clazz != null) {
                    return clazz;
                } else {
                    return classLoader == null ? getDefaultClassLoader().loadClass(name) : classLoader.loadClass(name);
                }
            }
        }
    }

    public static String findClassNameFromJarFiles(List<JarFile> poJarFiles, String pClsName) {
        String res = "";
        Iterator var3 = poJarFiles.iterator();

        while(true) {
            while(var3.hasNext()) {
                JarFile jar = (JarFile)var3.next();
                Enumeration enumeration = jar.entries();

                while(enumeration.hasMoreElements()) {
                    JarEntry jarEntry = (JarEntry)enumeration.nextElement();
                    String jarEntryName = jarEntry.getName();
                    if (jarEntryName.endsWith(".class") && !jarEntryName.contains("$")) {
                        String clsName = jarEntryName.substring(0, jarEntryName.length() - 6).replace("/", ".");
                        if (clsName.endsWith(pClsName)) {
                            res = clsName;
                            break;
                        }
                    }
                }
            }

            return res;
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println(forName("int").getName());
        System.out.println(forName("int[]").getName());
        System.out.println(forName("[I").getName());
        System.out.println(forName("String").getName());
        System.out.println(forName("java.lang.String").getName());
        System.out.println(forName("String[]").getName());
        System.out.println(forName("java.lang.String[]").getName());
        System.out.println(forName("[Ljava.lang.String;").getName());
    }
}

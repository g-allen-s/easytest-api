package com.my.easytest.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarUtils {
    public JarUtils() {
    }

    public static List<String> findAllClassList(String psJarPath) {
        ArrayList res = new ArrayList();

        try {
            JarFile jarfile = new JarFile(psJarPath);
            Enumeration enumeration = jarfile.entries();

            while(enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry)enumeration.nextElement();
                String jarEntryName = jarEntry.getName();
                if (jarEntryName.endsWith(".class") && !jarEntryName.contains("$")) {
                    String str_classname = jarEntryName.substring(0, jarEntryName.length() - 6).replace("/", ".");
                    res.add(str_classname);
                }
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return res;
    }

    public static List<URL> getListJarURLs(File dirs) throws MalformedURLException {
        ArrayList<URL> oList = new ArrayList();
        if (dirs.isDirectory()) {
            File[] oFiles = dirs.listFiles();
            File[] var3 = oFiles;
            int var4 = oFiles.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                File oFile = var3[var5];
                oList.addAll(getListJarURLs(oFile));
            }
        } else if (dirs.getAbsolutePath().endsWith(".jar") || dirs.getAbsolutePath().endsWith(".zip")) {
            oList.add(dirs.toURI().toURL());
        }

        return oList;
    }

    public static String getClassNameByJavaFile(File srcFile, File javaFile) {
        if (srcFile != null && srcFile.exists() && javaFile != null && javaFile.exists()) {
            String str = StrUtil.substringAfter(javaFile.getAbsolutePath(), srcFile.getAbsolutePath());
            str = StrUtil.replace(str, File.separator, ".").replaceFirst(".", "").replace(".java", "");
            return str;
        } else {
            return "";
        }
    }
}

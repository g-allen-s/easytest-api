package com.my.easytest.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FileHandler {
    public FileHandler() {
    }

    public static boolean createDir(File dir) throws IOException {
        if ((dir.exists() || dir.mkdirs()) && dir.canWrite()) {
            return true;
        } else if (dir.exists()) {
            makeWritable(dir);
            return dir.canWrite();
        } else {
            return createDir(dir.getParentFile());
        }
    }

    public static boolean makeWritable(File file) throws IOException {
        return file.canWrite() ? true : file.setWritable(true);
    }

    public static boolean makeExecutable(File file) throws IOException {
        return canExecute(file) ? true : file.setExecutable(true);
    }

    public static Boolean canExecute(File file) {
        return file.canExecute();
    }

    public static boolean isZipped(String fileName) {
        return fileName.endsWith(".zip") || fileName.endsWith(".xpi");
    }

    public static String getFileName(String filePath) {
        String result = "";
        File file = new File(filePath);
        if (file.exists()) {
            result = file.getName();
        }

        return result;
    }

    public static boolean delete(File toDelete) {
        if (toDelete == null) {
            return false;
        } else {
            boolean deleted = true;
            if (toDelete.isDirectory()) {
                File[] var2 = toDelete.listFiles();
                int var3 = var2.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    File child = var2[var4];
                    deleted &= child.canWrite() && delete(child);
                }
            }

            return deleted && toDelete.canWrite() && toDelete.delete();
        }
    }

    public static boolean deleteWithoutJudgeWritePermission(File dir) {
        if (dir.isDirectory()) {
            File[] var1 = dir.listFiles();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                File child = var1[var3];
                boolean success = deleteWithoutJudgeWritePermission(child);
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    public static void copy(File from, File to) throws IOException {
        copy(from, to, (FileHandler.Filter)(new FileHandler.NoFilter()));
    }

    public static void copy(File source, File dest, String suffix) throws IOException {
        copy(source, dest, (FileHandler.Filter)(suffix == null ? new FileHandler.NoFilter() : new FileHandler.FileSuffixFilter(suffix)));
    }

    private static void copy(File source, File dest, FileHandler.Filter onlyCopy) throws IOException {
        if (source.exists()) {
            if (source.isDirectory()) {
                copyDir(source, dest, onlyCopy);
            } else {
                copyFile(source, dest, onlyCopy);
            }

        }
    }

    public static File getChildFile(File parent, String childNameRegex) {
        File[] files = parent.listFiles();
        File[] var3 = files;
        int var4 = files.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            File iFile = var3[var5];
            if (Pattern.compile(childNameRegex).matcher(iFile.getName()).matches()) {
                return iFile;
            }
        }

        return null;
    }

    private static void copyDir(File from, File to, FileHandler.Filter onlyCopy) throws IOException {
        if (onlyCopy.isRequired(from)) {
            createDir(to);
            String[] children = from.list();
            if (children == null) {
                throw new IOException("Could not copy directory " + from.getPath());
            } else {
                String[] var4 = children;
                int var5 = children.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    String child = var4[var6];
                    if (!".parentlock".equals(child) && !"parent.lock".equals(child)) {
                        copy(new File(from, child), new File(to, child), onlyCopy);
                    }
                }

            }
        }
    }

    private static void copyFile(File from, File to, FileHandler.Filter onlyCopy) throws IOException {
        if (onlyCopy.isRequired(from)) {
            FileChannel out = null;
            FileChannel in = null;

            try {
                in = (new FileInputStream(from)).getChannel();
                out = (new FileOutputStream(to)).getChannel();
                long length = in.size();
                long copied = in.transferTo(0L, in.size(), out);
                if (copied != length) {
                    throw new IOException("Could not transfer all bytes.");
                }
            } finally {
                out.close();
                in.close();
            }

        }
    }

    public static String readAsString(File toRead) throws IOException {
        BufferedReader reader = null;

        try {
            String data = "";
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(toRead)));
            StringBuilder builder = new StringBuilder();

            while((data = reader.readLine()) != null) {
                builder.append(data);
                builder.append("\n");
            }

            String var4 = builder.toString();
            return var4;
        } finally {
            reader.close();
        }
    }

    public static String readAsString(File toRead, String charset) throws IOException {
        BufferedReader reader = null;

        try {
            String data = "";
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(toRead), charset));
            StringBuilder builder = new StringBuilder();

            while((data = reader.readLine()) != null) {
                builder.append(data);
                builder.append("\n");
            }

            String var5 = builder.toString();
            return var5;
        } finally {
            reader.close();
        }
    }

    public static String readAsStringNew(File poFile) {
        String sContent = null;
        InputStream in = null;
        byte[] strBuffer = null;
        boolean var4 = false;

        try {
            in = new FileInputStream(poFile);
            int flen = (int)poFile.length();
            strBuffer = new byte[flen];
            in.read(strBuffer, 0, flen);
        } catch (IOException var14) {
            var14.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException var13) {
                    var13.printStackTrace();
                }
            }

        }

        sContent = new String(strBuffer);
        strBuffer = null;
        return sContent;
    }

    public static List<URL> getFileURLs(File poFile) throws MalformedURLException {
        List<URL> oList = new ArrayList();
        if (poFile.isDirectory()) {
            File[] oFiles = poFile.listFiles();
            File[] var3 = oFiles;
            int var4 = oFiles.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                File oFile = var3[var5];
                oList.addAll(getFileURLs(oFile));
            }
        } else if (poFile.getAbsolutePath().endsWith(".jar") || poFile.getAbsolutePath().endsWith(".zip")) {
            oList.add(poFile.toURI().toURL());
        }

        return oList;
    }

    public static List<URL> getJarFileURLs(File poFile) throws MalformedURLException {
        List<URL> oList = new ArrayList();
        if (poFile.isDirectory()) {
            File[] oFiles = poFile.listFiles();
            File[] var3 = oFiles;
            int var4 = oFiles.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                File oFile = var3[var5];
                oList.addAll(getFileURLs(oFile));
            }
        } else if (poFile.getAbsolutePath().endsWith(".jar")) {
            oList.add(poFile.toURI().toURL());
        }

        return oList;
    }

    private static class NoFilter implements FileHandler.Filter {
        private NoFilter() {
        }

        public boolean isRequired(File file) {
            return true;
        }
    }

    private static class FileSuffixFilter implements FileHandler.Filter {
        private final String suffix;

        public FileSuffixFilter(String suffix) {
            this.suffix = suffix;
        }

        public boolean isRequired(File file) {
            return file.isDirectory() || file.getAbsolutePath().endsWith(this.suffix);
        }
    }

    public interface Filter {
        boolean isRequired(File var1);
    }
}

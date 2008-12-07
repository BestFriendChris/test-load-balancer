package com.googlecode.tlb.utils;

import org.apache.commons.io.FileUtils;
import static org.apache.commons.io.FileUtils.writeStringToFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class FileUtil {

    public static File createTempFolder() {
        final File file = new File(tempFolder(), UUID.randomUUID().toString());
        file.mkdirs();
        file.deleteOnExit();
        return file;
    }

    private static String tempFolder() {
        return System.getProperty("java.io.tmpdir");
    }

    public static File createFileInFolder(File folder, String fileName) throws IOException {
        File file = new File(folder, fileName);
        file.createNewFile();
        file.deleteOnExit();
        return file;
    }

    public static void deleteFolder(File folder) throws IOException {
        FileUtils.deleteDirectory(folder);
    }

    public static void copySingleFile(File src, File dest) throws IOException {
        FileUtils.copyFile(src, dest);
    }

    public static void copyFile(File src, File dest) throws IOException {
        if (src.isFile()) {
            copySingleFile(src, dest);
        }
        if (src.isDirectory()) {
            File[] files = src.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    continue;
                }
                copySingleFile(file, new File(dest, file.getName()));
            }
        }
    }

    public static void copyFileWithReplacement(File src, File dest, HashMap<String, String> hashMap) throws IOException {
        String content = org.apache.commons.io.FileUtils.readFileToString(src);
        for (String key : hashMap.keySet()) {
            content = content.replace(key, hashMap.get(key));
        }
        writeStringToFile(dest, content);
    }
}

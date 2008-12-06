package com.googlecode.tlb.utils;

import org.apache.commons.io.FileUtils;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.io.FileUtils.writeStringToFile;

import java.io.File;
import java.io.IOException;
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

    public static void copyFile(File src, File dest) throws IOException {
        org.apache.commons.io.FileUtils.copyFile(src, dest);
    }

    public static void replace(File configFile, String oldString, String newString) throws IOException {
        String content = readFileToString(configFile);
        writeStringToFile(configFile, content.replace(oldString, newString));
    }
}

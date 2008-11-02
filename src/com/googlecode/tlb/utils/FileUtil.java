package com.googlecode.tlb.utils;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    public static File createTempFolder(String folderName) {
        final File file = new File(tempFolder(), folderName);
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
}

package rw.rra.management.vehicles.Files;


import rw.rra.management.vehicles.Files.FileSizeType;

import java.util.UUID;

public class FileUtil {

    public static FileSizeType getFileSizeTypeFromFileSize(long size) {
        if (size >= (1024L * 1024 * 1024 * 1024))
            return FileSizeType.TB;
        else if (size >= 1024 * 1024 * 1024)
            return FileSizeType.GB;
        else if (size >= 1024 * 1024)
            return FileSizeType.MB;
        else if (size >= 1024)
            return FileSizeType.KB;
        else
            return FileSizeType.B;
    }

    public static int getFormattedFileSizeFromFileSize(double size, FileSizeType type ) {
        if (type == FileSizeType.TB)
            return (int) (size / (1024L * 1024 * 1024 * 1024));
        else if (type == FileSizeType.GB)
            return (int) (size / (1024 * 1024 * 1024));
        else if (type == FileSizeType.MB)
            return (int) (size / (1024 * 1024));
        else if (type == FileSizeType.KB)
            return (int) (size / (1024));
        else
            return (int) size;
    }

    public static String generateUUID(String fileName) {
        int period = fileName.indexOf(".");
        String prefix = fileName.substring(0, period);
        String suffix = fileName.substring(period);

        return prefix + "-" +  UUID.randomUUID().toString().replace("-", "")  + suffix;
    }



}


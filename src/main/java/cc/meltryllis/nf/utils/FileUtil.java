package cc.meltryllis.nf.utils;

import java.io.File;

/**
 * 文件工具。
 *
 * @author Zachary W
 * @date 2025/2/25
 */
public class FileUtil {

    private static final char SEPARATOR = '.';

    /**
     * 获取主文件名。
     *
     * @param file 文件对象
     *
     * @return 文件夹则返回文件名；文件则返回著文件名；文件对象为空时返回 {@code null}。
     */
    public static String getPrefix(File file) {
        if (file == null) {
            return null;
        }
        if (file.isDirectory()) {
            return file.getName();
        }
        String name = file.getName();
        int lastIndex = name.lastIndexOf(SEPARATOR);
        return name.substring(0, lastIndex);
    }

    /**
     * 获取文件扩展名。
     *
     * @param file 文件对象。
     *
     * @return 如果文件对象为空或者为文件夹，则返回 {@code null}；否则返回文件扩展名（扩展名不包含“.”）。
     */
    public static String getSuffix(File file) {
        if (file == null || file.isDirectory()) {
            return null;
        }
        String name = file.getName();
        int lastIndex = name.lastIndexOf(SEPARATOR);
        if (lastIndex + 1 >= name.length()) {
            return null;
        }
        return name.substring(lastIndex + 1);
    }

    /**
     * 删除文件以及其内部所有文件。
     *
     * @param file 需要删除的文件。
     *
     * @return 全部删除成功则返回 {@code true}, 否则停止删除操作且返回 {@code false}。
     */
    public static boolean delete(File file) {
        if (file == null || !file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (File listFile : listFiles) {
                    if (!delete(listFile)) {
                        return false;
                    }
                }
            }
        }
        return file.delete();
    }
}

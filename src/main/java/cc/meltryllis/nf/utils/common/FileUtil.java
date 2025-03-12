package cc.meltryllis.nf.utils.common;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * 文件工具。
 *
 * @author Zachary W
 * @date 2025/2/25
 */
@Slf4j
public class FileUtil {

    private static final char SEPARATOR = '.';

    /**
     * 是否为文件。
     *
     * @param file 文件对象。
     *
     * @return 是文件对象则返回 {@code true}，否则返回 {@code false}。
     */
    public static boolean isFile(File file) {
        return file != null && file.isFile();
    }

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
        if (!isFile(file)) {
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

    /**
     * 判断文件是否存在。
     *
     * @param file 文件对象。
     *
     * @return 存在则返回 {@code true}，否则返回 {@code false}。
     */
    public static boolean exists(File file) {
        return file != null && file.exists();
    }

    /**
     * 打开文件。
     *
     * @param file 文件对象。
     */
    public static void open(File file) {
        if (exists(file)) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                log.info("Open file failed.", e);
            }
        }
    }

    /**
     * 打开文件所在目录。
     *
     * @param file 文件对象。
     */
    public static void openParentDirectory(File file) {
        if (exists(file) && exists(file.getParentFile()) && file.getParentFile().isDirectory()) {
            open(file.getParentFile());
        }
    }
}

package cc.meltryllis.nf.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * 快捷使用Jackson库。
 *
 * @author Zachary W
 * @date 2025/2/6
 */
public class JSONUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> T parseObject(File file, Class<T> clazz) {
        try {
            return MAPPER.readValue(file, clazz);
        } catch (IOException e) {
            return null;
        }
    }

    public static String toString(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void storeFile(File target, Object obj) {
        try {
            // 创建父目录
            Files.createDirectories(target.getParentFile().toPath());
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(target, obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

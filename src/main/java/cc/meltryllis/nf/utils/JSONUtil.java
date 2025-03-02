package cc.meltryllis.nf.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * 快捷使用Jackson库。
 *
 * @author Zachary W
 * @date 2025/2/6
 */
@Slf4j
public class JSONUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> T parseObject(File file, Class<T> clazz) {
        try {
            return MAPPER.readValue(file, clazz);
        } catch (IOException e) {
            log.debug("Parse JSON file failed, return null.", e);
            return null;
        }
    }

    public static void storeFile(File target, Object obj) {
        try {
            // 创建父目录
            Files.createDirectories(target.getParentFile().toPath());
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(target, obj);
        } catch (IOException e) {
            log.warn("Store JSON file failed.", e);
        }
    }

}

package cc.meltryllis.nf.utils.jackson;

import cc.meltryllis.nf.utils.common.FileUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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

    static {
        MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        SimpleModule propertyModule = new SimpleModule();
        propertyModule.addSerializer(SimpleStringProperty.class, new SimpleStringPropertySerializer());
        propertyModule.addDeserializer(SimpleStringProperty.class, new SimpleStringPropertyDeserializer());
        propertyModule.addSerializer(SimpleIntegerProperty.class, new SimpleIntegerPropertySerializer());
        propertyModule.addDeserializer(SimpleIntegerProperty.class, new SimpleIntegerPropertyDeserializer());
        propertyModule.addSerializer(SimpleBooleanProperty.class, new SimpleBooleanPropertySerializer());
        propertyModule.addDeserializer(SimpleBooleanProperty.class, new SimpleBooleanPropertyDeserializer());
        MAPPER.registerModule(propertyModule);
    }

    public static <T> T parseObject(File file, Class<T> clazz) {
        if (!FileUtil.exists(file)) {
            return null;
        }
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

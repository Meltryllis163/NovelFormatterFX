package cc.meltryllis.nf.constants;

import cc.meltryllis.nf.ui.MainApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 数据存储。
 *
 * @author Zachary W
 * @date 2025/2/4
 */
public class DataCons {

    public static final String DATA_FOLDER          = "data";
    public static final String INPUT_FORMAT_CONFIG  = DATA_FOLDER + "/input_format.json";
    public static final String OUTPUT_FORMAT_CONFIG = DATA_FOLDER + "/output_format.json";
    public static final String BUILD_VERSION        = "/pom/build.version";

    public static final String VERSION;
    public static String GITHUB = "https://github.com/Meltryllis163/NovelFormatterFX";

    // 此处参考 https://developer.aliyun.com/article/1625108
    static {
        String version = "Unknown";
        try (InputStream inputStream = MainApplication.class.getResourceAsStream(BUILD_VERSION)) {
            if (inputStream != null) {
                Properties properties = new Properties();
                properties.load(inputStream);
                version = properties.getProperty("version", "Unknown");
            }
        } catch (IOException ignored) {

        } finally {
            VERSION = version;
        }
    }

}

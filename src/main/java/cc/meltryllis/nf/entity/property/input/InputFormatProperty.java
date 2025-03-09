package cc.meltryllis.nf.entity.property.input;

import cc.meltryllis.nf.constants.DataCons;
import cc.meltryllis.nf.constants.FileCons;
import cc.meltryllis.nf.entity.FormatFactory;
import cc.meltryllis.nf.utils.common.FileUtil;
import cc.meltryllis.nf.utils.jackson.JSONUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;

/**
 * 「小说导入」配置项的响应式属性。
 *
 * @author Zachary W
 * @date 2025/2/20
 */
@Getter
@ToString
public class InputFormatProperty {

    @JsonIgnore
    private static InputFormatProperty instance;

    @JsonIgnore
    private SimpleObjectProperty<File> fileProperty;
    @Setter
    @JsonProperty("chapterLength")
    private ChapterLengthProperty      chapterLengthProperty;
    @Setter
    @JsonProperty("chapterRegex")
    private ChapterRegexProperty       chapterRegexProperty;


    public InputFormatProperty() {
        if (instance == null) {
            initProperties();
        } else {
            throw new RuntimeException();
        }
    }

    public static InputFormatProperty getInstance() {
        if (instance == null) {
            InputFormatProperty property = JSONUtil.parseObject(new File(DataCons.INPUT_FORMAT_CONFIG),
                    InputFormatProperty.class);
            instance = property == null ? FormatFactory.createDefaultInputFormatProperty() : property;
        }
        return instance;
    }

    public static void store() {
        JSONUtil.storeFile(new File(DataCons.INPUT_FORMAT_CONFIG), getInstance());
    }

    private void initProperties() {
        fileProperty = new SimpleObjectProperty<>();
    }

    @JsonIgnore
    public File getFile() {
        return getFileProperty().getValue();
    }

    /**
     * 设置格式化文件，仅可以设置TXT扩展名的文件，否则会失败。
     *
     * @param file 文件对象。
     *
     * @return 设置成功则返回 {@code true}，否则返回 {@code false}。
     */
    public boolean setFile(File file) {
        if (FileUtil.isFile(file) && FileCons.TXT_SUFFIX.equalsIgnoreCase(FileUtil.getSuffix(file))) {
            getFileProperty().setValue(file);
            return true;
        }
        return false;
    }
}

package cc.meltryllis.nf.entity.property;

import cc.meltryllis.nf.entity.Regex;
import cc.meltryllis.nf.entity.config.InputFormat;
import cn.hutool.core.collection.CollectionUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

import java.io.File;

/**
 * {@link cc.meltryllis.nf.entity.config.InputFormat} 的响应式属性。
 *
 * @author Zachary W
 * @date 2025/2/20
 */
@Getter
public class InputFormatProperty {

    public static final UniqueListProperty<Integer> CHAPTER_MAX_LENGTH_UNIQUE_LIST = new UniqueListProperty<>(
            CollectionUtil.newArrayList(15, 20, 25, 30, 35, 40, 45, 50, 60, 70, 80, 90, 100, 200, 300));
    private static InputFormatProperty instance;
    private SimpleObjectProperty<File> fileProperty;
    private SimpleIntegerProperty chapterMaxLengthProperty;
    private UniqueListProperty<Regex> regexUniqueListProperty;

    private InputFormatProperty() {
        if (instance == null) {
            initProperties();
        } else {
            throw new RuntimeException();
        }
    }

    public static InputFormatProperty getInstance() {
        if (instance == null) {
            instance = new InputFormatProperty();
        }
        return instance;
    }

    private void initProperties() {
        InputFormat instance = InputFormat.getInstance();
        fileProperty = new SimpleObjectProperty<>(instance.getFile());
        fileProperty.addListener((observable, oldValue, newValue) -> instance.setFile(newValue));
        chapterMaxLengthProperty = new SimpleIntegerProperty(instance.getChapterMaxLength());
        chapterMaxLengthProperty.addListener(
                (observable, oldValue, newValue) -> instance.setChapterMaxLength(newValue.intValue()));
        regexUniqueListProperty = new UniqueListProperty<>(instance.getChapterRegexList());
    }
}

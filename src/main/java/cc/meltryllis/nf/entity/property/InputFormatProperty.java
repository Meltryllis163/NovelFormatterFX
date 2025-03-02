package cc.meltryllis.nf.entity.property;

import cc.meltryllis.nf.entity.Regex;
import cc.meltryllis.nf.entity.config.FormatFactory;
import cc.meltryllis.nf.entity.config.InputFormat;
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

    private static InputFormatProperty           instance;
    public         UniqueObservableList<Integer> chapterMaxLengthUniqueObservableList;
    private        SimpleObjectProperty<File>    fileProperty;
    private        SimpleIntegerProperty         chapterMaxLengthProperty;
    private        UniqueObservableList<Regex>   regexUniqueObservableList;

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
        chapterMaxLengthUniqueObservableList = new UniqueObservableList<>(
                FormatFactory.createDefaultChapterMaxLengthList(), Integer::compareTo);
        fileProperty = new SimpleObjectProperty<>(instance.getFile());
        fileProperty.addListener((observable, oldValue, newValue) -> instance.setFile(newValue));
        chapterMaxLengthProperty = new SimpleIntegerProperty(instance.getChapterMaxLength());
        chapterMaxLengthProperty.addListener(
                (observable, oldValue, newValue) -> instance.setChapterMaxLength(newValue.intValue()));
        regexUniqueObservableList = new UniqueObservableList<>(instance.getChapterRegexList());
    }
}

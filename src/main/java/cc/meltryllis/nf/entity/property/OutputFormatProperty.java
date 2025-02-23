package cc.meltryllis.nf.entity.property;

import cc.meltryllis.nf.constants.CharacterCons;
import cc.meltryllis.nf.entity.Replacement;
import cc.meltryllis.nf.entity.config.OutputFormat;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;

/**
 * {@link cc.meltryllis.nf.entity.config.OutputFormat} 的响应式属性。
 *
 * @author Zachary W
 * @date 2025/2/20
 */
@Getter
public class OutputFormatProperty {

    private static OutputFormatProperty instance;
    private UniqueListProperty<String> chapterTemplateUniqueListProperty;
    private SimpleStringProperty selectedChapterTemplateProperty;
    private SimpleIntegerProperty blankLineCountProperty;
    private SimpleIntegerProperty spaceCountProperty;
    private SimpleObjectProperty<Character> spaceProperty;
    private SimpleBooleanProperty indentationForChapterProperty;
    private UniqueListProperty<Replacement> replacementUniqueListProperty;

    public OutputFormatProperty() {
        if (instance == null) {
            initProperties();
        } else {
            throw new RuntimeException();
        }
    }

    public static OutputFormatProperty getInstance() {
        if (instance == null) {
            instance = new OutputFormatProperty();
        }
        return instance;
    }

    public void initProperties() {
        OutputFormat outputFormat = OutputFormat.getInstance();

        chapterTemplateUniqueListProperty = new UniqueListProperty<>(outputFormat.getChapterTemplateList());

        selectedChapterTemplateProperty = new SimpleStringProperty(outputFormat.getSelectedChapterTemplate());
        selectedChapterTemplateProperty.addListener(
                (observable, oldValue, newValue) -> outputFormat.setSelectedChapterTemplate(newValue));

        blankLineCountProperty = new SimpleIntegerProperty(outputFormat.getBlankLineCount());
        blankLineCountProperty.addListener(
                (observable, oldValue, newValue) -> outputFormat.setBlankLineCount(newValue.intValue()));

        spaceCountProperty = new SimpleIntegerProperty(outputFormat.getSpaceCount());
        spaceCountProperty.addListener(
                (observable, oldValue, newValue) -> outputFormat.setSpaceCount(newValue.intValue()));

        spaceProperty = new SimpleObjectProperty<>(outputFormat.getSpace());
        spaceProperty.addListener((observable, oldValue, newValue) -> outputFormat.setSpace(
                newValue == null ? CharacterCons.CHINESE.SPACE : newValue));

        indentationForChapterProperty = new SimpleBooleanProperty(outputFormat.isIndentationForChapter());
        indentationForChapterProperty.addListener(
                (observable, oldValue, newValue) -> outputFormat.setIndentationForChapter(
                        newValue != null && newValue));

        replacementUniqueListProperty = new UniqueListProperty<>(outputFormat.getReplacementList(), (o1, o2) -> {
            if (o1 == null || o1.getOldText() == null) {
                return -1;
            }
            if (o2 == null || o2.getOldText() == null) {
                return 1;
            }
            return o1.getOldText().compareTo(o2.getOldText());
        });
    }
}

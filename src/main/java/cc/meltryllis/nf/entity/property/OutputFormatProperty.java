package cc.meltryllis.nf.entity.property;

import cc.meltryllis.nf.constants.CharacterCons;
import cc.meltryllis.nf.entity.ChapterFormat;
import cc.meltryllis.nf.entity.Replacement;
import cc.meltryllis.nf.entity.config.OutputFormat;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

/**
 * {@link cc.meltryllis.nf.entity.config.OutputFormat} 的响应式属性。
 *
 * @author Zachary W
 * @date 2025/2/20
 */
@Getter
public class OutputFormatProperty {

    private static OutputFormatProperty                instance;
    private        UniqueObservableList<ChapterFormat> chapterFormatUniqueObservableList;
    private        SimpleObjectProperty<ChapterFormat> selectedChapterFormatProperty;
    private        SimpleIntegerProperty               blankLineCountProperty;
    private SimpleIntegerProperty spaceCountProperty;
    private SimpleObjectProperty<Character> spaceProperty;
    private SimpleBooleanProperty indentationForChapterProperty;
    private        UniqueObservableList<Replacement>   replacementUniqueObservableList;

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

        chapterFormatUniqueObservableList = new UniqueObservableList<>(outputFormat.getChapterFormatList());

        selectedChapterFormatProperty = new SimpleObjectProperty<>(outputFormat.getSelectedChapterFormat());
        selectedChapterFormatProperty.addListener(
                (observable, oldValue, newValue) -> outputFormat.setSelectedChapterFormat(newValue));

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

        replacementUniqueObservableList = new UniqueObservableList<>(outputFormat.getReplacementList(), (o1, o2) -> {
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

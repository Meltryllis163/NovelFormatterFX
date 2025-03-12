package cc.meltryllis.nf.entity.property.output;

import cc.meltryllis.nf.constants.DataCons;
import cc.meltryllis.nf.entity.FormatFactory;
import cc.meltryllis.nf.entity.property.UniqueObservableList;
import cc.meltryllis.nf.utils.jackson.JSONUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 「小说导出」配置项的响应式属性。
 *
 * @author Zachary W
 * @date 2025/2/20
 */
@Getter
@ToString
public class OutputFormatProperty {

    @JsonIgnore
    private static OutputFormatProperty instance;

    @JsonIgnore
    private UniqueObservableList<ChapterTemplateProperty> chapterTemplateUniqueList;
    @JsonIgnore
    private SimpleObjectProperty<ChapterTemplateProperty> selectedChapterTemplateObjectProperty;
    @JsonIgnore
    private SimpleBooleanProperty                         autoNumberForChapterProperty;
    @JsonIgnore
    private UniqueObservableList<ReplacementProperty>     replacementPropertyUniqueObservableList;

    @Setter
    @JsonProperty("paragraph")
    private ParagraphProperty paragraphProperty;


    @JsonCreator
    public OutputFormatProperty(
            @JsonProperty("chapterTemplateList") List<ChapterTemplateProperty> chapterTemplateProperties) {
        this(chapterTemplateProperties, null);
    }

    public OutputFormatProperty(List<ChapterTemplateProperty> chapterTemplateProperties,
                                ParagraphProperty paragraphProperty) {
        if (instance == null) {
            initProperties(chapterTemplateProperties, paragraphProperty);
        } else {
            throw new RuntimeException();
        }
    }

    public static OutputFormatProperty getInstance() {
        if (instance == null) {
            OutputFormatProperty property = JSONUtil.parseObject(new File(DataCons.OUTPUT_FORMAT_CONFIG),
                    OutputFormatProperty.class);
            instance = property == null ? FormatFactory.createDefaultOutputFormatProperty() : property;
        }
        return instance;
    }

    public static void store() {
        JSONUtil.storeFile(new File(DataCons.OUTPUT_FORMAT_CONFIG), getInstance());
    }

    public void initProperties(List<ChapterTemplateProperty> chapterTemplateProperties,
                               ParagraphProperty paragraphProperty) {

        chapterTemplateUniqueList = new UniqueObservableList<>(chapterTemplateProperties);
        selectedChapterTemplateObjectProperty = new SimpleObjectProperty<>();
        autoNumberForChapterProperty = new SimpleBooleanProperty(false);


        replacementPropertyUniqueObservableList = new UniqueObservableList<>(new ArrayList<>());
        replacementPropertyUniqueObservableList.setComparator((r1, r2) -> {
            if (r1 == null || r1.getOldText() == null) {
                return -1;
            }
            if (r2 == null || r2.getOldText() == null) {
                return 1;
            }
            return r1.getOldText().compareTo(r2.getOldText());
        });

        this.paragraphProperty = paragraphProperty;
    }

    @JsonGetter
    public List<ChapterTemplateProperty> getChapterTemplateList() {
        return getChapterTemplateUniqueList().toList();
    }

    @JsonIgnore
    public String getSelectedChapterTemplateText() {
        return getSelectedChapterTemplateObjectProperty().getValue().getTemplateText();
    }

    @JsonIgnore
    public boolean isAutoNumberForChapter() {
        return getAutoNumberForChapterProperty().getValue();
    }

    @JsonIgnore
    public List<ReplacementProperty> getReplacementPropertyList() {
        return getReplacementPropertyUniqueObservableList().toList();
    }
}

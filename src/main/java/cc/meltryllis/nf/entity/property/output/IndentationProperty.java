package cc.meltryllis.nf.entity.property.output;

import cc.meltryllis.nf.constants.CharacterCons;
import cc.meltryllis.nf.utils.common.StrUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import lombok.Getter;

/**
 * 「缩进格式」的响应式属性。
 *
 * @author Zachary W
 * @date 2025/3/4
 */
@Getter
public class IndentationProperty {

    @JsonIgnore
    private final SimpleObjectProperty<Space> spaceProperty;
    @JsonIgnore
    private final SimpleIntegerProperty       spaceCountProperty;
    @JsonIgnore
    private final SimpleBooleanProperty       effectiveForChapterProperty;

    @JsonCreator
    public IndentationProperty(@JsonProperty(value = "space", defaultValue = "CHINESE_SPACE") Space space,
                               @JsonProperty(value = "spaceCount", defaultValue = "2") int spaceCount,
                               @JsonProperty(value = "effectiveForChapter", defaultValue = "false") boolean effectiveForChapter) {
        spaceProperty = new SimpleObjectProperty<>(space);
        spaceCountProperty = new SimpleIntegerProperty(spaceCount);
        effectiveForChapterProperty = new SimpleBooleanProperty(effectiveForChapter);
    }

    @JsonGetter
    public Space getSpace() {
        return getSpaceProperty().getValue();
    }

    public void setSpace(Space space) {
        getSpaceProperty().setValue(space);
    }

    @JsonGetter
    public int getSpaceCount() {
        return getSpaceCountProperty().getValue();
    }

    public void setSpaceCount(int count) {
        getSpaceCountProperty().setValue(count);
    }

    @JsonGetter
    public boolean isEffectiveForChapter() {
        return getEffectiveForChapterProperty().getValue();
    }

    public void setEffectiveForChapter(boolean effectiveForChapter) {
        getEffectiveForChapterProperty().setValue(effectiveForChapter);
    }

    public String generateIndentationSpace() {
        return StrUtil.repeat(getSpace().getSpaceChar(), getSpaceCount());
    }

    @Getter
    public enum Space {
        CHINESE_SPACE(I18nUtil.createStringBinding("Common.Character.FullWidthSpace"), CharacterCons.CHINESE.SPACE),
        SPACE(I18nUtil.createStringBinding("Common.Character.Space"), CharacterCons.ENGLISH.SPACE);

        private final StringProperty nameProperty = new SimpleStringProperty();
        private final char           spaceChar;

        Space(ObservableValue<String> nameProperty, char spaceChar) {
            this.nameProperty.bind(nameProperty);
            this.spaceChar = spaceChar;
        }
    }

}

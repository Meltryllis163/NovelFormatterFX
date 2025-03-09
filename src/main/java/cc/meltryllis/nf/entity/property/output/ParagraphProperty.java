package cc.meltryllis.nf.entity.property.output;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 「段落格式」的响应式属性。
 *
 * @author Zachary W
 * @date 2025/3/4
 */
@Getter
public class ParagraphProperty {

    @JsonIgnore
    private final SimpleIntegerProperty blankLineCountProperty;
    @JsonIgnore
    private final SimpleBooleanProperty resegmentProperty;
    @Setter
    @JsonProperty("indentation")
    private       IndentationProperty   indentationProperty;

    @JsonCreator
    public ParagraphProperty(@JsonProperty("blankLineCount") int blankLineCount,
                             @JsonProperty("resegment") boolean resegment) {
        this(blankLineCount, resegment, null);
    }

    public ParagraphProperty(int blankLineCount, boolean resegment, IndentationProperty indentationProperty) {
        // 空行
        blankLineCountProperty = new SimpleIntegerProperty(blankLineCount);
        // 缩进
        this.indentationProperty = indentationProperty;
        // 重新分段
        resegmentProperty = new SimpleBooleanProperty(resegment);
    }

    @JsonGetter
    public int getBlankLineCount() {
        return getBlankLineCountProperty().getValue();
    }

    public void setBlankLineCount(int count) {
        getBlankLineCountProperty().setValue(count);
    }

    @JsonGetter
    public boolean isResegment() {
        return getResegmentProperty().getValue();
    }

    public void setResegment(boolean resegment) {
        getResegmentProperty().setValue(resegment);
    }
}

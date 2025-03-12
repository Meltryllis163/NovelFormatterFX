package cc.meltryllis.nf.entity.property.input;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.Getter;

/**
 * 「章节长度」响应式属性。
 *
 * @author Zachary W
 * @date 2025/3/7
 */
@Getter
public class ChapterLengthProperty {

    @JsonIgnore
    private final SimpleIntegerProperty maxLengthProperty;

    @JsonCreator
    public ChapterLengthProperty(@JsonProperty(value = "maxLength", defaultValue = "20") int maxLength) {
        maxLengthProperty = new SimpleIntegerProperty(maxLength);
    }

    @JsonGetter
    public int getMaxLength() {
        return getMaxLengthProperty().getValue();
    }

    public void setMaxLength(int maxLength) {
        getMaxLengthProperty().setValue(maxLength);
    }
}

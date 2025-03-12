package cc.meltryllis.nf.entity.property.input;

import cc.meltryllis.nf.ui.common.ICopyableTexts;
import cc.meltryllis.nf.utils.common.StrUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式响应式属性。
 *
 * @author Zachary W
 * @date 2025/3/5
 */
@Getter
public class RegexProperty implements ICopyableTexts {

    @NotNull
    @JsonIgnore
    private final SimpleStringProperty          descriptionProperty;
    @NotNull
    @JsonIgnore
    private final SimpleObjectProperty<Pattern> patternProperty;
    @NotNull
    @JsonIgnore
    private final SimpleBooleanProperty         enabledProperty;

    @JsonCreator
    public RegexProperty(@JsonProperty("description") String description, @JsonProperty("pattern") String pattern) {
        descriptionProperty = new SimpleStringProperty(description);
        patternProperty = new SimpleObjectProperty<>(Pattern.compile(pattern));
        enabledProperty = new SimpleBooleanProperty();
    }

    @JsonGetter
    public String getDescription() {
        return getDescriptionProperty().getValue();
    }

    public void setDescription(String description) {
        getDescriptionProperty().setValue(description);
    }

    @JsonGetter
    public Pattern getPattern() {
        return getPatternProperty().getValue();
    }

    public void setPattern(String pattern) {
        getPatternProperty().setValue(Pattern.compile(pattern));
    }

    @JsonIgnore
    public boolean isEnabled() {
        return getEnabledProperty().getValue();
    }

    public void setEnabled(boolean enabled) {
        getEnabledProperty().setValue(enabled);
    }

    @Nullable
    public Matcher matcher(String s) {
        if (!isEnabled()) {
            return null;
        }
        return getPattern().matcher(s);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof RegexProperty regexProperty) {
            return StrUtil.equals(getPattern().toString(), regexProperty.getPattern().toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int res = 1;
        res = res * prime + (getPattern().toString() == null ? 0 : getPattern().toString().hashCode());
        return res;
    }

    @Override
    public String[] getCopyableTexts() {
        return new String[] {
                getDescription(),
                getPattern().toString()
        };
    }
}

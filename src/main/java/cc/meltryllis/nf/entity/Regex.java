package cc.meltryllis.nf.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析用正则表达式。
 *
 * @author Zachary W
 * @date 2025/2/5
 */
@NoArgsConstructor
@Getter
@Setter
public class Regex {

    /** 正则功能描述。主要用于用户了解该正则的匹配样式。 */
    private String  description;
    /** 正则表达式 */
    private Pattern pattern;

    @JsonIgnore
    private SimpleBooleanProperty enabledProperty = new SimpleBooleanProperty(false);

    public Regex(String description, String pattern) {
        this(description, Pattern.compile(pattern));
    }

    public Regex(String description, Pattern pattern) {
        this(description, pattern, false);
    }

    public Regex(String description, Pattern pattern, boolean enabled) {
        this.description = description;
        this.pattern = pattern;
        this.enabledProperty.setValue(enabled);
    }

    @Nullable
    public Matcher matcher(String s) {
        if (!getEnabledProperty().getValue()) {
            return null;
        }
        return pattern.matcher(s);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof Regex) {
            return pattern.equals(((Regex) obj).getPattern());
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int res = 1;
        res = res * prime + (pattern == null ? 0 : pattern.hashCode());
        return res;
    }
}

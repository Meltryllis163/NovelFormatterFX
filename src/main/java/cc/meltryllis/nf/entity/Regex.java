package cc.meltryllis.nf.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Getter
@Setter
public class Regex {

    /** 正则功能描述。主要用于用户了解该正则的功能样式。 */
    private String description;
    private Pattern pattern;
    @JsonIgnore
    private boolean isEnabled;

    public Regex(String description, String pattern) {
        this(description, Pattern.compile(pattern));
    }

    public Regex(String description, Pattern pattern) {
        this(description, pattern, false);
    }

    @Nullable
    public Matcher matcher(String s) {
        if (!isEnabled()) {
            return null;
        }
        return pattern.matcher(s);
    }

}

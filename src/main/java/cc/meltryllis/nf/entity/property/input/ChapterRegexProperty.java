package cc.meltryllis.nf.entity.property.input;

import cc.meltryllis.nf.entity.property.UniqueObservableList;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * 「章节正则解析」响应式属性。
 *
 * @author Zachary W
 * @date 2025/3/4
 */
@Getter
@ToString
public class ChapterRegexProperty {

    @JsonIgnore
    private final UniqueObservableList<RegexProperty> regexPropertyUniqueObservableList;

    @JsonCreator
    public ChapterRegexProperty(@JsonProperty(value = "regexList", defaultValue = "[]") List<RegexProperty> regexList) {
        regexPropertyUniqueObservableList = new UniqueObservableList<>(regexList);
    }

    @JsonGetter
    public List<RegexProperty> getRegexList() {
        return getRegexPropertyUniqueObservableList().toList();
    }
}

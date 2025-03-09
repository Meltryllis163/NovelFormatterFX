package cc.meltryllis.nf.entity.property.output;

import cc.meltryllis.nf.utils.common.StrUtil;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * 「章节导出格式」响应式属性。
 *
 * @author Zachary W
 * @date 2025/2/24
 */
@Getter
@Setter
public class ChapterTemplateProperty {

    /** 中文章节编号占位符 */
    public static final String CHINESE_NUMBER_PLACEHOLDER = "{cnum}";
    /** 阿拉伯数字章节编号占位符 */
    public static final String ARABIC_NUMBER_PLACEHOLDER  = "{num}";
    /** 章节标题占位符 */
    public static final String TITLE_PLACEHOLDER          = "{title}";

    /** 默认章节模板文本 */
    private static final String                  DEFAULT_TEMPLATE_TEXT    = "第" + CHINESE_NUMBER_PLACEHOLDER + "章 " + TITLE_PLACEHOLDER;
    /** 默认章节格式实例 */
    public static final  ChapterTemplateProperty DEFAULT_CHAPTER_TEMPLATE = new ChapterTemplateProperty(
            DEFAULT_TEMPLATE_TEXT);

    @NotNull
    @JsonIgnore
    private final SimpleBooleanProperty selectedProperty;

    @NotNull
    @JsonIgnore
    private final SimpleStringProperty templateProperty;

    public ChapterTemplateProperty(@JsonProperty("template") String template) {
        this.templateProperty = new SimpleStringProperty(template);
        this.selectedProperty = new SimpleBooleanProperty();
    }

    @JsonGetter("template")
    public String getTemplateText() {
        return getTemplateProperty().getValue();
    }

    public void setTemplateText(String text) {
        getTemplateProperty().setValue(text);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof ChapterTemplateProperty) {
            return StrUtil.equals(getTemplateText(), ((ChapterTemplateProperty) obj).getTemplateText());
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int res = 1;
        res = res * prime + getTemplateText().hashCode();
        return res;
    }
}

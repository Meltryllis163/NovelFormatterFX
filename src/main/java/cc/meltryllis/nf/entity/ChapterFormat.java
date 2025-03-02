package cc.meltryllis.nf.entity;

import cc.meltryllis.nf.utils.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * 章节导出格式。
 *
 * @author Zachary W
 * @date 2025/2/24
 */
@Getter
@Setter
public class ChapterFormat {

    /** 中文章节编号占位符 */
    public static final String CHINESE_NUMBER_PLACEHOLDER = "{cnum}";
    /** 阿拉伯数字章节编号占位符 */
    public static final String ARABIC_NUMBER_PLACEHOLDER  = "{num}";
    /** 章节标题占位符 */
    public static final String TITLE_PLACEHOLDER          = "{title}";

    /** 默认章节模板文本 */
    private static final String        DEFAULT_TEMPLATE       = "第" + CHINESE_NUMBER_PLACEHOLDER + "章 " + TITLE_PLACEHOLDER;
    /** 默认章节格式实例 */
    public static final  ChapterFormat DEFAULT_CHAPTER_FORMAT = new ChapterFormat(DEFAULT_TEMPLATE);

    @JsonIgnore
    private final SimpleBooleanProperty selectedProperty = new SimpleBooleanProperty();

    @NotNull
    private final String templateText;

    public ChapterFormat() {
        this(DEFAULT_TEMPLATE);
    }

    public ChapterFormat(@NotNull String template) {
        this.templateText = template;
        selectedProperty.setValue(false);
    }

    public String exampleChapter() {
        return Chapter.exampleFormat(this.templateText);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof ChapterFormat) {
            return StrUtil.equals(getTemplateText(), ((ChapterFormat) obj).getTemplateText());
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

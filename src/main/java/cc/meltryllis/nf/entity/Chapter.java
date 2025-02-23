package cc.meltryllis.nf.entity;

import cc.meltryllis.nf.utils.I18nUtil;
import cc.meltryllis.nf.utils.NumberUtil;
import cn.hutool.core.convert.NumberChineseFormatter;
import cn.hutool.core.util.StrUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 章节实体类。
 *
 * @author Zachary W
 * @date 2025/2/13
 */
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PRIVATE)
public class Chapter {
    /** 中文章节编号占位符 */
    public static final String CHINESE_NUMBER_PLACEHOLDER = "{chineseNumber}";
    /** 阿拉伯数字章节编号占位符 */
    public static final String ARABIC_NUMBER_PLACEHOLDER = "{number}";
    /** 章节标题占位符 */
    public static final String TITLE_PLACEHOLDER = "{title}";

    private static final Chapter EXAMPLE_CHAPTER = new Chapter(1024, I18nUtil.get("Common.Novel.Chapter.ChapterName"));

    /** 章节编号 */
    private final int number;
    /** 章节名 */
    private final String title;

    public Chapter(String numberString, @Nullable String title) throws IllegalArgumentException {
        this.title = (StrUtil.trim(title));
        this.number = NumberUtil.parseNumber(numberString);
    }

    @NotNull
    public static String exampleFormat(@Nullable String template) {
        return EXAMPLE_CHAPTER.format(template);
    }

    @NotNull
    public String format(@Nullable String template) {
        if (StrUtil.isEmpty(template)) {
            return "";
        }
        template = template.replace(CHINESE_NUMBER_PLACEHOLDER,
                NumberChineseFormatter.format(getNumber(), false));
        template = template.replace(ARABIC_NUMBER_PLACEHOLDER, String.valueOf(getNumber()));
        return template.replace(TITLE_PLACEHOLDER, getTitle());
    }
}

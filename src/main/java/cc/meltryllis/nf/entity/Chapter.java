package cc.meltryllis.nf.entity;

import cc.meltryllis.nf.utils.ChineseUtil;
import cc.meltryllis.nf.utils.I18nUtil;
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
    /** 替换对应占位符的正则 */
    public static final String CHINESE_NUMBER_PLACEHOLDER_REGEX = "\\{chineseNumber}";
    /** 阿拉伯数字章节编号占位符 */
    public static final String ARABIC_NUMBER_PLACEHOLDER = "{number}";
    /** 替换对应占位符的正则 */
    public static final String ARABIC_NUMBER_PLACEHOLDER_REGEX = "\\{number}";
    /** 章节标题占位符 */
    public static final String TITLE_PLACEHOLDER = "{title}";
    /** 替换对应占位符的正则 */
    public static final String TITLE_PLACEHOLDER_REGEX = "\\{title}";

    private static final Chapter EXAMPLE_CHAPTER = new Chapter(1024, I18nUtil.get("Common.Novel.Chapter.ChapterName"));

    /** 章节编号 */
    private int number;
    /** 章节名 */
    private String title;

    public Chapter(String numberString, @Nullable String title) throws IllegalArgumentException {
        setTitle(StrUtil.trim(title));
        setStringNumber(numberString);
    }

    private void setStringNumber(String numberString) {
        numberString = StrUtil.trim(numberString);
        if (StrUtil.isBlank(numberString)) {
            throw new IllegalArgumentException("Chapter number string is blank.");
        }
        if (ChineseUtil.isChineseNumber(numberString.charAt(0))) {
            this.number = ChineseUtil.chineseToNumber(numberString);
        } else {
            this.number = Integer.parseInt(numberString);
        }
    }

    @NotNull
    public static String exampleFormat(@Nullable String template) {
        return EXAMPLE_CHAPTER.format(template);
    }

    @NotNull
    public String format(@Nullable String template) {
        if (template == null) {
            return "";
        }
        template = template.replaceAll(CHINESE_NUMBER_PLACEHOLDER_REGEX,
                NumberChineseFormatter.format(getNumber(), false));
        template = template.replaceAll(ARABIC_NUMBER_PLACEHOLDER_REGEX, String.valueOf(getNumber()));
        return template.replaceAll(TITLE_PLACEHOLDER_REGEX, getTitle());
    }
}

package cc.meltryllis.nf.entity;

import cc.meltryllis.nf.utils.I18nUtil;
import cc.meltryllis.nf.utils.NumberUtil;
import cc.meltryllis.nf.utils.StrUtil;
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

    private static final Chapter EXAMPLE_CHAPTER = new Chapter(null, 1024,
            I18nUtil.get("Common.Novel.Chapter.ChapterName"));

    /** 原文本 */
    private final String text;
    /** 章节编号 */
    private final int    number;
    /** 章节名 */
    private final String title;

    public Chapter(String text, String numberString, @Nullable String title) throws IllegalArgumentException {
        this.text = StrUtil.trim(text);
        this.title = StrUtil.trim(title);
        this.number = NumberUtil.parseNumber(numberString);
    }

    @NotNull
    public static String exampleFormat(@Nullable String template) {
        return EXAMPLE_CHAPTER.format(template);
    }

    /**
     * 根据模板格式化章节。
     *
     * @param format 格式化模板。
     *
     * @return 格式化以后的章节。如果格式为空则使用默认格式。
     *
     * @see #format(String)
     */
    @NotNull
    public String format(@Nullable ChapterFormat format) {
        return format(
                format == null ? ChapterFormat.DEFAULT_CHAPTER_FORMAT.getTemplateText() : format.getTemplateText());
    }

    /**
     * 根据模板格式化章节。
     *
     * @param template 格式化模板。
     *
     * @return 格式化以后的章节。如果格式为空则使用默认格式。
     *
     * @see ChapterFormat#CHINESE_NUMBER_PLACEHOLDER
     * @see ChapterFormat#ARABIC_NUMBER_PLACEHOLDER
     * @see ChapterFormat#TITLE_PLACEHOLDER
     * @see ChapterFormat#DEFAULT_CHAPTER_FORMAT
     */
    private String format(@Nullable String template) {
        if (StrUtil.isEmpty(template)) {
            template = ChapterFormat.DEFAULT_CHAPTER_FORMAT.getTemplateText();
        }
        template = template.replace(ChapterFormat.CHINESE_NUMBER_PLACEHOLDER, NumberUtil.format(getNumber(), false));
        template = template.replace(ChapterFormat.ARABIC_NUMBER_PLACEHOLDER, String.valueOf(getNumber()));
        return template.replace(ChapterFormat.TITLE_PLACEHOLDER, getTitle());
    }
}

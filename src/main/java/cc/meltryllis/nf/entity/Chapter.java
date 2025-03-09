package cc.meltryllis.nf.entity;

import cc.meltryllis.nf.entity.property.output.ChapterTemplateProperty;
import cc.meltryllis.nf.utils.common.NumberUtil;
import cc.meltryllis.nf.utils.common.StrUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
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
@Setter
public class Chapter {

    private static final Chapter EXAMPLE_CHAPTER = new Chapter(StrUtil.EMPTY, 1024,
            I18nUtil.get("Common.Novel.Chapter.ChapterName"));

    /** 原文本 */
    @NotNull
    private final String text;

    /** 章节编号 */
    private int    number;
    /** 章节名 */
    @NotNull
    private String title;

    public Chapter(@NotNull String text, @NotNull String numberString,
                   @NotNull String title) throws IllegalArgumentException {
        this.text = StrUtil.trim(text);
        this.title = StrUtil.trim(title);
        this.number = NumberUtil.parseNumber(numberString);
    }

    @NotNull
    public static String exampleText(@Nullable String template) {
        return EXAMPLE_CHAPTER.format(template);
    }

    /**
     * 根据模板格式化章节。
     *
     * @param templateProperty 格式化模板。
     *
     * @return 格式化以后的章节。如果格式为空则使用默认格式。
     *
     * @see #format(String)
     */
    @NotNull
    public String format(@Nullable ChapterTemplateProperty templateProperty) {
        return format(
                templateProperty == null ? ChapterTemplateProperty.DEFAULT_CHAPTER_TEMPLATE.getTemplateText() : templateProperty.getTemplateText());
    }

    /**
     * 根据模板格式化章节。
     *
     * @param template 格式化模板。
     *
     * @return 格式化以后的章节。如果格式为空则使用默认格式。
     *
     * @see ChapterTemplateProperty#CHINESE_NUMBER_PLACEHOLDER
     * @see ChapterTemplateProperty#ARABIC_NUMBER_PLACEHOLDER
     * @see ChapterTemplateProperty#TITLE_PLACEHOLDER
     * @see ChapterTemplateProperty#DEFAULT_CHAPTER_TEMPLATE
     */
    private String format(@Nullable String template) {
        if (StrUtil.isEmpty(template)) {
            template = ChapterTemplateProperty.DEFAULT_CHAPTER_TEMPLATE.getTemplateText();
        }
        template = template.replace(ChapterTemplateProperty.CHINESE_NUMBER_PLACEHOLDER,
                NumberUtil.format(getNumber(), false));
        template = template.replace(ChapterTemplateProperty.ARABIC_NUMBER_PLACEHOLDER, String.valueOf(getNumber()));
        return template.replace(ChapterTemplateProperty.TITLE_PLACEHOLDER, getTitle());
    }
}

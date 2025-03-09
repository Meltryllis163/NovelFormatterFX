package cc.meltryllis.nf.entity;

import cc.meltryllis.nf.entity.property.input.ChapterLengthProperty;
import cc.meltryllis.nf.entity.property.input.ChapterRegexProperty;
import cc.meltryllis.nf.entity.property.input.InputFormatProperty;
import cc.meltryllis.nf.entity.property.input.RegexProperty;
import cc.meltryllis.nf.entity.property.output.ChapterTemplateProperty;
import cc.meltryllis.nf.entity.property.output.IndentationProperty;
import cc.meltryllis.nf.entity.property.output.OutputFormatProperty;
import cc.meltryllis.nf.entity.property.output.ParagraphProperty;
import cc.meltryllis.nf.utils.common.CollectionUtil;

import java.util.List;

/**
 * 格式配置项相关对象的工厂。
 *
 * @author Zachary W
 * @date 2025/2/7
 */
public class FormatFactory {

    public static List<Integer> createDefaultChapterMaxLengthList() {
        return CollectionUtil.newArrayList(15, 20, 25, 30, 35, 40, 45, 50, 60, 70, 80, 90, 100, 200, 300);
    }

    private static List<ChapterTemplateProperty> createDefaultChapterFormatList() {
        return CollectionUtil.newArrayList(new ChapterTemplateProperty("第{cnum}章 {title}"),
                new ChapterTemplateProperty("第{num}章 {title}"));
    }

    private static IndentationProperty createDefaultIndentationProperty() {
        return new IndentationProperty(IndentationProperty.Space.CHINESE_SPACE, 2, false);
    }

    private static ParagraphProperty createDefaultParagraphProperty() {
        return new ParagraphProperty(1, false, createDefaultIndentationProperty());
    }

    public static OutputFormatProperty createDefaultOutputFormatProperty() {
        return new OutputFormatProperty(createDefaultChapterFormatList(), createDefaultParagraphProperty());
    }

    public static InputFormatProperty createDefaultInputFormatProperty() {
        InputFormatProperty property = new InputFormatProperty();
        property.setChapterRegexProperty(createDefaultChapterInputFormatProperty());
        property.setChapterLengthProperty(new ChapterLengthProperty(20));
        return property;
    }

    private static ChapterRegexProperty createDefaultChapterInputFormatProperty() {
        return new ChapterRegexProperty(createDefaultChapterRegexList());
    }

    private static List<RegexProperty> createDefaultChapterRegexList() {
        return CollectionUtil.newArrayList(new RegexProperty("第一千零二十四章 章节名",
                        "^第(?<chapterNumber>[零一壹二贰三叁四肆五伍六陆七柒八捌九玖十拾百佰千仟万萬亿]+)章\\s*(?<chapterTitle>[\\S\\s]*)"),
                new RegexProperty("第1024章 章节名", "^第(?<chapterNumber>[0-9]+)章\\s*(?<chapterTitle>[\\S\\s]*)"));
    }

}

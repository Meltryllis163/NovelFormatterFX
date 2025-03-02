package cc.meltryllis.nf.entity.config;

import cc.meltryllis.nf.constants.CharacterCons;
import cc.meltryllis.nf.entity.ChapterFormat;
import cc.meltryllis.nf.entity.Regex;
import cc.meltryllis.nf.utils.CollectionUtil;

import java.util.ArrayList;
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

    public static List<ChapterFormat> createDefaultChapterFormatList() {
        return CollectionUtil.newArrayList(new ChapterFormat("第{cnum}章 {title}"),
                new ChapterFormat("第{num}章 {title}"));
    }

    protected static OutputFormat createDefaultOutputFormat() {
        return OutputFormat.builder().chapterFormatList(createDefaultChapterFormatList()).blankLineCount(1)
                .spaceCount(2).space(CharacterCons.CHINESE.SPACE).indentationForChapter(false)
                .replacementList(new ArrayList<>()).build();
    }

    protected static InputFormat createDefaultInputFormat() {
        return InputFormat.builder().chapterMaxLength(20).chapterRegexList(createDefaultRegexList()).build();
    }

    private static List<Regex> createDefaultRegexList() {
        return CollectionUtil.newArrayList(new Regex("第一千零二十四章 章节名",
                        "^第(?<chapterNumber>[零一壹二贰三叁四肆五伍六陆七柒八捌九玖十拾百佰千仟万萬亿]+)章\\s*(?<chapterTitle>[\\S\\s]*)"),
                new Regex("第1024章 章节名", "^第(?<chapterNumber>[0-9]+)章\\s*(?<chapterTitle>[\\S\\s]*)"));
    }

}

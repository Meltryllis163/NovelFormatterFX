package cc.meltryllis.nf.entity.config;

import cc.meltryllis.nf.constants.CharacterCons;
import cc.meltryllis.nf.entity.Regex;
import cn.hutool.core.collection.CollectionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 格式配置项相关对象的工厂。
 *
 * @author Zachary W
 * @date 2025/2/7
 */
public class FormatFactory {

    public static List<String> createDefaultChapterTemplates() {
        return CollectionUtil.newArrayList("第{chineseNumber}章 {title}", "第{number}章 {title}");
    }

    protected static OutputFormat createDefaultOutputFormat() {
        return OutputFormat.builder().chapterTemplateList(createDefaultChapterTemplates()).blankLineCount(1)
                .spaceCount(2).space(CharacterCons.CHINESE.SPACE).indentationForChapter(false)
                .replacementList(new ArrayList<>()).build();
    }

    protected static InputFormat createDefaultInputFormat() {
        return InputFormat.builder().chapterMaxLength(20).chapterRegexList(createDefaultRegexList()).build();
    }

    private static List<Regex> createDefaultRegexList() {
        return CollectionUtil.newArrayList(new Regex("第一千零七十三章 章节名",
                "^第(?<chapterNumber>[零一二三四五六七八九十百千]+)章\\s*(?<chapterTitle>\\S*)"));
    }

}

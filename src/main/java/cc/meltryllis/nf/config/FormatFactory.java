package cc.meltryllis.nf.config;

import cc.meltryllis.nf.constants.CharacterCons;
import cc.meltryllis.nf.entity.Chapter;
import cc.meltryllis.nf.entity.Regex;
import cn.hutool.core.util.StrUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用于创建格式配置项的工厂。
 *
 * @author Zachary W
 * @date 2025/2/7
 */
public class FormatFactory {

    public static List<String> createDefaultChapterTemplates() {
        return Arrays.asList("第{chineseNumber}章 {title}", "第{number}章 {title}",
                "{chineseNumber}章 {title}", "{number}章 {title}",
                "{chineseNumber} {title}", "{number} {title}",
                "{chineseNumber}", "{number}");
    }

    protected static OutputFormat createDefaultOutputFormat() {
        return new OutputFormat(
                StrUtil.format("第{}章 {}", Chapter.CHINESE_NUMBER_PLACEHOLDER, Chapter.TITLE_PLACEHOLDER), 1, 2,
                CharacterCons.CHINESE.SPACE, false);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    protected static <T extends IFormat> T createDefaultFormat(Class<T> clazz) {
        if (clazz == InputFormat.class) {
            return (T) new InputFormat(createDefaultFormat(ChapterInputFormat.class));
        } else if (clazz == ChapterInputFormat.class) {
            return (T) new ChapterInputFormat(createDefaultRegexList(clazz), 50);
        }
        return null;
    }

    @NotNull
    private static <T extends IFormat> List<Regex> createDefaultRegexList(Class<T> clazz) {
        List<Regex> regexes = new ArrayList<>();
        if (clazz == ChapterInputFormat.class) {
            regexes.add(new Regex("第一千零七十三章 章节名",
                    "^第(?<chapterNumber>[零一二三四五六七八九十百千]+)章\\s*(?<chapterTitle>\\S*)"));
        }
        return regexes;
    }

}

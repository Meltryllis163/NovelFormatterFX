package cc.meltryllis.nf.parser;

import cc.meltryllis.nf.entity.Chapter;
import cc.meltryllis.nf.entity.Regex;
import cc.meltryllis.nf.entity.config.InputFormat;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;

/**
 * 章名解析器。
 *
 * @author Zachary W
 * @date 2025/2/5
 */
@Getter
@Setter
public class ChapterParser extends AbstractParser {

    public static final String  NUMBER_KEY = "chapterNumber";
    public static final String  TITLE_KEY  = "chapterTitle";
    private             Chapter chapter;

    public ChapterParser(String text) {
        super(text);
    }

    @Override
    protected boolean parse(@NotNull String trimmingText) {
        InputFormat inputFormat = InputFormat.getInstance();
        if (trimmingText.length() > inputFormat.getChapterMaxLength()) {
            return false;
        }
        for (Regex regex : inputFormat.getChapterRegexList()) {
            Matcher matcher = regex.matcher(trimmingText);
            if (matcher != null && matcher.matches()) {
                chapter = new Chapter(trimmingText, matcher.group(NUMBER_KEY), matcher.group(TITLE_KEY));
                return true;
            }
        }
        return false;
    }

}

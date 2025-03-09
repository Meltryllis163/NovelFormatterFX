package cc.meltryllis.nf.parser;

import cc.meltryllis.nf.entity.Chapter;
import cc.meltryllis.nf.entity.property.input.ChapterLengthProperty;
import cc.meltryllis.nf.entity.property.input.ChapterRegexProperty;
import cc.meltryllis.nf.entity.property.input.InputFormatProperty;
import cc.meltryllis.nf.entity.property.input.RegexProperty;
import cc.meltryllis.nf.utils.common.StrUtil;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
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
        ChapterLengthProperty lengthProperty = InputFormatProperty.getInstance().getChapterLengthProperty();
        ChapterRegexProperty regexProperty = InputFormatProperty.getInstance().getChapterRegexProperty();

        if (trimmingText.length() > lengthProperty.getMaxLength()) {
            return false;
        }
        for (RegexProperty regex : regexProperty.getRegexList()) {
            Matcher matcher = regex.matcher(trimmingText);
            if (matcher != null && matcher.matches()) {
                Map<String, Integer> namedGroups = matcher.namedGroups();
                String number = "0";
                if (namedGroups.containsKey(NUMBER_KEY) && matcher.group(NUMBER_KEY) != null) {
                    number = matcher.group(NUMBER_KEY);
                }
                String title = StrUtil.EMPTY;
                if (namedGroups.containsKey(TITLE_KEY) && matcher.group(TITLE_KEY) != null) {
                    title = matcher.group(TITLE_KEY);
                }
                chapter = new Chapter(trimmingText, number, title);
                return true;
            }
        }
        return false;
    }

}

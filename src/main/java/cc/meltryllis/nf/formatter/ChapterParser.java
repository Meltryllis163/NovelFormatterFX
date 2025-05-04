package cc.meltryllis.nf.formatter;

import cc.meltryllis.nf.entity.property.input.ChapterLengthProperty;
import cc.meltryllis.nf.entity.property.input.ChapterRegexProperty;
import cc.meltryllis.nf.entity.property.input.InputFormatProperty;
import cc.meltryllis.nf.entity.property.input.RegexProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * 章节行解析器。
 *
 * @author Zachary W
 * @date 2025/2/5
 */
@Slf4j
@Getter
@Setter
public class ChapterParser extends AbstractParser {

    public static final String NUMBER_KEY = "chapterNumber";
    public static final String TITLE_KEY  = "chapterTitle";

    private final List<RegexProperty> enabledRegexes = new ArrayList<>();
    private final int                 chapterMaxLength;

    private Matcher matcher;
    private String  chapterText;

    public ChapterParser() {
        ChapterLengthProperty lengthProperty = InputFormatProperty.getInstance().getChapterLengthProperty();
        this.chapterMaxLength = lengthProperty.getMaxLength();
        ChapterRegexProperty regexProperty = InputFormatProperty.getInstance().getChapterRegexProperty();
        for (RegexProperty regex : regexProperty.getRegexList()) {
            if (regex.isEnabled()) {
                enabledRegexes.add(regex);
            }
        }
    }

    protected void reset() {
        setMatcher(null);
    }

    @Override
    protected boolean parse(@NotNull String trimmingText) {
        reset();
        if (trimmingText.length() > chapterMaxLength) {
            return false;
        }
        for (RegexProperty regex : enabledRegexes) {
            Matcher matcher = regex.matcher(trimmingText);
            if (matcher != null && matcher.matches()) {
                this.matcher = matcher;
                this.chapterText = trimmingText;
                return true;
            }
        }
        return false;
    }

}

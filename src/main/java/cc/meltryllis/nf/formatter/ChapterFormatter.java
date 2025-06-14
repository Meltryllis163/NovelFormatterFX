package cc.meltryllis.nf.formatter;

import cc.meltryllis.nf.entity.Chapter;
import cc.meltryllis.nf.entity.property.output.IndentationProperty;
import cc.meltryllis.nf.entity.property.output.OutputFormatProperty;
import cc.meltryllis.nf.utils.common.NumberUtil;
import cc.meltryllis.nf.utils.common.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.regex.Matcher;

/**
 * 章节行格式化器。
 *
 * @author Zachary W
 * @date 2025/3/20
 */
@Slf4j
public class ChapterFormatter extends AbstractFormatter<ChapterParser> {

    private final String  template;
    private final boolean autoNumberForChapter;
    private final String  indentation;
    private       int     chapterCount;


    public ChapterFormatter(@NotNull BufferedWriter writer) {
        super(new ChapterParser(), writer);
        this.template = OutputFormatProperty.getInstance().getSelectedChapterTemplateText();
        this.autoNumberForChapter = OutputFormatProperty.getInstance().isAutoNumberForChapter();
        this.chapterCount = 0;
        IndentationProperty indentationProperty = OutputFormatProperty.getInstance().getParagraphProperty()
                .getIndentationProperty();
        this.indentation = indentationProperty.isEffectiveForChapter() ? indentationProperty.generateIndentationSpace() : StrUtil.EMPTY;
    }

    @Override
    protected boolean format() {
        super.format();
        chapterCount++;
        Matcher matcher = parser.getMatcher();
        String formatChapterText = parser.getChapterText();
        String title;
        try {
            title = matcher.group(ChapterParser.TITLE_KEY);
        } catch (IllegalArgumentException e) {
            title = StrUtil.EMPTY;
        }
        Chapter chapter = new Chapter(chapterCount, title);
        if (autoNumberForChapter) {
            formatChapterText = chapter.format(template);
        } else if (matcher.group(ChapterParser.NUMBER_KEY) != null) {
            try {
                int number = NumberUtil.parseNumber(matcher.group(ChapterParser.NUMBER_KEY));
                chapter.setNumber(number);
            } catch (IllegalArgumentException e) {
                chapter.setNumber(0);
            } finally {
                formatChapterText = chapter.format(template);
            }
        }
        try {
            writer.write(indentation);
            writer.write(formatChapterText);
            writer.newLine();
            return true;
        } catch (IOException e) {
            log.info("Write format chapter failed.", e);
            return false;
        }
    }

}

package cc.meltryllis.nf.formatter;

import cc.meltryllis.nf.entity.property.output.OutputFormatProperty;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Objects;

/**
 * 正文格式化器。
 *
 * @author Zachary W
 * @date 2025/3/24
 */
@Slf4j
public class ContentFormatter extends AbstractFormatter<ContentParser> {

    private final String        indentation;
    private final boolean       resegment;
    private final StringBuilder resegmentBuilder = new StringBuilder();

    public ContentFormatter(@NotNull BufferedWriter writer) {
        super(new ContentParser(), writer);
        this.indentation = OutputFormatProperty.getInstance().getParagraphProperty().getIndentationProperty()
                .generateIndentationSpace();
        this.resegment = OutputFormatProperty.getInstance().getParagraphProperty().isResegment();
    }

    @Override
    protected boolean format() {
        super.format();
        try {
            if (resegment) {
                resegmentBuilder.append(parser.getTrimmingText());
                if (parser.isComplete()) {
                    writer.write(indentation);
                    writer.write(resegmentBuilder.toString());
                    writer.newLine();
                    clearResegmentBuilder();
                    return true;
                }
                return false;
            } else {
                writer.write(indentation);
                writer.write(Objects.requireNonNull(parser.getTrimmingText()));
                writer.newLine();
                return true;
            }
        } catch (IOException e) {
            log.warn("Write format content failed.", e);
            return false;
        }
    }

    protected void clearResegmentBuilder() {
        this.resegmentBuilder.setLength(0);
    }

    protected void nextChapter() {
        if (!resegmentBuilder.isEmpty()) {
            try {
                writer.write(resegmentBuilder.toString());
                writer.newLine();
            } catch (IOException e) {
                log.warn("Write format content failed.", e);
            }
        }
        clearResegmentBuilder();
    }

}

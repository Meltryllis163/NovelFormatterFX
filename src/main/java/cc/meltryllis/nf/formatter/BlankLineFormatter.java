package cc.meltryllis.nf.formatter;

import cc.meltryllis.nf.entity.property.output.OutputFormatProperty;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * 空行格式化器。
 *
 * @author Zachary W
 * @date 2025/3/20
 */
@Slf4j
public class BlankLineFormatter extends AbstractFormatter<BlankLineParser> {

    private final int blankLineCount;

    public BlankLineFormatter(@NotNull BufferedWriter writer) {
        super(new BlankLineParser(), writer);
        this.blankLineCount = OutputFormatProperty.getInstance().getParagraphProperty().getBlankLineCount();
    }

    @Override
    protected boolean format() {
        return false;
    }

    protected void writeNewLines() {
        try {
            for (int i = 0; i < blankLineCount; i++) {
                writer.newLine();
            }
        } catch (IOException e) {
            log.warn("Write blank lines failed.", e);
        }
    }

}

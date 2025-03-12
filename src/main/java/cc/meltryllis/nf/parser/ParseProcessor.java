package cc.meltryllis.nf.parser;

import cc.meltryllis.nf.constants.FileCons;
import cc.meltryllis.nf.entity.Chapter;
import cc.meltryllis.nf.entity.property.output.OutputFormatProperty;
import cc.meltryllis.nf.entity.property.output.ReplacementProperty;
import cc.meltryllis.nf.utils.common.FileUtil;
import cc.meltryllis.nf.utils.common.StrUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import cc.meltryllis.nf.utils.message.dialog.DialogUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.util.Objects;

/**
 * 小说整体流程解析器。
 *
 * @author Zachary W
 * @date 2025/2/5
 */
@Slf4j
@Getter
public class ParseProcessor {


    private final File          file;
    private       File          outputFile;
    private       int           autoChapterNumber;
    private       StringBuilder resegmentContent;


    public ParseProcessor(File file) {
        this.file = file;
    }

    /**
     * 格式化小说。
     *
     * @return 格式化完成则返回 {@code true}，否则返回 {@code false}。
     */
    public boolean format() throws IOException {
        // 判断是否为TXT文件
        if (file == null || !file.exists() || !file.isFile() || !FileCons.TXT_SUFFIX.equalsIgnoreCase(
                FileUtil.getSuffix(file))) {
            DialogUtil.showMessage(I18nUtil.createStringBinding("Dialog.FileNotFound.Title"),
                    I18nUtil.createStringBinding("Dialog.FileNotFound.Desc"), DialogUtil.Type.WARNING);
            return false;
        }
        // 设定输出文件对象
        outputFile = new File(file.getParent(), FileUtil.getPrefix(file) + FileCons.TXT_OUTPUT_SUFFIX);
        // 删除旧输出文件
        if (!deleteOldOutputFile(outputFile)) {
            return false;
        }
        // 初始化自动章节编号
        autoChapterNumber = 1;
        // 初始化重新分段字符
        resegmentContent = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(
                file.toPath()); BufferedWriter writer = Files.newBufferedWriter(outputFile.toPath())) {
            // 记录导出配置
            OutputFormatProperty outputFormatProperty = OutputFormatProperty.getInstance();
            String indention = outputFormatProperty.getParagraphProperty().getIndentationProperty()
                    .generateIndentationSpace();
            // 格式化
            String line;
            while ((line = reader.readLine()) != null) {
                line = handleReplacement(line);
                AbstractParser parser = parseLine(line);
                if (parser instanceof ChapterParser chapterParser) {
                    formatChapter(writer, chapterParser, indention);
                    formatNewLines(writer);
                } else if (parser instanceof ContentParser contentParser) {
                    if (formatContent(writer, contentParser, indention)) {
                        formatNewLines(writer);
                    }
                }
            }
            return true;
        } catch (IOException e) {
            log.warn("Format txt failed.", e);
            throw e;
        }
    }

    private void formatNewLines(BufferedWriter writer) throws IOException {
        writer.newLine();
        int blankLineCount = OutputFormatProperty.getInstance().getParagraphProperty().getBlankLineCount();
        for (int i = 0; i < blankLineCount; i++) {
            writer.newLine();
        }
    }

    /**
     * 删除旧格式化文件。
     *
     * @param outputFile 格式化文件对象。
     *
     * @return 删除成功则返回 {@code true}，否则返回 {@code false}。
     */
    private boolean deleteOldOutputFile(File outputFile) {
        boolean deleteSuccess;
        try {
            deleteSuccess = FileUtil.delete(outputFile);
        } catch (Exception e) {
            deleteSuccess = false;
            log.debug("Delete old output file failed.", e);
        }
        if (!deleteSuccess) {
            DialogUtil.showMessage(I18nUtil.createStringBinding("Dialog.DeleteOutputFileFail.Title"),
                    I18nUtil.createStringBinding("Dialog.DeleteOutputFileFail.Desc", outputFile.getAbsolutePath()),
                    DialogUtil.Type.WARNING);
        }
        return deleteSuccess;
    }

    // TODO 应该允许用户自定义文本替换规则的顺序
    private String handleReplacement(String line) {
        OutputFormatProperty outputFormatProperty = OutputFormatProperty.getInstance();
        for (ReplacementProperty replacementProperty : outputFormatProperty.getReplacementPropertyList()) {
            if (replacementProperty.isRegexMode()) {
                line = line.replaceAll(replacementProperty.getOldText(), replacementProperty.getNewText());
            } else {
                line = line.replace(replacementProperty.getOldText(), replacementProperty.getNewText());
            }
        }
        return line;
    }

    private void formatChapter(Writer writer, ChapterParser parser, String indention) throws IOException {
        Objects.requireNonNull(parser);
        Chapter chapter = parser.getChapter();
        if (OutputFormatProperty.getInstance().isAutoNumberForChapter()) {
            chapter.setNumber(autoChapterNumber++);
        }
        if (OutputFormatProperty.getInstance().getParagraphProperty().getIndentationProperty()
                .isEffectiveForChapter()) {
            writer.write(indention);
        }
        writer.write(chapter.format(
                OutputFormatProperty.getInstance().getSelectedChapterTemplateObjectProperty().getValue()));
    }

    /**
     * 格式化正文内容。
     *
     * @param writer    输出流。
     * @param parser    正文解析器。
     * @param indention 正文前缩进空格字符串。
     *
     * @return 如果需要 {@link #formatNewLines(BufferedWriter)} 则返回 {@code true}，否则返回 {@code false}。
     *
     * @throws IOException 写入出现问题时抛出异常。
     */
    private boolean formatContent(Writer writer, ContentParser parser, String indention) throws IOException {
        Objects.requireNonNull(parser);
        String curContent = parser.getTrimmingText();
        // TODO 限制文本行拼接次数，避免奇怪的文档格式将整个文档都拼成一行。
        if (OutputFormatProperty.getInstance().getParagraphProperty().isResegment()) {
            if (!parser.isComplete()) {
                resegmentContent.append(curContent);
                return false;
            } else {
                curContent = resegmentContent.append(curContent).toString();
                // 清空
                resegmentContent.setLength(0);
            }
        }
        writer.write(indention);
        writer.write(curContent);
        return true;
    }

    @Nullable
    private AbstractParser parseLine(String line) {
        String trimmingLine = StrUtil.trim(line);
        BlankLineParser blankLineParser = new BlankLineParser(trimmingLine);
        if (blankLineParser.isSatisfied()) {
            return blankLineParser;
        }
        ChapterParser chapterParser = new ChapterParser(trimmingLine);
        if (chapterParser.isSatisfied()) {
            return chapterParser;
        }
        ContentParser contentParser = new ContentParser(trimmingLine);
        if (contentParser.isSatisfied()) {
            return contentParser;
        }
        return null;
    }

}

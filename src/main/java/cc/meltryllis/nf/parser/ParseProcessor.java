package cc.meltryllis.nf.parser;

import cc.meltryllis.nf.constants.FileCons;
import cc.meltryllis.nf.entity.Chapter;
import cc.meltryllis.nf.entity.Replacement;
import cc.meltryllis.nf.entity.config.OutputFormat;
import cc.meltryllis.nf.utils.FileUtil;
import cc.meltryllis.nf.utils.StrUtil;
import cc.meltryllis.nf.utils.message.DialogUtil;
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

    public static void format(File file) {
        if (file == null || !file.exists() || !file.isFile() || !FileCons.TXT_SUFFIX.equalsIgnoreCase(
                FileUtil.getSuffix(file))) {
            DialogUtil.show("Message.FileNotFound.Title", "Message.FileNotFound.Desc", DialogUtil.Type.WARNING);
            return;
        }
        File outputFile = new File(file.getParent(), FileUtil.getPrefix(file) + FileCons.TXT_OUTPUT_SUFFIX);
        if (!deleteOldOutputFile(outputFile)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(
                file.toPath()); BufferedWriter writer = Files.newBufferedWriter(outputFile.toPath())) {
            // 记录导出配置
            OutputFormat outputFormat = OutputFormat.getInstance();
            String indention = StrUtil.repeat(outputFormat.getSpace(), outputFormat.getSpaceCount());
            // 格式化
            String line;
            while ((line = reader.readLine()) != null) {
                line = handleReplacement(line);
                AbstractParser parser = ParseProcessor.parseLine(line);
                if (parser instanceof ChapterParser) {
                    formatChapter(writer, (ChapterParser) parser, indention);
                } else if (parser instanceof ContentParser) {
                    formatContent(writer, (ContentParser) parser, indention);
                }
                formatNewLines(writer);
            }
        } catch (IOException e) {
            log.warn("Format txt failed.", e);
        }
    }

    private static boolean deleteOldOutputFile(File outputFile) {
        boolean deleteSuccess;
        try {
            deleteSuccess = FileUtil.delete(outputFile);
        } catch (Exception e) {
            deleteSuccess = false;
            log.debug("Delete old output file failed.", e);
        }
        if (!deleteSuccess) {
            DialogUtil.show("Message.DeleteOutputFileFail.Title", "Message.DeleteOutputFileFail.Desc",
                    DialogUtil.Type.WARNING);
        }
        return deleteSuccess;
    }

    private static String handleReplacement(String line) {
        OutputFormat outputFormat = OutputFormat.getInstance();
        for (Replacement replacement : outputFormat.getReplacementList()) {
            if (replacement.isRegexMode()) {
                line = line.replaceAll(replacement.getOldText(), replacement.getNewText());
            } else {
                line = line.replace(replacement.getOldText(), replacement.getNewText());
            }
        }
        return line;
    }

    private static void formatChapter(Writer writer, ChapterParser parser, String indention) throws IOException {
        Objects.requireNonNull(parser);
        Chapter chapter = parser.getChapter();
        if (OutputFormat.getInstance().isIndentationForChapter()) {
            writer.write(indention);
        }
        writer.write(chapter.format(OutputFormat.getInstance().getSelectedChapterFormat()));
    }

    private static void formatContent(Writer writer, ContentParser parser, String indention) throws IOException {
        Objects.requireNonNull(parser);
        writer.write(indention);
        writer.write(parser.getTrimmingText());
    }

    private static void formatNewLines(BufferedWriter writer) throws IOException {
        writer.newLine();
        int blankLineCount = OutputFormat.getInstance().getBlankLineCount();
        for (int i = 0; i < blankLineCount; i++) {
            writer.newLine();
        }
    }

    @Nullable
    private static AbstractParser parseLine(String line) {
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

package cc.meltryllis.nf.parser;

import cc.meltryllis.nf.constants.FileCons;
import cc.meltryllis.nf.entity.Chapter;
import cc.meltryllis.nf.entity.Replacement;
import cc.meltryllis.nf.entity.config.OutputFormat;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;

/**
 * 小说整体流程解析器。
 *
 * @author Zachary W
 * @date 2025/2/5
 */
@Getter
@Setter(AccessLevel.PRIVATE)
public class ParseProcessor {

    /**
     * 解析失败
     */
    public static final int FAIL = -1;
    /**
     * 空白行
     */
    public static final int BLANK = 0;
    /**
     * 章节行
     */
    public static final int CHAPTER = 1;
    /**
     * 正文行
     */
    public static final int CONTENT = 2;

    private String trimmingText;
    private int type;
    @Nullable
    private AbstractParser satisfiedParser;

    public ParseProcessor(@NotNull String text) {
        setTrimmingText(StrUtil.trim(text));
    }

    public static void format(String filePath) throws FileNotFoundException {
        format(FileUtil.file(filePath));
    }

    public static void format(File file) throws FileNotFoundException {
        if (!file.exists() || !file.isFile() || !FileCons.TXT_SUFFIX.equalsIgnoreCase(FileUtil.getSuffix(file))) {
            throw new FileNotFoundException();
        }
        File outputFile = FileUtil.file(file.getParent(), FileUtil.getPrefix(file) + FileCons.TXT_OUTPUT_SUFFIX);
        FileUtil.del(outputFile);
        try (BufferedReader reader = Files.newBufferedReader(
                file.toPath()); BufferedWriter writer = Files.newBufferedWriter(outputFile.toPath())) {
            OutputFormat outputFormat = OutputFormat.getInstance();
            String indention = StrUtil.repeat(outputFormat.getSpace(), outputFormat.getSpaceCount());
            int blankLineCount = outputFormat.getBlankLineCount();
            boolean indentationForChapter = outputFormat.isIndentationForChapter();
            String line;
            while ((line = reader.readLine()) != null) {
                for (Replacement replacement : outputFormat.getReplacementList()) {
                    if (replacement.isRegexMode()) {
                        line = line.replaceAll(replacement.getOldText(), replacement.getNewText());
                    } else {
                        line = line.replace(replacement.getOldText(), replacement.getNewText());
                    }
                }
                ParseProcessor processor = new ParseProcessor(line);
                AbstractParser parser = processor.parse();
                int type = processor.getType();
                if (type == CHAPTER) {
                    ChapterParser chapterParser = (ChapterParser) parser;
                    assert chapterParser != null;
                    Chapter chapter = chapterParser.getChapter();
                    if (indentationForChapter) {
                        writer.write(indention);
                    }
                    writer.write(chapter.format(OutputFormat.getInstance().getSelectedChapterTemplate()));
                } else if (type == CONTENT) {
                    ContentParser contentParser = (ContentParser) parser;
                    assert contentParser != null;
                    writer.write(indention);
                    writer.write(contentParser.getTrimmingText());
                }
                writer.newLine();
                for (int i = 0; i < blankLineCount; i++) {
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    private AbstractParser parse() {
        String trimmingText = getTrimmingText();
        BlankLineParser blankLineParser = new BlankLineParser(trimmingText);
        if (blankLineParser.isSatisfied()) {
            setType(BLANK);
            setSatisfiedParser(blankLineParser);
            return blankLineParser;
        }
        ChapterParser chapterParser = new ChapterParser(trimmingText);
        if (chapterParser.isSatisfied()) {
            setType(CHAPTER);
            setSatisfiedParser(chapterParser);
            return chapterParser;
        }
        ContentParser contentParser = new ContentParser(trimmingText);
        if (contentParser.isSatisfied()) {
            setType(CONTENT);
            setSatisfiedParser(contentParser);
            return contentParser;
        }
        setType(FAIL);
        setSatisfiedParser(null);
        return null;
    }
}

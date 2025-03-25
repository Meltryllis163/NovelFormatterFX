package cc.meltryllis.nf.parse;

import cc.meltryllis.nf.constants.FileCons;
import cc.meltryllis.nf.entity.property.input.ChapterRegexProperty;
import cc.meltryllis.nf.entity.property.input.InputFormatProperty;
import cc.meltryllis.nf.entity.property.input.RegexProperty;
import cc.meltryllis.nf.entity.property.output.OutputFormatProperty;
import cc.meltryllis.nf.entity.property.output.ReplacementProperty;
import cc.meltryllis.nf.utils.common.FileUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import cc.meltryllis.nf.utils.message.dialog.DialogUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * 小说格式化处理器。
 *
 * @author Zachary W
 * @date 2025/2/5
 */
@Slf4j
@Getter
public class FormatProcessor {


    private final File          file;
    private       File          outputFile;
    private       int           autoChapterNumber;
    private       StringBuilder resegmentContent;


    public FormatProcessor(File file) {
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
        if (!checkChapterRegexEnabled()) {
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
        // TODO 定义文档编码格式
        try (BufferedReader reader = Files.newBufferedReader(
                file.toPath()); BufferedWriter writer = Files.newBufferedWriter(outputFile.toPath())) {
            BlankLineFormatter blankLineFormatter = new BlankLineFormatter(writer);
            ChapterFormatter chapterFormatter = new ChapterFormatter(writer);
            ContentFormatter contentFormatter = new ContentFormatter(writer);
            // 格式化
            String line;
            while ((line = reader.readLine()) != null) {
                line = handleReplacement(line);
                if (chapterFormatter.tryFormat(line)) {
                    contentFormatter.nextChapter();
                } else if (!blankLineFormatter.tryFormat(line)) {
                    contentFormatter.tryFormat(line);
                }
                blankLineFormatter.writeNewLines();
            }
            return true;
        } catch (IOException e) {
            log.warn("Format txt failed.", e);
            throw e;
        }
    }

    /**
     * 确保当前有章节正则表达式在启用。
     */
    private boolean checkChapterRegexEnabled() {
        boolean hasChapterRegexEnabled = false;
        ChapterRegexProperty chapterRegexProperty = InputFormatProperty.getInstance().getChapterRegexProperty();
        for (RegexProperty regexProperty : chapterRegexProperty.getRegexList()) {
            if (regexProperty.isEnabled()) {
                hasChapterRegexEnabled = true;
                break;
            }
        }
        if (!hasChapterRegexEnabled) {
            return DialogUtil.showChoice(I18nUtil.createStringBinding("Dialog.CheckChapterRegexEnabled.Title"),
                    DialogUtil.Type.ACCENT, false,
                    I18nUtil.createStringBinding("Dialog.CheckChapterRegexEnabled.Desc"));
        }
        return true;
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
    // TODO 允许用户直接选取一些常用的符号来替换，例如「」“”之类的
    // TODO 将textfield换成combobox，而且设置为editable，这样可以将combobox的popup菜单作为历史记录使用，这一条和上一条TODO有重复，需要适当取舍
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

}

package cc.meltryllis.nf.formatter;

import cc.meltryllis.nf.constants.FileCons;
import cc.meltryllis.nf.entity.property.input.ChapterRegexProperty;
import cc.meltryllis.nf.entity.property.input.InputFormatProperty;
import cc.meltryllis.nf.entity.property.input.RegexProperty;
import cc.meltryllis.nf.entity.property.output.OutputFormatProperty;
import cc.meltryllis.nf.entity.property.output.ReplacementProperty;
import cc.meltryllis.nf.ui.MainApplication;
import cc.meltryllis.nf.ui.controls.Message;
import cc.meltryllis.nf.utils.common.FileUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import cc.meltryllis.nf.utils.message.dialog.DialogUtil;
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
public record FormatProcessor(File file) {

    /**
     * 格式化小说。
     *
     * @return 格式化完成则返回 {@code true}，否则返回 {@code false}。
     */
    public boolean format() throws IOException {
        // 判断是否为TXT文件
        if (!FileCons.TXT_SUFFIX.equalsIgnoreCase(FileUtil.getSuffix(file))) {
            DialogUtil.showMessage(MainApplication.OWNER, Message.Type.WARNING,
                    I18nUtil.createStringBinding("Dialog.FileNotSelected.Title"),
                    I18nUtil.createStringBinding("Dialog.FileNotSelected.Desc"));
            return false;
        }
        if (!checkChapterRegexEnabled()) {
            return false;
        }
        // 设定输出文件对象
        File outputFile = new File(file.getParent(), FileUtil.getPrefix(file) + FileCons.TXT_OUTPUT_SUFFIX);
        // 删除旧输出文件
        if (!deleteOldOutputFile(outputFile)) {
            return false;
        }
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(),
                InputFormatProperty.getInstance().getCharset()); BufferedWriter writer = Files.newBufferedWriter(
                outputFile.toPath())) {
            BlankLineFormatter blankLineFormatter = new BlankLineFormatter(writer);
            ChapterFormatter chapterFormatter = new ChapterFormatter(writer);
            ContentFormatter contentFormatter = new ContentFormatter(writer);
            // 格式化
            String line;
            while ((line = reader.readLine()) != null) {
                line = handleReplacement(line);
                if (chapterFormatter.parse(line)) {
                    contentFormatter.nextChapter();
                    chapterFormatter.format();
                    blankLineFormatter.writeNewLines();
                } else if (!blankLineFormatter.parse(line)) {
                    if (contentFormatter.parse(line) && contentFormatter.format()) {
                        blankLineFormatter.writeNewLines();
                    }
                }
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
            return DialogUtil.showChoice(MainApplication.OWNER, Message.Type.ACCENT,
                    I18nUtil.createStringBinding("Dialog.CheckChapterRegexEnabled.Title"), false,
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
            DialogUtil.showMessage(MainApplication.OWNER, Message.Type.WARNING,
                    I18nUtil.createStringBinding("Dialog.DeleteOutputFileFail.Title"),
                    I18nUtil.createStringBinding("Dialog.DeleteOutputFileFail.Desc", outputFile.getAbsolutePath()));
        }
        return deleteSuccess;
    }

    // TODO 允许编辑某条Replacement
    // TODO 增强正则表达式输入框显示
    // TODO unique list 添加重复元素时提示
    // TODO 忽略第一章之前的内容，不看作正文，一般认为是简介或者其他信息
    // TODO 重新使用hutool
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

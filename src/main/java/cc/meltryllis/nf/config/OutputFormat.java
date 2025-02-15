package cc.meltryllis.nf.config;

import lombok.*;

/**
 * 输出流格式总配置。
 *
 * @author Zachary W
 * @date 2025/2/13
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
public class OutputFormat implements IFormat {
    private String chapterTemplate;
    private int blankLineCount;
    private int spaceCount;
    private char space;
    private boolean indentationForChapter;

    private static OutputFormat instance = null;

    public static OutputFormat getInstance() {
        if (instance == null) {
            instance = FormatFactory.createDefaultOutputFormat();
        }
        return instance;
    }
}

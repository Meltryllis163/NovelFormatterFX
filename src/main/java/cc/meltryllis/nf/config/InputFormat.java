package cc.meltryllis.nf.config;

import cc.meltryllis.nf.constants.DataCons;
import cc.meltryllis.nf.utils.JSONUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;

/**
 * 输入流格式总配置。
 *
 * @author Zachary W
 * @date 2025/2/4
 */
@NoArgsConstructor
@Getter
@Setter
public final class InputFormat implements IFormat {

    private File file;

    private static InputFormat instance = null;

    private ChapterInputFormat chapterFormat;

    public InputFormat(ChapterInputFormat chapterFormat) {
        this.chapterFormat = chapterFormat;
    }

    public static InputFormat getInstance() {
        if (instance == null) {
            InputFormat importConfig = JSONUtil.parseObject(new File(DataCons.INPUT_FORMAT_CONFIG), InputFormat.class);
            instance = importConfig == null ? FormatFactory.createDefaultFormat(InputFormat.class) :
                    importConfig;
        }
        return instance;
    }

}

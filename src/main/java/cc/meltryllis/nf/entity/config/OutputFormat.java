package cc.meltryllis.nf.entity.config;

import cc.meltryllis.nf.constants.DataCons;
import cc.meltryllis.nf.entity.ChapterFormat;
import cc.meltryllis.nf.entity.Replacement;
import cc.meltryllis.nf.utils.JSONUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.File;
import java.util.List;

/**
 * 输出流格式总配置。
 *
 * @author Zachary W
 * @date 2025/2/13
 */
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Builder(access = AccessLevel.PROTECTED)
public final class OutputFormat {

    @JsonIgnore
    private static OutputFormat instance = null;
    private List<ChapterFormat> chapterFormatList;
    private List<Replacement> replacementList;
    @JsonIgnore
    private ChapterFormat selectedChapterFormat;
    @JsonIgnore
    @JsonProperty(defaultValue = "1")
    private int           blankLineCount;
    @JsonIgnore
    @JsonProperty(defaultValue = "2")
    private int           spaceCount;
    @JsonIgnore
    @JsonProperty(defaultValue = "\u3000")
    private char          space;
    @JsonIgnore
    @JsonProperty(defaultValue = "false")
    private boolean       indentationForChapter;

    public static OutputFormat getInstance() {
        if (instance == null) {
            OutputFormat outputFormat = JSONUtil.parseObject(new File(DataCons.OUTPUT_FORMAT_CONFIG),
                    OutputFormat.class);
            instance = outputFormat == null ? FormatFactory.createDefaultOutputFormat() : outputFormat;
        }
        return instance;
    }

    public static void store() {
        JSONUtil.storeFile(new File(DataCons.OUTPUT_FORMAT_CONFIG), getInstance());
    }
}

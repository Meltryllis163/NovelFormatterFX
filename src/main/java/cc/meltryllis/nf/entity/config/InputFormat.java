package cc.meltryllis.nf.entity.config;

import cc.meltryllis.nf.constants.DataCons;
import cc.meltryllis.nf.entity.Regex;
import cc.meltryllis.nf.utils.JSONUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.File;
import java.util.List;

/**
 * 输入流格式总配置。
 *
 * @author Zachary W
 * @date 2025/2/4
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(access = AccessLevel.PACKAGE)
public final class InputFormat {

    private static InputFormat instance = null;
    @JsonIgnore
    private File file;
    @JsonProperty(defaultValue = "20")
    private int chapterMaxLength;
    @JsonProperty(defaultValue = "[]")
    private List<Regex> chapterRegexList;

    public static InputFormat getInstance() {
        if (instance == null) {
            InputFormat importConfig = JSONUtil.parseObject(new File(DataCons.INPUT_FORMAT_CONFIG), InputFormat.class);
            instance = importConfig == null ? FormatFactory.createDefaultInputFormat() :
                    importConfig;
        }
        return instance;
    }

    public static void store() {
        JSONUtil.storeFile(new File(DataCons.INPUT_FORMAT_CONFIG), InputFormat.getInstance());
    }

}

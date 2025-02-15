package cc.meltryllis.nf.config;

import cc.meltryllis.nf.entity.Regex;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 章节行输入流格式配置。
 *
 * @author Zachary W
 * @date 2025/2/7
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChapterInputFormat implements IFormat {

    /** 最大限制长度 */
    @JsonIgnore
    private int maxLimitLength;

    /** 章节名正则表达式 */
    protected List<Regex> regexList;

    public ChapterInputFormat(List<Regex> regexList, int maxLimitLength) {
        this.regexList = regexList;
        this.maxLimitLength = maxLimitLength;
    }

}

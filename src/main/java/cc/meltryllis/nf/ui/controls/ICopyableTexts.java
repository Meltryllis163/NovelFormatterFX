package cc.meltryllis.nf.ui.controls;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 可复制文本合集。
 * 用于获取类中可以复制到剪贴板的 {@code String} 文本集合。
 *
 * @author Zachary W
 * @date 2025/3/5
 */
public interface ICopyableTexts {

    @JsonIgnore
    String[] getCopyableTexts();

}

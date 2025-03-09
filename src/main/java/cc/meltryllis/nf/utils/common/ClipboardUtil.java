package cc.meltryllis.nf.utils.common;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

/**
 * JavaFX剪贴板工具。
 *
 * @author Zachary W
 * @date 2025/3/4
 */
public class ClipboardUtil {

    public static Clipboard getClipboard() {
        return Clipboard.getSystemClipboard();
    }

    public static boolean set(String str) {
        if (StrUtil.isEmpty(str)) {
            return false;
        }
        ClipboardContent content = new ClipboardContent();
        content.putString(str);
        return getClipboard().setContent(content);
    }
}

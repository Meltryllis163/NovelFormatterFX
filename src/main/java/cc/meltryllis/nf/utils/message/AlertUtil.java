package cc.meltryllis.nf.utils.message;

import cc.meltryllis.nf.utils.common.StrUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.scene.control.Alert;
import javafx.stage.Window;

/**
 * {@link javafx.scene.control.Alert} 通知工具。
 *
 * @author Zachary W
 * @date 2025/3/1
 */
public class AlertUtil {

    public static Window owner;

    public static void registerOwner(Window owner) {
        AlertUtil.owner = owner;
    }

    public static void show(Alert.AlertType type, String titleI18nKey, String contentI18nKey) {
        Alert alert = new Alert(type);
        if (!StrUtil.isEmpty(titleI18nKey)) {
            alert.titleProperty().bind(I18nUtil.createStringBinding(titleI18nKey));
        }
        alert.setHeaderText(null);
        alert.contentTextProperty().bind(I18nUtil.createStringBinding(contentI18nKey));
        alert.initOwner(owner);
        alert.show();
    }


}

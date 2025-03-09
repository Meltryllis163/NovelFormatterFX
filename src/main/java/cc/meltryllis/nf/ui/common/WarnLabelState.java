package cc.meltryllis.nf.ui.common;

import atlantafx.base.theme.Styles;
import cc.meltryllis.nf.utils.FXUtil;

/**
 * 代表「警告」的 {@code AbstractLabelState}。
 *
 * @author Zachary W
 * @date 2025/3/10
 */
public class WarnLabelState extends AbstractLabelState {

    @Override
    protected void handle(StateLabel label) {
        label.setVisible(true);
        label.setGraphic(FXUtil.createFontIcon("fth-alert-circle"));
        FXUtil.removeAllStyleClass(label, Styles.SUCCESS);
        FXUtil.addStyleClass(label, Styles.WARNING);
    }
}

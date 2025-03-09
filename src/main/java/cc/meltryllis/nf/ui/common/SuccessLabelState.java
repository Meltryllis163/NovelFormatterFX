package cc.meltryllis.nf.ui.common;

import atlantafx.base.theme.Styles;
import cc.meltryllis.nf.utils.FXUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 代表「成功」的 {@code AbstractLabelState}。
 *
 * @author Zachary W
 * @date 2025/3/10
 */
@Slf4j
public class SuccessLabelState extends AbstractLabelState {

    @Override
    protected void handle(StateLabel label) {
        label.setVisible(true);
        label.setGraphic(FXUtil.createFontIcon("fth-check-circle"));
        FXUtil.removeAllStyleClass(label, Styles.WARNING);
        FXUtil.addStyleClass(label, Styles.SUCCESS);
    }
}

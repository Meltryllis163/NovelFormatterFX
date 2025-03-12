package cc.meltryllis.nf.ui.common;

/**
 * 代表「空」的 {@code AbstractLabelState}。
 *
 * @author Zachary W
 * @date 2025/3/10
 */
public class EmptyLabelState extends AbstractLabelState {

    @Override
    protected void handle(StateLabel label) {
        label.setVisible(false);
    }
}

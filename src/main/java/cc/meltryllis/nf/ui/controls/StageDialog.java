package cc.meltryllis.nf.ui.controls;

import atlantafx.base.theme.Styles;
import cc.meltryllis.nf.constants.ColorCons;
import cc.meltryllis.nf.constants.UICons;
import cc.meltryllis.nf.ui.MainApplication;
import cc.meltryllis.nf.ui.controller.TitleBarController;
import cc.meltryllis.nf.ui.stage.CustomStage;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;


/**
 * 对话框形式的 {@link Stage}。
 * <p>
 * 该对话框的主体容器是 {@code container}，该容器有三个部分：
 * <ul>
 *     <li>消息类型图标：该图标由传入的 {@code dialogType} 决定， 如果 {@code dialogType} 的值是 {@code DialogType.NONE}，则该部分不存在。</li>
 *     <li>用户自定义内容面板，该部分不可能为空。</li>
 *     <li>按钮面板:用于显示确认以及取消按钮。</li>
 * </ul>
 *
 * @author Zachary W
 * @date 2025/3/9
 */
@Slf4j
public class StageDialog extends CustomStage {


    private final StageDialogControl control;
    private final VBox               container;

    protected StageDialog(StageDialogControl control) {
        super();
        this.control = control;

        // container
        container = new VBox();
        container.setAlignment(Pos.CENTER_LEFT);
        container.setBackground(Background.fill(ColorCons.DIALOG_BACKGROUND));
        container.setPadding(UICons.DIALOG_INSETS);
        container.getStylesheets().add(MainApplication.INDEX_CSS);
        container.getStyleClass().add("container");

        // addAll
        container.getChildren().addAll(control.getMessage());

        // initialize
        initialize(container);
        initTitleBar();
        // stage
        initOwner(control.getOwner());
        setResizable(control.isResizable());
        initModality(control.getModality());
        setMinWidth(control.getMinWidth());
        setTitle(control.titleProperty());

        getScene().getRoot().getStyleClass().add("stage-dialog");
    }

    public static StageDialogBuilder builder(@NotNull Window owner, @NotNull Node content) {
        return new StageDialogBuilder(owner, content);
    }

    private void initTitleBar() {
        TitleBarController titleBarController = getTitleBarController();
        titleBarController.getLogo().setText(null);
        titleBarController.getLogoSlot().setManaged(false);
    }

    public void setTitle(StringProperty titleProperty) {
        getTitleBarController().getTitle().textProperty().bind(titleProperty);
    }

    public static class StageDialogBuilder {

        private final StageDialogControl control;

        public StageDialogBuilder(@NotNull Window owner, @NotNull Node content) {
            control = new StageDialogControl(owner, content);
        }

        public StageDialogBuilder resizable(boolean resizable) {
            control.setResizable(resizable);
            return this;
        }

        public StageDialogBuilder iconified(boolean iconified) {
            control.setIconified(iconified);
            return this;
        }

        public StageDialogBuilder modality(Modality modality) {
            control.setModality(modality);
            return this;
        }

        public StageDialogBuilder minWidth(int minWidth) {
            control.setMinWidth(minWidth);
            return this;
        }

        public StageDialogBuilder title(String title) {
            control.setTitle(title);
            return this;
        }

        public StageDialogBuilder title(ObservableValue<String> property) {
            control.titleProperty().bind(property);
            return this;
        }

        public StageDialogBuilder type(Message.Type type) {
            control.getMessage().setType(type);
            return this;
        }

        public StageDialogBuilder okButton(boolean add) {
            control.getMessage().setOKButton(add ? createDefaultOKButton() : null);
            return this;
        }

        public StageDialogBuilder cancelButton(boolean add) {
            control.getMessage().setCancelButton(add ? createDefaultCancelButton() : null);
            return this;
        }

        private Button createDefaultOKButton() {
            Button ok = new Button();
            ok.textProperty().bind(I18nUtil.createStringBinding("Common.OK"));
            ok.getStyleClass().add(Styles.SUCCESS);
            return ok;
        }

        private Button createDefaultCancelButton() {
            Button cancel = new Button();
            cancel.textProperty().bind(I18nUtil.createStringBinding("Common.Cancel"));
            return cancel;
        }

        public StageDialog build() {
            return new StageDialog(control);
        }

    }

}

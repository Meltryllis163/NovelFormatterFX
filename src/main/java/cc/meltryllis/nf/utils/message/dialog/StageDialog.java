package cc.meltryllis.nf.utils.message.dialog;

import atlantafx.base.theme.Styles;
import cc.meltryllis.nf.constants.UICons;
import cc.meltryllis.nf.ui.MainApplication;
import cc.meltryllis.nf.utils.FXUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kordamp.ikonli.javafx.FontIcon;


/**
 * 对话框形式的 {@link Stage}。
 * <p>
 * 该对话框的主体容器是 {@code container}，该容器有两个部分：
 * <ul>
 *     <li>消息类型图标：该图标由传入的 {@code type} 决定， 如果 {@code type} 的值是 {@code Type.NONE}，则该部分不存在。</li>
 *     <li>用户自定义内容面板，该部分不可能为空。</li>
 * </ul>
 *
 * @author Zachary W
 * @date 2025/3/9
 */
@Getter(AccessLevel.PRIVATE)
public class StageDialog extends Stage {

    private VBox   container;
    private HBox   contentBox;
    private HBox   buttonBox;
    @Nullable
    @Getter(AccessLevel.PUBLIC)
    private Button okButton;
    @Nullable
    @Getter(AccessLevel.PUBLIC)
    private Button cancelButton;

    protected StageDialog(StageDialogBuilder builder) {
        this(builder.contentPane, builder.type, builder.resizable, builder.iconified, builder.modality,
                builder.minWidth, builder.icon, builder.title, builder.okButton, builder.cancelButton);
        container.getStylesheets().add(MainApplication.CUSTOM_CSS);
    }

    protected StageDialog(@NotNull Pane contentPane, @NotNull DialogUtil.Type type, boolean resizable,
                          boolean iconified, Modality modality, int minWidth, Image icon, StringBinding title,
                          boolean okButton, boolean cancelButton) {
        initOwner(DialogUtil.owner);
        initContainer();
        setOKButton(okButton);
        setCancelButton(cancelButton);
        setContent(type, contentPane);
        setResizable(resizable);
        setIconified(iconified);
        initModality(modality);
        setMinWidth(minWidth);
        setIcon(icon);
        setTitle(title);
    }

    public static StageDialogBuilder builder(@NotNull Pane contentPane) {
        return new StageDialogBuilder(contentPane);
    }

    private void initContainer() {

        contentBox = new HBox(UICons.LARGE_SPACING);
        contentBox.setAlignment(Pos.CENTER_LEFT);

        buttonBox = new HBox(UICons.SMALL_SPACING);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        container = new VBox(UICons.DEFAULT_SPACING);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(UICons.DIALOG_INSETS);
        container.getChildren().addAll(contentBox, buttonBox);

        Scene scene = new Scene(container);
        setScene(scene);
    }

    private void setOKButton(boolean add) {
        if (add) {
            okButton = new Button();
            okButton.getStyleClass().add(Styles.SUCCESS);
            okButton.textProperty().bind(I18nUtil.createStringBinding("Common.OK"));
            okButton.setOnAction(event -> close());
            buttonBox.getChildren().addAll(okButton);
        }
    }

    public void setOnOKAction(EventHandler<ActionEvent> handler) {
        if (okButton != null) {
            okButton.setOnAction(handler);
        }
    }

    private void setCancelButton(boolean add) {
        if (add) {
            cancelButton = new Button();
            cancelButton.textProperty().bind(I18nUtil.createStringBinding("Common.Cancel"));
            cancelButton.setOnAction(event -> close());
            buttonBox.getChildren().add(cancelButton);
        }
    }

    public void setOnCancelAction(EventHandler<ActionEvent> handler) {
        if (cancelButton != null) {
            cancelButton.setOnAction(handler);
        }
    }

    public void setTitle(@Nullable StringBinding title) {
        if (title != null) {
            titleProperty().bind(title);
        }
    }

    public void setContent(@NotNull DialogUtil.Type type, @NotNull Pane contentPane) {
        FontIcon icon = createTypeIcon(type);
        getContentBox().getChildren().clear();
        if (icon != null) {
            getContentBox().getChildren().add(icon);
        }
        HBox.setHgrow(contentPane, Priority.ALWAYS);
        getContentBox().getChildren().add(contentPane);
    }

    @Nullable
    private FontIcon createTypeIcon(@NotNull DialogUtil.Type type) {
        FontIcon icon = switch (type) {
            case ACCENT -> new FontIcon("fth-help-circle:50:#89CFF0");
            case WARNING, DANGER -> new FontIcon("fth-alert-circle:50:orange");
            case SUCCESS -> new FontIcon("fth-check-circle:50:green");
            case NONE -> null;
        };
        // 清除默认风格，让尺寸和颜色生效
        if (icon != null) {
            icon.getStyleClass().clear();
        }
        return icon;
    }

    public void setIcon(Image icon) {
        getIcons().clear();
        getIcons().add(icon);
    }

    public static class StageDialogBuilder {

        private final Pane contentPane;

        private boolean         resizable    = false;
        private boolean         iconified    = false;
        private boolean         okButton     = true;
        private boolean         cancelButton = true;
        private Modality        modality     = Modality.APPLICATION_MODAL;
        private int             minWidth     = UICons.DIALOG_MIN_WIDTH;
        private Image           icon         = FXUtil.newImage("/icons/icon.png");
        private DialogUtil.Type type         = DialogUtil.Type.NONE;

        private StringBinding title;

        public StageDialogBuilder(@NotNull Pane contentPane) {
            this.contentPane = contentPane;
        }

        public StageDialogBuilder setResizable(boolean resizable) {
            this.resizable = resizable;
            return this;
        }

        public StageDialogBuilder setIconified(boolean iconified) {
            this.iconified = iconified;
            return this;
        }

        public StageDialogBuilder setModality(Modality modality) {
            this.modality = modality;
            return this;
        }

        public StageDialogBuilder setMinWidth(int minWidth) {
            this.minWidth = minWidth;
            return this;
        }

        public StageDialogBuilder setType(@NotNull DialogUtil.Type type) {
            this.type = type;
            return this;
        }

        public StageDialogBuilder setIcon(Image icon) {
            this.icon = icon;
            return this;
        }

        public StageDialogBuilder setTitle(StringBinding title) {
            this.title = title;
            return this;
        }

        public StageDialogBuilder setOKButton(boolean okButton) {
            this.okButton = okButton;
            return this;
        }

        public StageDialogBuilder setCancelButton(boolean cancelButton) {
            this.cancelButton = cancelButton;
            return this;
        }

        public StageDialog build() {
            return new StageDialog(this);
        }

    }

}

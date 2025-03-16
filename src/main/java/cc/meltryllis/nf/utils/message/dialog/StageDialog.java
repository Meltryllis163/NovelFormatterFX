package cc.meltryllis.nf.utils.message.dialog;

import cc.meltryllis.nf.constants.UICons;
import cc.meltryllis.nf.ui.MainApplication;
import cc.meltryllis.nf.utils.FXUtil;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
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

    private HBox container;

    protected StageDialog(StageDialogBuilder builder) {
        this(builder.contentPane, builder.type, builder.resizable, builder.iconified, builder.modality,
                builder.minWidth, builder.icon, builder.title);
        container.getStylesheets().add(MainApplication.CUSTOM_CSS);
    }

    protected StageDialog(@NotNull Pane contentPane, @NotNull DialogUtil.Type type, boolean resizable,
                          boolean iconified, Modality modality, int minWidth, Image icon, StringBinding title) {
        initOwner(DialogUtil.owner);
        initContainer();
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
        this.container = new HBox(UICons.LARGE_SPACING);
        container.setPadding(UICons.DIALOG_INSETS);
        container.setAlignment(Pos.CENTER_LEFT);
        setScene(new Scene(container));
    }

    public void setTitle(@Nullable StringBinding title) {
        if (title != null) {
            titleProperty().bind(title);
        }
    }

    public void setContent(@NotNull DialogUtil.Type type, @NotNull Pane contentPane) {
        FontIcon icon = createTypeIcon(type);
        getContainer().getChildren().clear();
        if (icon != null) {
            getContainer().getChildren().add(icon);
        }
        HBox.setHgrow(contentPane, Priority.ALWAYS);
        getContainer().getChildren().add(contentPane);
    }

    @Nullable
    private FontIcon createTypeIcon(@NotNull DialogUtil.Type type) {
        FontIcon icon = switch (type) {
            case WARNING, DANGER -> new FontIcon("fth-alert-circle:40:orange");
            case SUCCESS -> new FontIcon("fth-check-circle:40:green");
            case NONE, ACCENT -> null;
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

        private boolean         resizable = false;
        private boolean         iconified = false;
        private Modality        modality  = Modality.APPLICATION_MODAL;
        private int             minWidth  = UICons.DIALOG_MIN_WIDTH;
        private Image           icon      = FXUtil.newImage("/icons/icon.png");
        private DialogUtil.Type type      = DialogUtil.Type.NONE;

        private StringBinding title;

        protected StageDialogBuilder(@NotNull Pane contentPane) {
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

        public StageDialog build() {
            return new StageDialog(this);
        }

    }

}

package cc.meltryllis.nf.ui.stage;

import cc.meltryllis.nf.constants.UICons;
import cc.meltryllis.nf.ui.MainApplication;
import cc.meltryllis.nf.ui.controller.TitleBarController;
import cc.meltryllis.nf.utils.FXUtil;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.StageStyle;
import lombok.Getter;
import xss.it.nfx.AbstractNfxUndecoratedWindow;
import xss.it.nfx.HitSpot;
import xss.it.nfx.WindowState;

import java.io.IOException;
import java.util.List;

/**
 * 自定义窗口。有可自由规划的标题栏。
 *
 * @author Zachary W
 * @date 2025/5/5
 */
public class CustomStage extends AbstractNfxUndecoratedWindow {

    /** 最小化 */
    public static final String MIN_SHAPE = "M1 7L1 8L14 8L14 7Z";

    /** 最大化 */
    public static final String MAX_SHAPE = "M2.5 2 A 0.50005 0.50005 0 0 0 2 2.5L2 13.5 A 0.50005 0.50005 0 0 0 2.5 14L13.5 14 A 0.50005 0.50005 0 0 0 14 13.5L14 2.5 A 0.50005 0.50005 0 0 0 13.5 2L2.5 2 z M 3 3L13 3L13 13L3 13L3 3 z";

    /** 恢复(从最大化恢复至正常窗口) */
    public static final String RESTORE_SHAPE = "M4.5 2 A 0.50005 0.50005 0 0 0 4 2.5L4 4L2.5 4 A 0.50005 0.50005 0 0 0 2 4.5L2 13.5 A 0.50005 0.50005 0 0 0 2.5 14L11.5 14 A 0.50005 0.50005 0 0 0 12 13.5L12 12L13.5 12 A 0.50005 0.50005 0 0 0 14 11.5L14 2.5 A 0.50005 0.50005 0 0 0 13.5 2L4.5 2 z M 5 3L13 3L13 11L12 11L12 4.5 A 0.50005 0.50005 0 0 0 11.5 4L5 4L5 3 z M 3 5L11 5L11 13L3 13L3 5 z";

    /** 关闭 */
    public static final String CLOSE_SHAPE = "M3.726563 3.023438L3.023438 3.726563L7.292969 8L3.023438 12.269531L3.726563 12.980469L8 8.707031L12.269531 12.980469L12.980469 12.269531L8.707031 8L12.980469 3.726563L12.269531 3.023438L8 7.292969Z";

    private HBox               titleBar;
    @Getter
    private TitleBarController titleBarController;

    public CustomStage() {
        super();
        initStyle(StageStyle.UNIFIED);
    }

    public CustomStage(Node content, double sceneWidth, double sceneHeight) {
        this();
        initialize(content, sceneWidth, sceneHeight);
    }

    public void initialize(Node content) {
        initialize(content, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
    }

    public void initialize(Node content, double sceneWidth, double sceneHeight) {
        initTitleBar();
        VBox root = new VBox(titleBar, content);
        VBox.setVgrow(content, Priority.ALWAYS);
        root.setAlignment(Pos.TOP_CENTER);
        root.getStylesheets().add(MainApplication.INDEX_CSS);
        setScene(new Scene(root, sceneWidth, sceneHeight));
        getIcons().add(FXUtil.newImage("/icons/logo-blue-128.png"));

        handelState(getWindowState());
        windowStateProperty().addListener((obs, o, state) -> handelState(state));
    }

    private void initTitleBar() {
        FXMLLoader loader = FXUtil.newFXMLLoader("/fxml/title-bar.fxml");
        try {
            titleBar = loader.load();
            titleBarController = loader.getController();
            titleBarController.setWindow(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 处理窗口状态变化时的图标。
     * <p>
     * 主要是最大化按钮的图标变化。
     *
     * @param state 当前窗口状态。
     */
    private void handelState(WindowState state) {
        Button maxBtn = titleBarController.getMaxBtn();
        if (maxBtn.getGraphic() instanceof SVGPath path) {
            if (state.equals(WindowState.MAXIMIZED)) {
                path.setContent(RESTORE_SHAPE);
            } else if (state.equals(WindowState.NORMAL)) {
                path.setContent(MAX_SHAPE);
            }
        }
    }

    @Override
    public List<HitSpot> getHitSpots() {
        return titleBarController.getHitSpots();
    }

    @Override
    public double getTitleBarHeight() {
        return titleBar == null ? UICons.TITLE_BAR_HEIGHT : titleBar.getHeight();
    }

}

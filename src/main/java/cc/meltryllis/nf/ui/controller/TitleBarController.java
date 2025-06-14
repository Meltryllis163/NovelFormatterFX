package cc.meltryllis.nf.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.Getter;
import xss.it.nfx.AbstractNfxUndecoratedWindow;
import xss.it.nfx.HitSpot;
import xss.it.nfx.WindowState;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


/**
 * 标题栏控制器。
 *
 * @author Zachary W
 * @date 2025/5/31
 */
@Getter
public class TitleBarController implements Initializable {

    @FXML
    private HBox   titleBar;
    @FXML
    private HBox   logoSlot;
    @FXML
    private Label  logo;
    @FXML
    private HBox   titleSlot;
    @FXML
    private Label  title;
    @FXML
    private Button minBtn, maxBtn, closeBtn;

    private AbstractNfxUndecoratedWindow window;

    public void setLogoSlotWidth(int width) {
        logoSlot.setMinWidth(width);
        logoSlot.setPrefWidth(width);
        logoSlot.setMaxWidth(width);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setWindow(AbstractNfxUndecoratedWindow window) {
        this.window = window;

        minBtn.setOnAction(event -> window.setWindowState(WindowState.MINIMIZED));
        minBtn.visibleProperty().bind(window.resizableProperty());
        minBtn.disableProperty().bind(window.resizableProperty().not());

        maxBtn.setOnAction(event -> {
            if (window.getWindowState().equals(WindowState.MAXIMIZED)) {
                window.setWindowState(WindowState.NORMAL);
            } else {
                window.setWindowState(WindowState.MAXIMIZED);
            }
        });
        maxBtn.visibleProperty().bind(window.resizableProperty());
        maxBtn.disableProperty().bind(window.resizableProperty().not());

        closeBtn.setOnAction(event -> window.close());
    }

    public List<HitSpot> getHitSpots() {
        HitSpot minimizeHitSpot = HitSpot.builder().window(window).control(minBtn).minimize(true).build();
        minimizeHitSpot.hoveredProperty().addListener((obs, o, hovered) -> {
            if (hovered) {
                minimizeHitSpot.getControl().getStyleClass().add("hit-hovered");
            } else {
                minimizeHitSpot.getControl().getStyleClass().removeAll("hit-hovered");
            }
        });

        HitSpot maximizeHitSpot = HitSpot.builder().window(window).control(maxBtn).build();
        maximizeHitSpot.hoveredProperty().addListener((obs, o, hovered) -> {
            if (hovered) {
                maximizeHitSpot.getControl().getStyleClass().add("hit-hovered");
            } else {
                maximizeHitSpot.getControl().getStyleClass().removeAll("hit-hovered");
            }
        });


        HitSpot closeHitSpot = HitSpot.builder().window(window).control(closeBtn).close(true).build();
        closeHitSpot.hoveredProperty().addListener((obs, o, hovered) -> {
            if (hovered) {
                closeHitSpot.getControl().getStyleClass().add("hit-close-btn");
                closeBtn.getGraphic().getStyleClass().add("shape-close-hovered");
            } else {
                closeHitSpot.getControl().getStyleClass().removeAll("hit-close-btn");
                closeBtn.getGraphic().getStyleClass().removeAll("shape-close-hovered");
            }
        });

        return List.of(minimizeHitSpot, maximizeHitSpot, closeHitSpot);
    }

}

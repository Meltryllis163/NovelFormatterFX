package cc.meltryllis.nf.ui.controls;

import cc.meltryllis.nf.ui.MainApplication;
import cc.meltryllis.nf.utils.FXUtil;
import javafx.animation.PauseTransition;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.robot.Robot;
import javafx.stage.Popup;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;


/**
 * 用 {@code Popup} 包装的提示组件。
 *
 * @author Zachary W
 * @date 2025/5/29
 */
public class PopupTip extends Popup {

    public static final String POPUP_TIP_PROPERTY_KEY = "POPUP_TIP";

    private final Node  node;
    private final Label msg = new Label();

    private final SimpleObjectProperty<Pos>      positionProperty  = new SimpleObjectProperty<>();
    private final IntegerProperty                xOffsetProperty   = new SimpleIntegerProperty();
    private final IntegerProperty                yOffsetProperty   = new SimpleIntegerProperty();
    private final SimpleObjectProperty<Duration> showDelayProperty = new SimpleObjectProperty<>();
    private final PauseTransition                showTransition    = new PauseTransition();
    private final SimpleObjectProperty<Duration> hideDelayProperty = new SimpleObjectProperty<>();
    private final PauseTransition                hideTransition    = new PauseTransition();

    private PopupTip(Builder builder) {
        this.node = builder.node;

        msg.getStylesheets().add(MainApplication.INDEX_CSS);
        msg.getStyleClass().add("pt-label");
        getContent().add(msg);

        load(builder);

        showTransition.durationProperty().bind(this.showDelayProperty);
        showTransition.setOnFinished(event -> ownerShow());

        hideTransition.durationProperty().bind(this.hideDelayProperty);
        hideTransition.setOnFinished(event -> hide());

        setOnShown(event -> {
            Point2D anchor = anchor();
            setX(anchor.getX());
            setY(anchor.getY());
        });
    }

    public static Builder builder(@NotNull Node node, @NotNull String text) {
        return new Builder(node, text);
    }

    public static Builder builder(@NotNull Node node, @NotNull ObservableValue<String> textObservableValue) {
        return new Builder(node, textObservableValue);
    }

    private void load(Builder builder) {
        this.positionProperty.setValue(builder.position);
        this.xOffsetProperty.setValue(builder.xOffset);
        this.yOffsetProperty.setValue(builder.yOffset);
        this.showDelayProperty.setValue(builder.showDelay);
        this.hideDelayProperty.setValue(builder.hideDelay);
        msg.textProperty().bind(builder.textProperty);
    }

    public ObjectProperty<Pos> positionProperty() {
        return this.positionProperty;
    }

    public Pos getPosition() {
        return positionProperty().getValue();
    }

    public void setPosition(Pos pos) {
        positionProperty().setValue(pos);
    }

    public IntegerProperty xOffsetProperty() {
        return this.xOffsetProperty;
    }

    public int getXOffset() {
        return xOffsetProperty().getValue();
    }

    public void setXOffset(int xOffset) {
        xOffsetProperty().setValue(xOffset);
    }

    public IntegerProperty yOffsetProperty() {
        return this.yOffsetProperty;
    }

    public int getYOffset() {
        return yOffsetProperty().getValue();
    }

    public void setYOffset(int yOffset) {
        yOffsetProperty().setValue(yOffset);
    }

    public void ownerShow() {
        show(node.getScene().getWindow());
        // 如果存在有效隐藏倒计时，则启动或重新启动此倒计时
        if (!hideDelayProperty.getValue().isIndefinite()) {
            hideTransition.playFromStart();
        }
    }

    public void install() {
        node.setOnMouseEntered(event -> showTransition.play());
        node.setOnMouseExited(event -> {
            showTransition.stop();
            hide();
        });
    }

    private Point2D anchor() {
        Bounds bounds = FXUtil.getBoundsInScreen(node);
        Pos position = getPosition();
        int xOffset = getXOffset();
        int yOffset = getYOffset();
        if (position == Pos.CENTER) {
            Robot robot = new Robot();
            return new Point2D(robot.getMouseX() + xOffset, robot.getMouseY() + yOffset);
        }
        double x = 0, y = 0;
        double popupWidth = getWidth();
        double popupHeight = getHeight();
        switch (position.getHpos()) {
            case LEFT -> x = bounds.getMinX() - popupWidth - xOffset;
            case CENTER -> x = bounds.getCenterX() - popupWidth / 2;
            case RIGHT -> x = bounds.getMaxX() + xOffset;
        }
        switch (position.getVpos()) {
            case TOP -> y = bounds.getMinY() - popupHeight - yOffset;
            case CENTER -> y = bounds.getCenterY() - popupHeight / 2;
            case BOTTOM -> y = bounds.getMaxY() + yOffset;
        }
        return new Point2D(x, y);
    }

    public StringProperty textProperty() {
        return msg.textProperty();
    }

    public String getText() {
        return msg.getText();
    }

    public void setText(String text) {
        msg.setText(text);
    }

    public static class Builder {

        private final StringProperty textProperty = new SimpleStringProperty();
        private final Node           node;

        private Pos      position;
        private int      xOffset   = 5;
        private int      yOffset   = 3;
        private Duration showDelay = Duration.ZERO;
        private Duration hideDelay = Duration.INDEFINITE;

        public Builder(@NotNull Node node, @NotNull String text) {
            this.node = node;
            this.textProperty.setValue(text);
        }

        public Builder(@NotNull Node node, @NotNull ObservableValue<String> textObservableValue) {
            this.node = node;
            this.textProperty.bind(textObservableValue);
        }

        public Builder position(Pos position) {
            this.position = position;
            return this;
        }

        public Builder xOffset(int xOffset) {
            this.xOffset = xOffset;
            return this;
        }

        public Builder yOffset(int yOffset) {
            this.yOffset = yOffset;
            return this;
        }

        public Builder showDelay(double millis) {
            this.showDelay = Duration.millis(millis);
            return this;
        }

        public Builder hideDelay(double millis) {
            this.hideDelay = Duration.millis(millis);
            return this;
        }

        public PopupTip build() {
            PopupTip pt;
            try {
                // 如果已经存在，则获取并加载新配置
                pt = (PopupTip) node.getProperties().get(POPUP_TIP_PROPERTY_KEY);
                pt.load(this);
            } catch (NullPointerException e) {
                // 如果不存在，则新构造一个对象
                pt = new PopupTip(this);
                node.getProperties().put(POPUP_TIP_PROPERTY_KEY, pt);
            }
            return pt;
        }

        public void install() {
            build().install();
        }

    }

}

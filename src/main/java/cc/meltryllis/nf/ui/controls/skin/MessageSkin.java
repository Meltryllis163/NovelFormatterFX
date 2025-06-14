package cc.meltryllis.nf.ui.controls.skin;

import cc.meltryllis.nf.constants.ColorCons;
import cc.meltryllis.nf.ui.MainApplication;
import cc.meltryllis.nf.ui.controls.Message;
import cc.meltryllis.nf.ui.controls.SlotListener;
import javafx.beans.value.ChangeListener;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * {@link cc.meltryllis.nf.ui.controls.Message} 的皮肤。
 *
 * @author Zachary W
 * @date 2025/6/9
 */
public class MessageSkin implements Skin<Message> {

    protected static final PseudoClass HAS_GRAPHIC = PseudoClass.getPseudoClass("has-graphic");

    private final Message control;

    private final BorderPane           root;
    private final HBox                 graphicSlot;
    private final ChangeListener<Node> graphicSlotListener;
    private final HBox                 okSlot;
    private final ChangeListener<Node> okSlotListener;
    private final HBox                 cancelSlot;
    private final ChangeListener<Node> cancelSlotListener;

    public MessageSkin(Message control) {
        this.control = control;
        // root
        root = new BorderPane();
        root.setBackground(Background.fill(ColorCons.DIALOG_BACKGROUND));
        root.getStylesheets().add(MainApplication.INDEX_CSS);
        root.getStyleClass().add("message");
        // graphic
        graphicSlot = new HBox();
        graphicSlot.setAlignment(Pos.CENTER);
        graphicSlot.getStyleClass().add("graphic");
        graphicSlotListener = new SlotListener(graphicSlot,
                (node, active) -> root.pseudoClassStateChanged(HAS_GRAPHIC, active));
        control.graphicProperty().addListener(graphicSlotListener);
        graphicSlotListener.changed(control.graphicProperty(), null, control.getGraphic());
        // ok
        okSlot = new HBox();
        okSlotListener = new SlotListener(okSlot);
        control.oKButtonProperty().addListener(okSlotListener);
        okSlotListener.changed(control.oKButtonProperty(), null, control.getOKButton());
        // cancel
        cancelSlot = new HBox();
        cancelSlotListener = new SlotListener(cancelSlot);
        control.cancelButtonProperty().addListener(cancelSlotListener);
        cancelSlotListener.changed(control.oKButtonProperty(), null, control.getCancelButton());
        // type
        ChangeListener<Message.Type> typeChangeListener = (observable, oldValue, newType) -> {
            switch (newType) {
                case ACCENT -> root.getStyleClass().add("accent");
                case SUCCESS -> root.getStyleClass().add("success");
                case WARNING -> root.getStyleClass().add("warning");
                case DANGER -> root.getStyleClass().add("danger");
                case NONE -> root.getStyleClass().add("none");
            }
            control.setGraphic(newType == Message.Type.NONE ? null : new FontIcon());
        };
        control.typeProperty().addListener(typeChangeListener);
        typeChangeListener.changed(control.typeProperty(), null, control.getType());
        // addAll
        root.setCenter(control.getContent());
        root.setBottom(new HBox(okSlot, cancelSlot));
    }

    @Override
    public Message getSkinnable() {
        return control;
    }

    @Override
    public Node getNode() {
        return root;
    }

    @Override
    public void dispose() {

    }

}

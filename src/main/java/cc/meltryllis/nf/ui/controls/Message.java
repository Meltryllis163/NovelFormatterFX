package cc.meltryllis.nf.ui.controls;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import org.jetbrains.annotations.NotNull;

/**
 * 消息面板控制器。
 *
 * @author Zachary W
 * @date 2025/6/9
 */
public class Message extends Control {

    private final ObjectProperty<Node> contentProperty;
    private final ObjectProperty<Node> graphicProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Button> oKButtonProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Button> cancelButtonProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Type> typeProperty = new SimpleObjectProperty<>(Type.NONE);

    public Message(@NotNull Node content) {
        this.contentProperty = new SimpleObjectProperty<>(content);
    }

    public ObjectProperty<Node> contentProperty() {
        return contentProperty;
    }

    public Node getContent() {
        return contentProperty().getValue();
    }

    public ObjectProperty<Node> graphicProperty() {
        return graphicProperty;
    }

    public Node getGraphic() {
        return graphicProperty().getValue();
    }

    public void setGraphic(Node graphic) {
        graphicProperty().setValue(graphic);
    }

    public ObjectProperty<Button> oKButtonProperty() {
        return oKButtonProperty;
    }

    public Button getOKButton() {
        return oKButtonProperty().getValue();
    }

    public void setOKButton(Button button) {
        // TODO 模仿DialogPane的ButtonType，通过添加不同的ButtonType来增加按钮。
        //  class enum ButtonType {
        //   String i18nKey;
        //   Button createButton();...
        //   }
        oKButtonProperty().setValue(button);
    }

    public ObjectProperty<Button> cancelButtonProperty() {
        return cancelButtonProperty;
    }

    public Button getCancelButton() {
        return cancelButtonProperty().getValue();
    }

    public void setCancelButton(Button button) {
        cancelButtonProperty().setValue(button);
    }

    public ObjectProperty<Type> typeProperty() {
        return typeProperty;
    }

    public Type getType() {
        return typeProperty().getValue();
    }

    public void setType(Type type) {
        typeProperty().setValue(type);
    }

    public enum Type {
        NONE,
        ACCENT,
        WARNING,
        DANGER,
        SUCCESS
    }

    // private final BooleanProperty resizableProperty = new SimpleBooleanProperty(true);
    //
    // public BooleanProperty resizableProperty() {
    //     return resizableProperty;
    // }
    //
    // public Boolean isResizable() {
    //     return resizableProperty().getValue();
    // }
    //
    // public void setResizable(boolean resizable) {
    //     resizableProperty().setValue(resizable);
    // }
    //
    // private final BooleanProperty iconifiedProperty = new SimpleBooleanProperty(true);
    //
    // public BooleanProperty iconifiedProperty() {
    //     return iconifiedProperty;
    // }
    //
    // public Boolean isIconified() {
    //     return iconifiedProperty().getValue();
    // }
    //
    // public void setIconified(boolean iconified) {
    //     iconifiedProperty().setValue(iconified);
    // }
    //
    // public ObjectProperty<Modality> modalityProperty = new SimpleObjectProperty<>(Modality.APPLICATION_MODAL);
    //
    // public ObjectProperty<Modality> modalityProperty() {
    //     return modalityProperty;
    // }
    //
    // public Modality getModality() {
    //     return modalityProperty().getValue();
    // }
    //
    // public void setModality(Modality modality) {
    //     modalityProperty().setValue(modality);
    // }
    //
    // private final StringProperty titleProperty = new SimpleStringProperty();
    //
    // public StringProperty titleProperty() {
    //     return titleProperty;
    // }
    //
    // public String setTitle() {
    //     return titleProperty().getValue();
    // }
    //
    // public void setTitle(String title) {
    //     titleProperty().setValue(title);
    // }

}

package cc.meltryllis.nf.ui.controls;

import cc.meltryllis.nf.constants.UICons;
import javafx.beans.property.*;
import javafx.scene.Node;
import javafx.stage.Modality;
import javafx.stage.Window;

/**
 * {@link StageDialog} 内容控制器。
 *
 * @author Zachary W
 * @date 2025/6/4
 */
public class StageDialogControl {

    private final ObjectProperty<Window> ownerProperty;
    private final ObjectProperty<Message> messageProperty;
    private final BooleanProperty resizableProperty = new SimpleBooleanProperty(true);
    private final BooleanProperty iconifiedProperty = new SimpleBooleanProperty(true);
    private final IntegerProperty minWidthProperty = new SimpleIntegerProperty(UICons.DIALOG_MIN_WIDTH);
    private final StringProperty titleProperty = new SimpleStringProperty();
    public ObjectProperty<Modality> modalityProperty = new SimpleObjectProperty<>(Modality.APPLICATION_MODAL);


    public StageDialogControl(Window owner, Node content) {
        this.ownerProperty = new SimpleObjectProperty<>(owner);
        this.messageProperty = new SimpleObjectProperty<>(new Message(content));
    }

    public ObjectProperty<Window> ownerProperty() {
        return ownerProperty;
    }

    public Window getOwner() {
        return ownerProperty().getValue();
    }

    public ObjectProperty<Message> messageProperty() {
        return messageProperty;
    }

    public Message getMessage() {
        return messageProperty().getValue();
    }

    public void setMessage(Message message) {
        messageProperty().setValue(message);
    }

    public BooleanProperty resizableProperty() {
        return resizableProperty;
    }

    public Boolean isResizable() {
        return resizableProperty().getValue();
    }

    public void setResizable(boolean resizable) {
        resizableProperty().setValue(resizable);
    }

    public BooleanProperty iconifiedProperty() {
        return iconifiedProperty;
    }

    public Boolean isIconified() {
        return iconifiedProperty().getValue();
    }

    public void setIconified(boolean iconified) {
        iconifiedProperty().setValue(iconified);
    }

    public ObjectProperty<Modality> modalityProperty() {
        return modalityProperty;
    }

    public Modality getModality() {
        return modalityProperty().getValue();
    }

    public void setModality(Modality modality) {
        modalityProperty().setValue(modality);
    }

    public IntegerProperty minWidthProperty() {
        return minWidthProperty;
    }

    public Integer getMinWidth() {
        return minWidthProperty().getValue();
    }

    public void setMinWidth(int minWidth) {
        minWidthProperty().setValue(minWidth);
    }

    public StringProperty titleProperty() {
        return titleProperty;
    }

    public String setTitle() {
        return titleProperty().getValue();
    }

    public void setTitle(String title) {
        titleProperty().setValue(title);
    }

}

package cc.meltryllis.nf.ui.controls;

import cc.meltryllis.nf.ui.controls.skin.FormFieldSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * 表单字段控制器。
 *
 * @author Zachary W
 * @date 2025/5/28
 */
public class FormField extends Control {


    private final StringProperty titleProperty = new SimpleStringProperty(this, "title");
    private final StringProperty descriptionProperty = new SimpleStringProperty(this, "description");
    private final ObjectProperty<Node> bodyProperty = new SimpleObjectProperty<>(this, "body");

    @Override
    protected Skin<?> createDefaultSkin() {
        return new FormFieldSkin(this);
    }

    public StringProperty titleProperty() {
        return titleProperty;
    }

    public String getTitle() {
        return titleProperty().get();
    }

    public void setTitle(String title) {
        titleProperty().set(title);
    }

    public StringProperty descriptionProperty() {
        return descriptionProperty;
    }

    public String getDescription() {
        return descriptionProperty().get();
    }

    public void setDescription(String description) {
        descriptionProperty().set(description);
    }

    public ObjectProperty<Node> bodyProperty() {
        return bodyProperty;
    }

    public Node getBody() {
        return bodyProperty().get();
    }

    public void setBody(Node body) {
        bodyProperty().set(body);
    }

}

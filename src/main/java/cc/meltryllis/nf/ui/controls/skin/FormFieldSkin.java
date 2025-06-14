package cc.meltryllis.nf.ui.controls.skin;

import cc.meltryllis.nf.ui.controls.FormField;
import cc.meltryllis.nf.ui.controls.PopupTip;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * {@link FormField} 的皮肤。
 *
 * @author Zachary W
 * @date 2025/5/28
 */
public class FormFieldSkin implements Skin<FormField> {

    private final FormField control;

    private final VBox  root;
    private final HBox  header;
    private final Label title;
    private final Label description;
    private final Node  body;

    public FormFieldSkin(FormField control) {
        this.control = control;
        this.root = new VBox();

        root.getStyleClass().add("form-field");

        title = new Label();
        title.getStyleClass().add("title");
        title.textProperty().bind(control.titleProperty());

        description = new Label(null, new FontIcon());
        description.getStyleClass().add("description");
        description.visibleProperty().bind(control.descriptionProperty().isNotEmpty());
        PopupTip.builder(description, control.descriptionProperty()).position(Pos.CENTER_RIGHT).install();

        header = new HBox();
        header.getStyleClass().add("header");
        header.getChildren().addAll(title, description);

        this.body = control.getBody();

        root.getChildren().addAll(header, control.getBody());

    }

    @Override
    public FormField getSkinnable() {
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

package cc.meltryllis.nf.ui.controller.output;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import cc.meltryllis.nf.config.FormatFactory;
import cc.meltryllis.nf.config.OutputFormat;
import cc.meltryllis.nf.constants.UICons;
import cc.meltryllis.nf.entity.Chapter;
import cc.meltryllis.nf.ui.controller.property.UniqueItemsProperty;
import cc.meltryllis.nf.utils.I18nUtil;
import cn.hutool.core.util.StrUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 章节格式导出控制器。
 *
 * @author Zachary W
 * @date 2025/2/16
 */
public class ChapterFormatController implements Initializable {
    @FXML
    public Card root;
    @FXML
    public Tile tile;
    @FXML
    public ComboBox<String> combobox;

    private UniqueItemsProperty<String> uniqueItemsProperty;
    private VBox itemListBox;

    private void initTile() {
        tile.titleProperty()
                .bind(I18nUtil.createStringBinding("App.NovelExportConfiguration.ChapterFormat.Tile.Title"));
        tile.descriptionProperty()
                .bind(I18nUtil.createStringBinding("App.NovelExportConfiguration.ChapterFormat.Tile.Desc"));
    }

    private void initComboBox() {
        List<String> chapterTemplateList = FormatFactory.createDefaultChapterTemplates();
        uniqueItemsProperty = new UniqueItemsProperty<>(chapterTemplateList);
        combobox.itemsProperty().bind(uniqueItemsProperty.getItems());
        combobox.setConverter(new StringConverter<>() {
            @Override
            public String toString(String template) {
                if (template == null) {
                    return null;
                }
                return Chapter.exampleFormat(template);
            }

            @Override
            public String fromString(String string) {
                return null;
            }
        });
        combobox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(-1)) {
                combobox.getSelectionModel().selectFirst();
            }
        });
        combobox.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> OutputFormat.getInstance()
                        .setChapterTemplate(newValue));
        combobox.getSelectionModel().selectFirst();
    }

    public void editList(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.titleProperty()
                .bind(I18nUtil.createStringBinding("App.NovelExportConfiguration.ChapterFormat.Tile.Title"));
        alert.setHeaderText(null);
        // alert图标会同步为owner的图标
        alert.initOwner(root.getScene().getWindow());
        alert.getDialogPane().setContent(createEditDialog());
        alert.getDialogPane().setPrefSize(600, 700);
        alert.getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = alert.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);
        alert.show();
    }

    private VBox createEditDialog() {

        VBox itemEditBox = new VBox();
        // itemEditBox.setPadding(UICons.DIALOG_INSETS);
        itemEditBox.setFillWidth(true);
        itemEditBox.setSpacing(UICons.BIG_SPACING);
        itemEditBox.setPadding(new Insets(20, 20, 20 ,20));

        Label header = new Label();
        header.textProperty()
                .bind(I18nUtil.createStringBinding("App.NovelExportConfiguration.ChapterFormat.EditDialog.Tile.Title"));
        header.getStyleClass().add(Styles.TITLE_2);
        Label subHeader = new Label();
        subHeader.textProperty()
                .bind(I18nUtil.createStringBinding("App.NovelExportConfiguration.ChapterFormat.EditDialog.Tile.Desc"));
        subHeader.getStyleClass().addAll(Styles.TEXT_SUBTLE);
        VBox.setVgrow(subHeader, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(createItemListBox());
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        itemEditBox.getChildren().addAll(header, subHeader, scrollPane, createItemAddBox());

        return itemEditBox;
    }

    private VBox createItemListBox() {
        itemListBox = new VBox(UICons.SMALL_SPACING);
        for (String template : uniqueItemsProperty.getItems().get()) {
            itemListBox.getChildren().add(createItemListLine(template));
        }
        return itemListBox;
    }

    private HBox createItemListLine(String template) {
        HBox itemBox = new HBox(UICons.SMALL_SPACING);
        TextField itemField = new TextField(Chapter.exampleFormat(template));
        itemField.setDisable(true);
        Button deleteButton = new Button(null, new FontIcon(Feather.TRASH_2));
        deleteButton.getStyleClass().add(Styles.DANGER);
        deleteButton.disableProperty().bind(uniqueItemsProperty.getSizeProperty().isEqualTo(1));
        deleteButton.setFocusTraversable(false);
        deleteButton.setOnAction(event -> {
            if (uniqueItemsProperty.remove(template)) {
                itemListBox.getChildren().remove(itemBox);
            }
        });
        HBox.setHgrow(itemField, Priority.ALWAYS);
        HBox.setHgrow(deleteButton, Priority.NEVER);
        itemBox.getChildren().addAll(itemField, deleteButton);
        return itemBox;
    }

    private HBox createItemAddBox() {
        HBox addBox = new HBox(UICons.SMALL_SPACING);
        TextField inputField = new TextField();
        Button addButton = new Button(null, new FontIcon(Feather.CHECK));
        addButton.getStyleClass().add(Styles.SUCCESS);
        HBox.setHgrow(inputField, Priority.ALWAYS);
        addBox.getChildren().addAll(inputField, addButton);
        addButton.setOnAction(event -> {
            String newTemplate = inputField.getText();
            if (!StrUtil.isEmpty(newTemplate) && uniqueItemsProperty.add(newTemplate)) {
                itemListBox.getChildren().add(createItemListLine(newTemplate));
            }
            inputField.clear();
            inputField.requestFocus();
        });
        return addBox;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTile();
        initComboBox();
    }
}

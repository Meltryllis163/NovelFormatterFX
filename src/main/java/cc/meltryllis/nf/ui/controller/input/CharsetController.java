package cc.meltryllis.nf.ui.controller.input;

import atlantafx.base.controls.Tile;
import cc.meltryllis.nf.entity.property.input.InputFormatProperty;
import cc.meltryllis.nf.utils.common.StrUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * @author Zachary W
 * @date 2025/3/26
 */
public class CharsetController implements Initializable {

    @FXML
    public Tile              charsetTile;
    @FXML
    public ComboBox<Charset> charsetComboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        charsetTile.titleProperty().bind(I18nUtil.createStringBinding("App.Formatter.Input.Charset.Tile.Title"));
        charsetTile.descriptionProperty().bind(I18nUtil.createStringBinding("App.Formatter.Input.Charset.Tile.Desc"));
        charsetComboBox.getItems().addAll(StandardCharsets.UTF_8, StandardCharsets.UTF_16, StandardCharsets.UTF_16LE,
                StandardCharsets.UTF_16BE, Charset.forName("GB18030"), Charset.forName("GBK"),
                Charset.forName("GB2312"));
        charsetComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Charset object) {
                if (object == null) {
                    return StrUtil.EMPTY;
                }
                return object.displayName();
            }

            @Override
            public Charset fromString(String string) {
                return null;
            }
        });
        charsetComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> InputFormatProperty.getInstance().setCharset(newValue == null ? StandardCharsets.UTF_8 : newValue));
        charsetComboBox.getSelectionModel().selectFirst();
    }

}

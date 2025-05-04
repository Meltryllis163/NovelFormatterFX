package cc.meltryllis.nf.ui.common;

import cc.meltryllis.nf.constants.CharacterCons;
import cc.meltryllis.nf.utils.common.StrUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 自定义文本输入框。
 *
 * @author Zachary W
 * @date 2025/3/28
 */
public class MTextField extends TextField {

    public MTextField() {
        this(StrUtil.EMPTY);
    }

    public MTextField(String text) {
        super(text);
        setContextMenu(createContextMenu());
    }

    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        // 插入
        Menu insertMenu = new Menu(null, FontIcon.of(Feather.MENU));
        insertMenu.textProperty().bind(I18nUtil.createStringBinding("Common.MTextField.ContextMenu.Insert"));
        List<String> insertStringList = createInsertStringList(CharacterCons.CHINESE.LEFT_CORNER_BRACKET,
                CharacterCons.CHINESE.RIGHT_CORNER_BRACKET, CharacterCons.CHINESE.LEFT_DOUBLE_QUOTE,
                CharacterCons.CHINESE.RIGHT_DOUBLE_QUOTE, CharacterCons.CHINESE.LEFT_WHITE_CORNER_BRACKET,
                CharacterCons.CHINESE.RIGHT_WHITE_CORNER_BRACKET);
        for (String insertString : insertStringList) {
            MenuItem insertStringItem = new MenuItem(insertString);
            insertStringItem.setOnAction(event -> replaceSelection(insertString));
            insertMenu.getItems().add(insertStringItem);
        }
        // 剪切
        ContextMenuItem cut = new ContextMenuItem(null, FontIcon.of(Feather.SCISSORS), this, TextInputControl::cut);
        cut.textProperty().bind(I18nUtil.createStringBinding("Common.MTextField.ContextMenu.Cut"));
        cut.disableProperty().bind(selectedTextProperty().isEmpty());
        // 复制
        ContextMenuItem copy = new ContextMenuItem(null, FontIcon.of(Feather.COPY), this, TextInputControl::copy);
        copy.textProperty().bind(I18nUtil.createStringBinding("Common.MTextField.ContextMenu.Copy"));
        copy.disableProperty().bind(selectedTextProperty().isEmpty());
        // 粘贴
        ContextMenuItem paste = new ContextMenuItem(null, FontIcon.of(Feather.CLIPBOARD), this,
                TextInputControl::paste);
        paste.textProperty().bind(I18nUtil.createStringBinding("Common.MTextField.ContextMenu.Paste"));
        // 全选
        ContextMenuItem selectAll = new ContextMenuItem(null, FontIcon.of(Feather.
                MAXIMIZE), this, TextInputControl::selectAll);
        selectAll.textProperty().bind(I18nUtil.createStringBinding("Common.MTextField.ContextMenu.SelectAll"));
        // 撤销
        ContextMenuItem undo = new ContextMenuItem(null, FontIcon.of(Feather.ROTATE_CCW), this, TextInputControl::undo);
        undo.textProperty().bind(I18nUtil.createStringBinding("Common.MTextField.ContextMenu.Undo"));
        undo.disableProperty().bind(undoableProperty().not());
        // 恢复
        ContextMenuItem redo = new ContextMenuItem(null, FontIcon.of(Feather.ROTATE_CW), this, TextInputControl::redo);
        redo.textProperty().bind(I18nUtil.createStringBinding("Common.MTextField.ContextMenu.Redo"));
        redo.disableProperty().bind(redoableProperty().not());

        contextMenu.getItems()
                .addAll(insertMenu, new SeparatorMenuItem(), cut, copy, paste, new SeparatorMenuItem(), selectAll,
                        new SeparatorMenuItem(), undo, redo);

        return contextMenu;
    }

    private List<String> createInsertStringList(Character... characters) {
        List<String> res = new ArrayList<>();
        for (Character chr : characters) {
            res.add(String.valueOf(chr));
        }
        return res;
    }

    private static class ContextMenuItem extends MenuItem {

        public ContextMenuItem(TextInputControl control, Consumer<TextInputControl> function) {
            this(null, null, control, function);
        }

        public ContextMenuItem(String text, TextInputControl control, Consumer<TextInputControl> function) {
            this(text, null, control, function);
        }

        public ContextMenuItem(String text, Node graphic, TextInputControl control,
                               Consumer<TextInputControl> function) {
            super(text, graphic);
            setOnAction(event -> function.accept(control));
        }

    }

}

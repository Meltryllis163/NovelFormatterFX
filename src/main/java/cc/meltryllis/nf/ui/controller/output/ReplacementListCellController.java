package cc.meltryllis.nf.ui.controller.output;

import cc.meltryllis.nf.constants.MyStyles;
import cc.meltryllis.nf.entity.Replacement;
import cc.meltryllis.nf.ui.common.FXMLListCellController;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * @author Zachary W
 * @date 2025/2/20
 */
public class ReplacementListCellController extends FXMLListCellController<Replacement> {

    public VBox root;
    public CheckBox itemSelectedCheckBox;
    public TextField targetField;
    public TextField replacementField;

    @Override
    protected void setListItem(Replacement item) {
        /*
        此处最好不使用bind，有以下几点原因：
        1.ListView的数据不会变化，唯一的可能是用户将其从ListView中删除重新添加，这同样不会产生任何显示内容的响应式变动。
        2.ListView存在一些对象复用的现象，比如当某个ListCell由于滚动等原因不再显示在当前视图中时，系统会将这个ListCell重新是用来渲染新的单元格

        更新：
        这个问题已经解决。详见cc.meltryllis.nf.ui.common.FXMLListCell.loadRoot()注释。
        但是上方的原因还是合理的，因此此处仍然要注意。
         */
        itemSelectedCheckBox.selectedProperty().bindBidirectional(item.getSelectedProperty());
        targetField.setText(item.getOldText());
        if (item.isRegexMode()) {
            targetField.getStyleClass().add(MyStyles.TEXT_FIELD_LIGHT_BLUE);
        }
        replacementField.setText(item.getNewText());
    }
}

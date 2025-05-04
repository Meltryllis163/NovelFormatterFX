package cc.meltryllis.nf.ui.common;

import cc.meltryllis.nf.utils.common.ArrayUtil;
import cc.meltryllis.nf.utils.common.ClipboardUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import cc.meltryllis.nf.utils.message.NotificationUtil;
import cc.meltryllis.nf.utils.message.dialog.DialogUtil;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义 {@link javafx.scene.control.TableView}。
 *
 * @author Zachary W
 * @date 2025/3/5
 */
@Slf4j
public class MTableView<S> extends TableView<S> {

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
    private final List<S> selections = new ArrayList<>();

    public MTableView() {
        super();
        initTableRow();
        // 属性
        setEditable(true);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        setMinHeight(400);
    }

    private void initTableRow() {
        setRowFactory(tableView -> crateTableViewRow());
    }

    private TableRow<S> crateTableViewRow() {
        final TableRow<S> row = new TableRow<>();
        initRowContextMenu(row);
        initRowDragSortEvent(row);
        return row;
    }

    /**
     * Copy from <a href="https://stackoverflow.com/a/52437193/22357486">StackOverFlow</a>。
     *
     * @param row 表格行对象。
     */
    private void initRowDragSortEvent(TableRow<S> row) {
        row.setOnDragDetected(event -> {
            if (!row.isEmpty()) {
                Integer index = row.getIndex();
                selections.clear();
                ObservableList<S> items = getSelectionModel().getSelectedItems();
                selections.addAll(items);
                Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                db.setDragView(row.snapshot(null, null));
                ClipboardContent cc = new ClipboardContent();
                cc.put(SERIALIZED_MIME_TYPE, index);
                db.setContent(cc);
                event.consume();
            }
        });

        row.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                if (row.getIndex() != (Integer) db.getContent(SERIALIZED_MIME_TYPE)) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    event.consume();
                }
            }
        });

        row.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();

            if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                int dropIndex;
                S dataItem = null;
                if (row.isEmpty()) {
                    dropIndex = getItems().size();
                } else {
                    dropIndex = row.getIndex();
                    dataItem = getItems().get(dropIndex);
                }
                int delta = 0;
                if (dataItem != null) {
                    while (selections.contains(dataItem)) {
                        delta = 1;
                        --dropIndex;
                        if (dropIndex < 0) {
                            dataItem = null;
                            dropIndex = 0;
                            break;
                        }
                        dataItem = getItems().get(dropIndex);
                    }
                }

                for (S selectedItem : selections) {
                    getItems().remove(selectedItem);
                }

                if (dataItem != null) {
                    dropIndex = getItems().indexOf(dataItem) + delta;
                } else if (dropIndex != 0) {
                    dropIndex = getItems().size();
                }


                getSelectionModel().clearSelection();

                for (S selectedItem : selections) {
                    // draggedIndex = selections.get(i);
                    getItems().add(dropIndex, selectedItem);
                    getSelectionModel().select(dropIndex);
                    dropIndex++;
                }

                event.setDropCompleted(true);
                selections.clear();
                event.consume();
            }
        });
    }

    private void initRowContextMenu(TableRow<S> row) {
        final ContextMenu rowMenu = new ContextMenu();
        // 复制、关闭
        row.itemProperty().addListener((observable, oldValue, newValue) -> {
            rowMenu.getItems().clear();
            if (newValue == null) {
                return;
            }
            if (newValue instanceof ICopyableTexts) {
                String[] copyableTexts = ((ICopyableTexts) newValue).getCopyableTexts();
                rowMenu.getItems().addAll(createCopyableTextItems(copyableTexts));
            }
            rowMenu.getItems().add(createCloseItem());
        });
        row.contextMenuProperty().bind(Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(rowMenu));
    }

    private MenuItem[] createCopyableTextItems(String[] texts) {
        if (ArrayUtil.isEmpty(texts)) {
            return null;
        }
        MenuItem[] items = new MenuItem[texts.length];
        for (int i = 0; i < texts.length; i++) {
            final String text = texts[i];
            MenuItem item = new MenuItem();
            item.textProperty().bind(I18nUtil.createStringBinding("Common.CopySomething", text));
            item.setOnAction(event -> {
                if (ClipboardUtil.set(text)) {
                    NotificationUtil.show(DialogUtil.Type.SUCCESS, "Dialog.CopySuccess", Pos.TOP_CENTER);
                }
            });
            items[i] = item;
        }
        return items;
    }

    private MenuItem createCloseItem() {
        MenuItem closeItem = new MenuItem();
        closeItem.textProperty().bind(I18nUtil.createStringBinding("Common.Close"));
        closeItem.setOnAction(event -> {
            if (closeItem.getParentMenu() != null) {
                closeItem.getParentMenu().hide();
            }
        });
        return closeItem;
    }

}

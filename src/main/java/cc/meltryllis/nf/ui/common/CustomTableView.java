package cc.meltryllis.nf.ui.common;

import cc.meltryllis.nf.utils.common.ArrayUtil;
import cc.meltryllis.nf.utils.common.ClipboardUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import cc.meltryllis.nf.utils.message.NotificationUtil;
import cc.meltryllis.nf.utils.message.dialog.DialogUtil;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.skin.NestedTableColumnHeader;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.control.skin.TableViewSkin;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义 {@link javafx.scene.control.TableView}。
 *
 * @author Zachary W
 * @date 2025/3/5
 */
@Slf4j
public class CustomTableView<S> extends TableView<S> {

    public CustomTableView() {
        super();
        setSkin(new FitWidthTableViewSkin<>(this));
        initCopyContextMenu();
        // 属性
        setEditable(true);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        itemsProperty().addListener(observable -> addFitColumnWidthListener());
    }

    /**
     * 当表格 {@code setItems()} 以后，会触发监听器，为新的 {@code items} 增加下方监听器来实时适配表格列宽度。
     * <p>
     * 但是存在这样一种特殊情况：
     * 新生成的表格 {@code items == null}，然后又 {@code setItems()} 了一个新的 {@code items}，而且元素数量也为0。
     * 这时候程序将不会触发 {@code itemsProperty} 的监听器，导致以下监听器注册不上。
     * 因此将该方法设置为 {@code public}，碰到这种特殊情况时允许手动调用。
     * <p>
     * 更新：该问题通过使用 {@code InvalidationListener} 而不是 {@code ChangeListener} 解决了，现在不需要再额外调用。
     * {@code InvalidationListener} 的调用范围更广，它会在发生set事件的时候调用，而不考虑值是不是真的发生了变化。
     */
    private void addFitColumnWidthListener() {
        getItems().addListener((ListChangeListener<S>) c -> {
            log.debug("ListChanged");
            if (getSkin() instanceof CustomTableView.FitWidthTableViewSkin<?> fitWidthTableViewSkin) {
                fitWidthTableViewSkin.resizeColumnsToFitContent();
            }
        });
    }

    private void initCopyContextMenu() {
        setRowFactory(tableView -> {
            final TableRow<S> row = new TableRow<>();
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
            row.contextMenuProperty()
                    .bind(Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(rowMenu));
            return row;
        });
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

    static class FitWidthTableViewSkin<S> extends TableViewSkin<S> {

        public FitWidthTableViewSkin(TableView<S> tableView) {
            super(tableView);
        }

        @Override
        protected TableHeaderRow createTableHeaderRow() {
            return new TableHeaderRow(this) {

                @Override
                protected NestedTableColumnHeader createRootHeader() {
                    return new NestedTableColumnHeader(null) {
                        @Override
                        protected TableColumnHeader createTableColumnHeader(TableColumnBase col) {
                            return new FitWidthTableColumnHeader(col);
                        }
                    };
                }
            };
        }

        public void resizeColumnsToFitContent() {
            for (TableColumnHeader columnHeader : getTableHeaderRow().getRootHeader().getColumnHeaders()) {
                if (columnHeader instanceof FitWidthTableColumnHeader colHead) {
                    colHead.resizeColumnToFitContent(-1);
                }
            }
        }

        static class FitWidthTableColumnHeader extends TableColumnHeader {

            public FitWidthTableColumnHeader(TableColumnBase col) {
                super(col);
            }

            @Override
            public void resizeColumnToFitContent(int rows) {
                super.resizeColumnToFitContent(-1);
            }

        }

    }

}

package cc.meltryllis.nf.ui.controller.dialog;

import cc.meltryllis.nf.utils.message.dialog.StageDialog;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * 用作 {@link cc.meltryllis.nf.utils.message.dialog.StageDialog} 内容的FXML文件的控制器。
 * <p>
 * {@code T} 代表对话框关闭后可从对话框返回的数据类型。
 *
 * @author Zachary W
 * @date 2025/3/10
 */
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
public abstract class StageDialogController<T> {

    private StageDialog stageDialog;

    /** 传入数据 */
    private T result;

    public T registerStageDialog(StageDialog stageDialog) {
        return registerStageDialog(null, stageDialog);
    }

    public T registerStageDialog(T initialResult, StageDialog stageDialog) {
        setStageDialog(stageDialog);
        setInitialResult(initialResult);
        stageDialog.showAndWait();
        return getResult();
    }

    /**
     * 如果想处理初始数值，请重写该方法。
     *
     * @param initialResult 初始化数值
     */
    protected void setInitialResult(T initialResult) {
        setResult(initialResult);
    }
}

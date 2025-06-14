package cc.meltryllis.nf.ui.controller.dialog;

import cc.meltryllis.nf.ui.controls.StageDialog;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 用作 {@link StageDialog} 内容的FXML文件的控制器。
 * <p>
 * {@code T} 代表对话框关闭后可从对话框返回的数据类型。
 *
 * @author Zachary W
 * @date 2025/3/10
 */
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
public abstract class AbstractStageDialogController<T> {

    private StageDialog stageDialog;

    /** 传入数据 */
    private T result;

    public T registerStageDialog(@NotNull StageDialog stageDialog) {
        return registerStageDialog(null, stageDialog);
    }

    public T registerStageDialog(@Nullable T initialResult, @NotNull StageDialog stageDialog) {
        setStageDialog(stageDialog);
        setInitialResult(initialResult);
        stageDialogRegistered();
        stageDialog.showAndWait();
        return getResult();
    }

    /**
     * 如果需要修改 {@code stageDialog}，请重写该方法。
     * <p>
     * 该方法运行时，{@link #getStageDialog()} 必定非空。
     *
     * @see #registerStageDialog(Object, StageDialog)
     */
    protected void stageDialogRegistered() {

    }

    /**
     * 如果想处理初始数值，请重写该方法。
     * 例如：
     * <pre>
     *     &#064;Override
     *     protected void setInitialResult(String initialResult) {
     *         super.setInitialResult(initialResult);
     *         // 处理初始数值代码
     *     }
     * </pre>
     *
     * @param initialResult 初始化数值
     *
     * @see #registerStageDialog(Object, StageDialog)
     */
    protected void setInitialResult(T initialResult) {
        setResult(initialResult);
    }

}

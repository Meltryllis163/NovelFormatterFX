package cc.meltryllis.nf;

import cc.meltryllis.nf.ui.MainApplication;

/**
 * 调用JavaFX真实主类的外包装，不然打包会有问题。
 * 折磨哥们一整天，头发又掉了几根。
 * 不包装可能会产生<i>缺少JavaFX运行时组件</i>的错误。
 * 具体原因至今没搞懂，只找到一个<a href="https://github.com/javafxports/openjdk-jfx/issues/236#issuecomment-426583174">相关的解释</a>。
 * 总之先凑合用。
 *
 * @author Zachary W
 * @date 2025/2/15
 */
public class Main {
    public static void main(String[] args) {
        MainApplication.main(args);
    }
}

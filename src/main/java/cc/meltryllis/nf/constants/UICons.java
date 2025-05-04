package cc.meltryllis.nf.constants;

import javafx.geometry.Insets;
import javafx.scene.paint.Color;

/**
 * 界面布局常量。
 *
 * @author Zachary W
 * @date 2025/2/11
 */
public interface UICons {

    int PREF_WIDTH       = 1200;
    int PREF_HEIGHT      = 800;
    int STAGE_MIN_WIDTH  = 300;
    int STAGE_MIN_HEIGHT = 300;
    int DIALOG_MIN_WIDTH = 450;

    int SMALL_SPACING   = 5;
    int DEFAULT_SPACING = 10;
    int BIG_SPACING     = 20;
    int LARGE_SPACING   = 30;

    Insets DIALOG_INSETS       = new Insets(15, 30, 15, 30);
    Insets DEFAULT_INSETS      = new Insets(15, 50, 15, 50);
    Insets NOTIFICATION_INSETS = new Insets(10, 10, 10, 10);

    Color DODER_BLUE = Color.valueOf("#1E90FF");

}

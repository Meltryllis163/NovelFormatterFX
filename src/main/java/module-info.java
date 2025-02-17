module cc.meltryllis.nf {
    requires javafx.fxml;

    requires atlantafx.base;
    requires cn.hutool.core;
    requires static lombok;
    requires com.fasterxml.jackson.databind;
    requires org.jetbrains.annotations;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.feather;

    opens cc.meltryllis.nf.ui.controller to javafx.fxml;
    opens cc.meltryllis.nf.ui.controller.output to javafx.fxml;
    opens cc.meltryllis.nf.constants to javafx.fxml;

    exports cc.meltryllis.nf.ui;
    exports cc.meltryllis.nf.ui.controller;
    exports cc.meltryllis.nf.entity;
    exports cc.meltryllis.nf.ui.controller.property;
    exports cc.meltryllis.nf.ui.controller.input;
}
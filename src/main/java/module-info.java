module cc.meltryllis.nf {
    requires javafx.fxml;
    requires javafx.controls;

    requires atlantafx.base;
    requires cn.hutool.core;
    requires static lombok;
    requires com.fasterxml.jackson.databind;
    requires org.jetbrains.annotations;

    opens cc.meltryllis.nf.ui.controller to javafx.fxml;
    opens cc.meltryllis.nf.constants to javafx.fxml;

    exports cc.meltryllis.nf.ui;
    exports cc.meltryllis.nf.ui.controller;
    exports cc.meltryllis.nf.ui.table;
    exports cc.meltryllis.nf.entity;
}
module cc.meltryllis.nf {
    requires javafx.fxml;
    requires javafx.controls;

    requires atlantafx.base;
    requires static lombok;
    requires com.fasterxml.jackson.databind;
    requires org.jetbrains.annotations;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.feather;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
    requires org.slf4j;
    requires java.desktop;

    opens cc.meltryllis.nf.ui.controller to javafx.fxml;
    opens cc.meltryllis.nf.ui.controller.input to javafx.fxml;
    opens cc.meltryllis.nf.ui.controller.output to javafx.fxml;
    opens cc.meltryllis.nf.ui.controller.settings to javafx.fxml;
    opens cc.meltryllis.nf.ui.controller.dialog to javafx.fxml;
    opens cc.meltryllis.nf.ui.common to javafx.fxml;
    opens cc.meltryllis.nf.constants to javafx.fxml;

    opens cc.meltryllis.nf.entity to com.fasterxml.jackson.databind;
    opens cc.meltryllis.nf.ui to com.fasterxml.jackson.databind;

    opens cc.meltryllis.nf.entity.property to com.fasterxml.jackson.databind, javafx.base;
    opens cc.meltryllis.nf.entity.property.input to com.fasterxml.jackson.databind, javafx.base;
    opens cc.meltryllis.nf.entity.property.output to com.fasterxml.jackson.databind, javafx.base;

    exports cc.meltryllis.nf.ui;
    exports cc.meltryllis.nf.ui.controller;

}
module org.example.billeteravirtual {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens org.example.billeteravirtual to javafx.fxml;
    opens org.example.billeteravirtual.ui to javafx.fxml;

    opens org.example.billeteravirtual.agentes to javafx.base;
    opens org.example.billeteravirtual.transacciones to javafx.base;
    exports org.example.billeteravirtual;
}
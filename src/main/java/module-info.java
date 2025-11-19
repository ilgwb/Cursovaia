module com.example.cursovaia3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.fasterxml.jackson.databind;
    requires jdk.compiler;
    requires jdk.jdi;
    requires javafx.graphics;

    opens com.example.cursovaia3 to javafx.fxml;
    exports com.example.cursovaia3;
}
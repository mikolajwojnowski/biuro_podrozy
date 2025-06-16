module org.example.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires java.net.http;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;

    // Open entire frontend package to Jackson
    opens org.example.frontend to javafx.fxml;
    opens org.example.frontend.controllers to javafx.fxml;
    opens org.example.frontend.models to com.fasterxml.jackson.databind;
    opens views to javafx.fxml;

    exports org.example.frontend;
    exports org.example.frontend.controllers;
    exports org.example.frontend.models;
    exports org.example.frontend.services;
    exports org.example.frontend.utils;
}
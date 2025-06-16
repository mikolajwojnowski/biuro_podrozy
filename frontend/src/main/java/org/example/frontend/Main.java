package org.example.frontend;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;



public class Main extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        try {
            this.primaryStage = primaryStage;
            SceneManager.setPrimaryStage(primaryStage);
            SceneManager.showLoginScene();
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Failed to start: " + e.getMessage());
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
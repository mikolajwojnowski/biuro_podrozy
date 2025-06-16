package org.example.frontend;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import org.example.frontend.utils.DialogUtils;

import java.io.IOException;
import java.net.URL;

public class SceneManager {
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    private static void loadScene(String fxmlPath, String title) {
        try {
            URL location = SceneManager.class.getResource(fxmlPath);
            if (location == null) {
                throw new IOException("FXML file not found: " + fxmlPath);
            }

            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(location);
                    Parent root = loader.load();
                    
                    // Get current scene dimensions if they exist
                    double width = primaryStage.getWidth();
                    double height = primaryStage.getHeight();
                    
                    // If no scene exists yet, use screen dimensions
                    if (width == 0 || height == 0) {
                        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
                        width = bounds.getWidth();
                        height = bounds.getHeight();
                    }
                    
                    // Create new scene with current dimensions
                    Scene scene = new Scene(root, width, height);
                    
                    // Set the scene and update title
                    primaryStage.setScene(scene);
                    primaryStage.setTitle(title);
                    
                    // Special handling for login/register screens
                    if (fxmlPath.contains("loginfx.fxml") || fxmlPath.contains("register.fxml")) {
                        // Center small dialogs
                        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
                        primaryStage.setWidth(800);
                        primaryStage.setHeight(465);
                        primaryStage.setX((bounds.getWidth() - 800) / 2);
                        primaryStage.setY((bounds.getHeight() - 465) / 2);
                    } else {
                        // Maximize main screens
                        primaryStage.setMaximized(true);
                    }
                    
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    DialogUtils.showErrorDialog("Error", "Failed to load scene: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.showErrorDialog("Error", "Failed to load scene: " + e.getMessage());
        }
    }

    public static void showLoginScene() {
        try {
            String fxmlPath = "/views/loginfx.fxml";
            if (fxmlPath.contains("loginfx.fxml") || fxmlPath.contains("register.fxml")) {
                loadScene(fxmlPath, "Login");
            } else {
                loadScene(fxmlPath, "Travel Agency System");
            }
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.showErrorDialog("Error", "Failed to load login scene: " + e.getMessage());
        }
    }

    public static void showRegisterScene() {
        loadScene("/views/register.fxml", "Travel System - Register");
    }

    public static void showMainScene() {
        loadScene("/views/MainScreen.fxml", "Travel System");
    }

    public static void showAdminScene() {
        loadScene("/views/admin.fxml", "Travel System - Admin");
    }
}
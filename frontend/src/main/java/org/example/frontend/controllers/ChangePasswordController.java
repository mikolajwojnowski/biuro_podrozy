package org.example.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.example.frontend.services.AuthService;
import org.example.frontend.AppContext;
import javafx.application.Platform;
import java.util.Timer;
import java.util.TimerTask;

public class ChangePasswordController {
    @FXML
    private PasswordField oldPasswordField;
    
    @FXML
    private PasswordField newPasswordField;
    
    @FXML
    private PasswordField confirmPasswordField;
    
    @FXML
    private Label errorLabel;
    
    private Stage dialogStage;
    private AuthService authService;
    private Runnable onPasswordChanged;
    
    public void initialize() {
        authService = new AuthService(AppContext.getApiClient());
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setOnPasswordChanged(Runnable callback) {
        this.onPasswordChanged = callback;
    }
    
    @FXML
    private void handleChangePassword() {
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showError("All fields are required");
            return;
        }
        
        if (newPassword.length() < 8) {
            showError("New password must be at least 8 characters long");
            return;
        }
        
        if (newPassword.equals(oldPassword)) {
            showError("New password must be different from the current password");
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            showError("New passwords do not match");
            return;
        }
        
        try {
            authService.changePassword(oldPassword, newPassword);
            showSuccess("Password changed! Logging out in 3s...");
            
            // Schedule logout after 3 seconds
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        if (onPasswordChanged != null) {
                            onPasswordChanged.run();
                        }
                        dialogStage.close();
                    });
                }
            }, 3000);
            
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }
    
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    
    private void showError(String message) {
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setWrapText(true);
    }
    
    private void showSuccess(String message) {
        errorLabel.setStyle("-fx-text-fill: green;");
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setWrapText(true);
    }
} 
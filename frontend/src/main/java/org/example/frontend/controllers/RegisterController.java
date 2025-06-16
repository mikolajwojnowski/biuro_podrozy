package org.example.frontend.controllers;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.frontend.AppContext;
import org.example.frontend.SceneManager;
import org.example.frontend.services.AuthService;
import org.example.frontend.utils.DialogUtils;
import org.example.frontend.models.AuthToken;

public class RegisterController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField passwordFieldConfirm;
    @FXML private Button registerButton;
    @FXML private Label errorLabel;
    
    private final BooleanProperty isRegistering = new SimpleBooleanProperty(false);
    private static final int MIN_PASSWORD_LENGTH = 8;
    private final AuthService authService;

    public RegisterController() {
        this.authService = new AuthService(AppContext.getApiClient());
    }

    @FXML
    private void initialize() {
        errorLabel.setVisible(false);
        errorLabel.getStyleClass().add("error-label");
        
        // Add tooltip to password field
        passwordField.setTooltip(new Tooltip("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long"));
        
        // Bind register button to form validation and registration state
        registerButton.disableProperty().bind(
            Bindings.or(
                Bindings.or(
                    emailField.textProperty().isEmpty(),
                    passwordField.textProperty().isEmpty()
                ),
                Bindings.or(
                    passwordFieldConfirm.textProperty().isEmpty(),
                    isRegistering
                )
            )
        );
    }

    @FXML
    private void handleRegister() {
        try {
            errorLabel.setVisible(false);
            isRegistering.set(true);
            
            String email = emailField.getText();
            String password = passwordField.getText();
            String confirmPassword = passwordFieldConfirm.getText();

            if (!password.equals(confirmPassword)) {
                showError("Passwords don't match");
                return;
            }

            if (password.length() < MIN_PASSWORD_LENGTH) {
                showError("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long");
                return;
            }

            AuthToken token = authService.register(email, password, confirmPassword);
            if (token != null) {
                DialogUtils.showInfoDialog("Success", "Registration successful! Please login with your new account.");
                SceneManager.showLoginScene();
            } else {
                showError("Registration failed - invalid response from server");
            }
            
        } catch (Exception e) {
            String errorMessage = extractUserFriendlyMessage(e.getMessage());
            showError(errorMessage);
        } finally {
            isRegistering.set(false);
        }
    }

    @FXML
    private void handleBackToLogin() {
        SceneManager.showLoginScene();
    }
    
    private void showError(String message) {
        Platform.runLater(() -> {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
            passwordField.clear();
            passwordFieldConfirm.clear();
        });
    }

    private String extractUserFriendlyMessage(String errorMessage) {
        if (errorMessage == null) {
            return "Registration failed. Please try again.";
        }
        
        if (errorMessage.contains("400")) {
            return "Invalid email or password format";
        }
        if (errorMessage.contains("409") || errorMessage.contains("Email already exists")) {
            return "Email already registered";
        }
        if (errorMessage.contains("403")) {
            return "Registration is currently not allowed";
        }
        if (errorMessage.contains("Connection refused")) {
            return "Cannot connect to server";
        }
        
        return "Registration failed: " + errorMessage;
    }
}
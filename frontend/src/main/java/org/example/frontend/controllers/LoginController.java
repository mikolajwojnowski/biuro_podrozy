package org.example.frontend.controllers;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.frontend.AppContext;
import org.example.frontend.SceneManager;
import org.example.frontend.models.AuthToken;
import org.example.frontend.services.AuthService;
import org.example.frontend.utils.DialogUtils;


/// LoginController is the controller for the login screen. 
/// It handles the login process and the registration process.
/// It also handles the error messages and the loading state.

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Label errorLabel;
    
    private final BooleanProperty isLoggingIn = new SimpleBooleanProperty(false);
    private final AuthService authService;

    public LoginController() {
        this.authService = new AuthService(AppContext.getApiClient());
    }

    @FXML
    private void initialize() {
        errorLabel.setVisible(false);
        errorLabel.getStyleClass().add("error-label");
        
        // Bind login button to form validation and login state
        loginButton.disableProperty().bind(
            Bindings.or(
                Bindings.or(
                    emailField.textProperty().isEmpty(),
                    passwordField.textProperty().isEmpty()
                ),
                isLoggingIn
            )
        );
    }

    @FXML
    private void handleLogin() {
        try {
            errorLabel.setVisible(false);
            isLoggingIn.set(true);
            
            String email = emailField.getText().trim();
            String password = passwordField.getText();

            System.out.println("Attempting login for user: " + email);
            AuthToken token = authService.login(email, password);
            
            if (token != null && token.getAccessToken() != null) {
                System.out.println("Login successful, setting token...");
                AppContext.setAuthToken(token);
                SceneManager.showMainScene();
            } else {
                System.out.println("Login failed - null token or access token");
                showError("Login failed - invalid response from server");
            }
            
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            String errorMessage = extractUserFriendlyMessage(e.getMessage());
            showError(errorMessage);
            // Clear password field on error
            passwordField.clear();
        } finally {
            isLoggingIn.set(false);
        }
    }

    @FXML
    private void handleRegister() {
        try {
            SceneManager.showRegisterScene();
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.showErrorDialog("Error", "Cannot load registration form: " + e.getMessage());
        }
    }
    
    private void showError(String message) {
        Platform.runLater(() -> {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
        });
    }

    private String extractUserFriendlyMessage(String errorMessage) {
        if (errorMessage == null) {
            return "Login failed. Please try again.";
        }
        
        if (errorMessage.contains("Account has been deactivated")) {
            return "This account has been deactivated";
        }
        if (errorMessage.contains("401")) {
            return "Incorrect email or password";
        }
        if (errorMessage.contains("403")) {
            return "Access denied";
        }
        if (errorMessage.contains("Connection refused")) {
            return "Cannot connect to server";
        }
        
        return "Login failed: " + errorMessage;
    }
}
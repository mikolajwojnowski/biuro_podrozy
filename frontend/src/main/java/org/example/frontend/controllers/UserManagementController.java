package org.example.frontend.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.frontend.AppContext;
import org.example.frontend.models.UserDTO;
import org.example.frontend.services.UserService;
import org.example.frontend.utils.DialogUtils;
import java.util.Optional;
import java.util.List;

public class UserManagementController {
    @FXML private TableView<UserDTO> usersTable;
    @FXML private TableColumn<UserDTO, String> emailColumn;
    @FXML private TableColumn<UserDTO, String> roleColumn;
    @FXML private TableColumn<UserDTO, Void> actionsColumn;
    @FXML private Label statusLabel;

    private final ObservableList<UserDTO> usersList = FXCollections.observableArrayList();
    private final UserService userService;
    private Stage stage;

    public UserManagementController() {
        this.userService = new UserService(AppContext.getApiClient());
    }

    @FXML
    private void initialize() {
        setupTable();
        loadUsers();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setupTable() {
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        roleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRoleName()));
        
        setupActionsColumn();
        usersTable.setItems(usersList);
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final HBox buttons = new HBox(5);
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.getStyleClass().add("delete-button");
                deleteButton.setOnAction(e -> handleDeleteUser(getTableView().getItems().get(getIndex())));
                buttons.getChildren().add(deleteButton);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    UserDTO user = getTableView().getItems().get(getIndex());
                    // Don't show delete button for the current user
                    if (user.getEmail().equals(AppContext.getCurrentUserEmail())) {
                        setGraphic(null);
                        return;
                    }
                    setGraphic(buttons);
                }
            }
        });
    }

    private void handleDeleteUser(UserDTO user) {
        Optional<ButtonType> result = DialogUtils.showConfirmation(
            "Confirm Deactivation",
            "Are you sure you want to deactivate user: " + user.getEmail() + "?"
        );
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                userService.deactivateUser(user.getId());
                loadUsers(); // Refresh the list
                updateStatus("User deactivated successfully");
            } catch (Exception e) {
                DialogUtils.showError("Error", "Failed to deactivate user: " + e.getMessage());
                updateStatus("Failed to deactivate user");
            }
        }
    }

    @FXML
    private void handleRefresh() {
        loadUsers();
    }

    @FXML
    private void handleClose() {
        stage.close();
    }

    private void loadUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers();
            usersList.setAll(users);
            updateStatus("Users loaded successfully");
        } catch (Exception e) {
            DialogUtils.showError("Error", "Failed to load users: " + e.getMessage());
            updateStatus("Failed to load users");
        }
    }

    private void updateStatus(String message) {
        Platform.runLater(() -> {
            statusLabel.setText(message);
            statusLabel.setVisible(true);
        });
    }
} 
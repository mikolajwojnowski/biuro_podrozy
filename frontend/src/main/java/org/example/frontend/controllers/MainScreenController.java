package org.example.frontend.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.frontend.AppContext;
import org.example.frontend.SceneManager;
import org.example.frontend.models.Trip;
import org.example.frontend.services.TripService;
import org.example.frontend.services.ReservationService;
import org.example.frontend.utils.DialogUtils;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/// MainScreenController is the controller for the main screen.
/// It handles the main screen and the different screens.
/// It also handles the user information and the trip information.
/// It uses the TripService to get the trip information.
/// It uses the ReservationService to get the reservation information.
/// It uses the DialogUtils to show the error messages to the user.
/// It uses the AppContext to get the ApiClient.
public class MainScreenController {
    @FXML private TableView<Trip> tripsTable;
    @FXML private TableColumn<Trip, String> titleColumn;
    @FXML private TableColumn<Trip, String> descriptionColumn;
    @FXML private TableColumn<Trip, String> dateColumn;
    @FXML private TableColumn<Trip, String> daysColumn;
    @FXML private TableColumn<Trip, String> priceColumn;
    @FXML private TableColumn<Trip, String> availableSpotsColumn;
    @FXML private TableColumn<Trip, Void> actionsColumn;
    
    @FXML private Label userInfoLabel;
    @FXML private Label statusLabel;
    @FXML private Button addTripButton;
    @FXML private MenuItem addTripMenuItem;
    @FXML private Menu tripsMenu;
    @FXML private Menu adminMenu;
    @FXML private MenuItem manageUsersMenuItem;
    @FXML private MenuItem changePasswordMenuItem;

    private final ObservableList<Trip> tripsList = FXCollections.observableArrayList();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private boolean isAdmin;
    private TripService tripService;
    private ReservationService reservationService;

    @FXML
    private void initialize() {
        System.out.println("Initializing MainScreenController...");
        tripService = new TripService(AppContext.getApiClient());
        reservationService = new ReservationService(AppContext.getApiClient());
        isAdmin = AppContext.getAuthToken().getRole().equals("ADMIN");
        setupUserInfo();
        setupTable();
        setupAdminControls();
        loadTrips();
        System.out.println("MainScreenController initialization complete.");
    }

    private void setupUserInfo() {
        String role = AppContext.getAuthToken().getRole();
        String email = AppContext.getAuthToken().getEmail();
        userInfoLabel.setText(String.format("Logged in as: %s (%s)", email, role));
        System.out.println("User info set: " + email + " (" + role + ")");
    }

    private void setupTable() {
        System.out.println("Setting up table...");
        titleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        
        // Setup description column with text wrapping
        descriptionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        descriptionColumn.setCellFactory(tc -> new TableCell<>() {
            private Text text;
            {
                text = new Text();
                text.wrappingWidthProperty().bind(descriptionColumn.widthProperty().subtract(20));
                setGraphic(text);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    text.setText("");
                } else {
                    text.setText(item);
                }
            }
        });
        
        dateColumn.setCellValueFactory(data -> {
            Trip trip = data.getValue();
            if (trip != null && trip.getTripDate() != null) {
                return new SimpleStringProperty(trip.getTripDate().format(dateFormatter));
            }
            return new SimpleStringProperty("Not set");
        });
        daysColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getDays())));
        priceColumn.setCellValueFactory(data -> new SimpleStringProperty(String.format("$%.2f", data.getValue().getPrice())));
        availableSpotsColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getAvailableSpots())));
        
        setupActionsColumn();
        tripsTable.setItems(tripsList);
        System.out.println("Table setup complete.");
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Trip trip = getTableView().getItems().get(getIndex());
                    HBox actions = new HBox(5);
                    
                    // Reserve button (available to all users if spots available)
                    Button reserveBtn = new Button("Reserve");
                    reserveBtn.getStyleClass().add("secondary-button");
                    reserveBtn.setDisable(trip.getAvailableSpots() <= 0);
                    reserveBtn.setOnAction(e -> handleReserveTrip(trip));
                    
                    // View Reservations button (available to all users)
                    Button viewReservationsBtn = new Button("View Reservations");
                    viewReservationsBtn.getStyleClass().add("secondary-button");
                    viewReservationsBtn.setOnAction(e -> showReservations(trip));
                    
                    actions.getChildren().addAll(reserveBtn, viewReservationsBtn);
                    
                    // Admin-only buttons
                    if (isAdmin) {
                        Button editBtn = new Button("Edit");
                        editBtn.getStyleClass().add("secondary-button");
                        editBtn.setOnAction(e -> handleEditTrip(trip));
                        
                        Button deleteBtn = new Button("Delete");
                        deleteBtn.getStyleClass().add("secondary-button");
                        deleteBtn.setOnAction(e -> handleDeleteTrip(trip));
                        
                        actions.getChildren().addAll(editBtn, deleteBtn);
                    }
                    
                    setGraphic(actions);
                }
            }
        });
    }

    private void setupAdminControls() {
        addTripButton.setVisible(isAdmin);
        addTripMenuItem.setVisible(isAdmin);
        tripsMenu.setVisible(isAdmin);
        adminMenu.setVisible(isAdmin);
        manageUsersMenuItem.setVisible(isAdmin);
        changePasswordMenuItem.setVisible(true);
    }

    private void loadTrips() {
        System.out.println("Loading trips...");
        try {
            List<Trip> trips = tripService.getAllTrips();
            System.out.println("Loaded " + trips.size() + " trips from backend:");
            for (Trip trip : trips) {
                System.out.println(String.format("Trip: %s, Active: %b, Available Spots: %d", 
                    trip.getTitle(), trip.isActive(), trip.getAvailableSpots()));
            }
            tripsList.setAll(trips.stream()
                .filter(Trip::isActive)
                .collect(Collectors.toList()));
            System.out.println("Filtered to " + tripsList.size() + " active trips");
            updateStatus("Trips loaded successfully");
        } catch (Exception e) {
            System.err.println("Error loading trips: " + e.getMessage());
            e.printStackTrace();
            DialogUtils.showError("Error", "Failed to load trips: " + e.getMessage());
            updateStatus("Failed to load trips");
        }
    }

    @FXML
    private void handleRefreshTrips() {
        loadTrips();
    }

    @FXML
    private void handleAddTrip() {
        if (!isAdmin) {
            DialogUtils.showError("Error", "You don't have permission to add trips");
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/TripDialog.fxml"));
            Scene scene = new Scene(loader.load());
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Trip");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tripsTable.getScene().getWindow());
            
            TripDialogController controller = loader.getController();
            controller.setTripService(tripService);
            controller.setDialogStage(dialogStage);
            controller.setOnSaveCallback(this::loadTrips);
            
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
            
        } catch (IOException e) {
            DialogUtils.showError("Error", "Could not load add trip dialog: " + e.getMessage());
        }
    }

    private void handleEditTrip(Trip trip) {
        if (!isAdmin) {
            DialogUtils.showError("Error", "You don't have permission to edit trips");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/TripDialog.fxml"));
            Scene scene = new Scene(loader.load());
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Trip");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tripsTable.getScene().getWindow());
            
            TripDialogController controller = loader.getController();
            controller.setTripService(tripService);
            controller.setDialogStage(dialogStage);
            controller.setOnSaveCallback(this::loadTrips);
            controller.setTripToEdit(trip);
            
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
            
        } catch (IOException e) {
            DialogUtils.showError("Error", "Could not load edit trip dialog: " + e.getMessage());
        }
    }

    private void handleDeleteTrip(Trip trip) {
        if (!isAdmin) {
            DialogUtils.showError("Error", "You don't have permission to delete trips");
            return;
        }
        
        if (trip == null || trip.getId() == null) {
            DialogUtils.showError("Error", "Invalid trip selected");
            return;
        }
        
        Optional<ButtonType> result = DialogUtils.showConfirmation(
            "Confirm Deactivate",
            "Are you sure you want to deactivate trip: " + trip.getTitle() + "?\nThis will hide it from the trips list."
        );
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                tripService.deleteTrip(trip.getId());
                loadTrips(); // Refresh the list after deletion
                updateStatus("Trip deactivated successfully");
            } catch (IOException e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : "Unknown error occurred";
                if (errorMsg.contains("403")) {
                    DialogUtils.showError("Error", "You don't have permission to deactivate this trip");
                } else if (errorMsg.contains("404")) {
                    DialogUtils.showError("Error", "Trip not found. It may have been already deactivated.");
                    loadTrips(); // Refresh anyway to ensure UI is up to date
                } else {
                    DialogUtils.showError("Error", "Failed to deactivate trip: " + errorMsg);
                }
                updateStatus("Failed to deactivate trip");
            }
        }
    }

    private void handleReserveTrip(Trip trip) {
        if (trip.getAvailableSpots() <= 0) {
            DialogUtils.showError("Error", "No available spots for this trip");
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ReservationDialog.fxml"));
            Scene scene = new Scene(loader.load());
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Make Reservation");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tripsTable.getScene().getWindow());
            
            ReservationDialogController controller = loader.getController();
            controller.setReservationService(reservationService);
            controller.setDialogStage(dialogStage);
            controller.setTripId(trip.getId());
            controller.setOnReserveCallback(this::loadTrips);
            
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
            
        } catch (IOException e) {
            DialogUtils.showError("Error", "Could not load reservation dialog: " + e.getMessage());
        }
    }

    private void showReservations(Trip trip) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/reservationList.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Reservations");
            stage.setScene(new Scene(root));
            
            ReservationListController controller = loader.getController();
            controller.setStage(stage);
            controller.setTrip(trip);
            controller.setOnReservationDeletedCallback(this::loadTrips);
            
            stage.show();
        } catch (IOException e) {
            DialogUtils.showError("Error", "Could not open reservations window: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            AppContext.clearAuthToken();
            SceneManager.showLoginScene();
        } catch (Exception e) {
            DialogUtils.showError("Error", "Failed to logout: " + e.getMessage());
        }
    }

    @FXML
    private void handleAbout() {
        DialogUtils.showInfo("About", "Biuro Podróży\nWersja 1\n\nStworzone przez: \n\nMikolaj Wojnowski\nJan Tomasik\nArkadiusz Orzel\nKamil Zajac");
    }

    @FXML
    private void handleChangePassword() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ChangePasswordDialog.fxml"));
            Parent root = loader.load();
            
            ChangePasswordController controller = loader.getController();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Change Password");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(tripsTable.getScene().getWindow());
            
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            
            controller.setDialogStage(dialogStage);
            controller.setOnPasswordChanged(() -> {
                SceneManager.showLoginScene();
            });
            
            dialogStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to open change password dialog: " + e.getMessage());
        }
    }

    @FXML
    private void handleManageUsers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserManagement.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("User Management");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            
            UserManagementController controller = loader.getController();
            controller.setStage(stage);
            
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load user management window", Alert.AlertType.ERROR);
        }
    }

    private void updateStatus(String message) {
        Platform.runLater(() -> statusLabel.setText(message));
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 
package org.example.frontend.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.frontend.AppContext;
import org.example.frontend.models.ReservationDTO;
import org.example.frontend.models.TripDTO;
import org.example.frontend.services.ReservationService;
import org.example.frontend.utils.DialogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ReservationManagementController {
    @FXML private TableView<ReservationDTO> reservationsTable;
    @FXML private TableColumn<ReservationDTO, String> userEmailColumn;
    @FXML private TableColumn<ReservationDTO, String> numberOfPeopleColumn;
    @FXML private TableColumn<ReservationDTO, String> totalPriceColumn;
    @FXML private TableColumn<ReservationDTO, String> statusColumn;
    @FXML private TableColumn<ReservationDTO, Void> actionsColumn;
    @FXML private Label tripDetailsLabel;
    @FXML private Label statusLabel;

    private final ObservableList<ReservationDTO> reservationsList = FXCollections.observableArrayList();
    private final ReservationService reservationService;
    private Stage stage;
    private TripDTO trip;

    public ReservationManagementController() {
        this.reservationService = new ReservationService(AppContext.getApiClient());
    }

    @FXML
    private void initialize() {
        setupTable();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setTrip(TripDTO trip) {
        this.trip = trip;
        tripDetailsLabel.setText(String.format("%s (%s - %s)", 
            trip.getName(), trip.getStartDate(), trip.getEndDate()));
        loadReservations();
    }

    private void setupTable() {
        userEmailColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getUserEmail()));
        numberOfPeopleColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getNumberOfPeople().toString()));
        totalPriceColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getTotalPrice().toString()));
        statusColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().isActive() ? "Active" : "Inactive"));
        
        setupActionsColumn();
        reservationsTable.setItems(reservationsList);
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deactivateButton = new Button("Deactivate");
            private final Button pdfButton = new Button("Generate PDF");
            private final HBox buttonsBox = new HBox(5, deactivateButton, pdfButton);

            {
                deactivateButton.getStyleClass().add("delete-button");
                pdfButton.getStyleClass().add("pdf-button");
                
                deactivateButton.setOnAction(e -> handleDeactivateReservation(getTableView().getItems().get(getIndex())));
                pdfButton.setOnAction(e -> handleGeneratePdf(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ReservationDTO reservation = getTableView().getItems().get(getIndex());
                    deactivateButton.setVisible(reservation.isActive());
                    setGraphic(buttonsBox);
                }
            }
        });
    }

    private void handleDeactivateReservation(ReservationDTO reservation) {
        Optional<ButtonType> result = DialogUtils.showConfirmation(
            "Confirm Deactivation",
            "Are you sure you want to deactivate this reservation?"
        );
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                reservationService.cancelReservation(reservation.getId());
                loadReservations(); // Refresh the list
                updateStatus("Reservation deactivated successfully");
            } catch (Exception e) {
                DialogUtils.showError("Error", "Failed to deactivate reservation: " + e.getMessage());
                updateStatus("Failed to deactivate reservation");
            }
        }
    }

    private void handleGeneratePdf(ReservationDTO reservation) {
        try {
            byte[] pdfBytes = reservationService.generateReservationPdf(reservation.getId());
            
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF");
            fileChooser.setInitialFileName("reservation_" + reservation.getId() + ".pdf");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf")
            );
            
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(pdfBytes);
                }
                updateStatus("PDF generated successfully");
            }
        } catch (Exception e) {
            DialogUtils.showError("Error", "Failed to generate PDF: " + e.getMessage());
            updateStatus("Failed to generate PDF");
        }
    }

    @FXML
    private void handleRefresh() {
        loadReservations();
    }

    @FXML
    private void handleClose() {
        stage.close();
    }

    private void loadReservations() {
        try {
            List<ReservationDTO> reservations = reservationService.getReservationsForTrip(trip.getId());
            reservationsList.setAll(reservations);
            updateStatus("Reservations loaded successfully");
        } catch (Exception e) {
            DialogUtils.showError("Error", "Failed to load reservations: " + e.getMessage());
            updateStatus("Failed to load reservations");
        }
    }

    private void updateStatus(String message) {
        Platform.runLater(() -> {
            statusLabel.setText(message);
            statusLabel.setVisible(true);
        });
    }
} 
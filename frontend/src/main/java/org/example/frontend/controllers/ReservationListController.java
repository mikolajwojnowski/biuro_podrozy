package org.example.frontend.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.frontend.models.ReservationDTO;
import org.example.frontend.models.Trip;
import org.example.frontend.services.ReservationService;
import org.example.frontend.services.TripService;
import org.example.frontend.utils.DialogUtils;
import org.example.frontend.utils.ApiClient;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationListController {
    @FXML private TableView<ReservationDTO> reservationsTable;
    @FXML private TableColumn<ReservationDTO, String> nameColumn;
    @FXML private TableColumn<ReservationDTO, String> surnameColumn;
    @FXML private TableColumn<ReservationDTO, String> emailColumn;
    @FXML private TableColumn<ReservationDTO, String> phoneColumn;
    @FXML private TableColumn<ReservationDTO, String> participantsColumn;
    @FXML private TableColumn<ReservationDTO, String> totalPriceColumn;
    @FXML private TableColumn<ReservationDTO, Void> actionsColumn;
    @FXML private Label tripDetailsLabel;
    @FXML private Label statusLabel;

    private final ObservableList<ReservationDTO> reservationsList = FXCollections.observableArrayList();
    private final ReservationService reservationService;
    private final TripService tripService;
    private Stage stage;
    private Trip trip;
    private Runnable onReservationDeletedCallback;

    public ReservationListController() {
        ApiClient apiClient = new ApiClient();
        this.reservationService = new ReservationService(apiClient);
        this.tripService = new TripService(apiClient);
    }

    @FXML
    private void initialize() {
        setupTable();
        reservationsTable.setItems(reservationsList);
    }

    private void setupTable() {
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        surnameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSurname()));
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        phoneColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhoneNumber()));
        participantsColumn.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getParticipants().stream()
                .map(p -> p.getName() + " " + p.getSurname())
                .collect(Collectors.joining(", "))
        ));
        totalPriceColumn.setCellValueFactory(data -> new SimpleStringProperty(
            String.format("$%.2f", data.getValue().getTotalPrice())
        ));
        
        setupActionsColumn();
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            private final Button pdfButton = new Button("Generate PDF");
            private final HBox buttonsBox = new HBox(5, deleteButton, pdfButton);

            {
                deleteButton.setOnAction(e -> handleDeleteReservation(getTableRow().getItem()));
                pdfButton.setOnAction(e -> handleGeneratePdf(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonsBox);
            }
        });
    }

    private void handleDeleteReservation(ReservationDTO reservation) {
        try {
            reservationService.cancelReservation(reservation.getId());
            trip.setAvailableSpots(trip.getAvailableSpots() + reservation.getParticipants().size());
            tripService.updateTrip(trip.getId(), trip);
            loadReservations();
            updateStatus("Reservation cancelled successfully");
            if (onReservationDeletedCallback != null) {
                onReservationDeletedCallback.run();
            }
        } catch (Exception e) {
            DialogUtils.showError("Error", "Failed to cancel reservation: " + e.getMessage());
        }
    }

    private void handleGeneratePdf(ReservationDTO reservation) {
        try {
            byte[] pdfContent = reservationService.generateReservationPdf(reservation.getId());
            
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Reservation Confirmation");
            fileChooser.setInitialFileName("reservation_" + reservation.getId() + ".pdf");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
            );

            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(pdfContent);
                }
                updateStatus("PDF generated successfully");
            }
        } catch (Exception e) {
            DialogUtils.showError("Error", "Failed to generate PDF: " + e.getMessage());
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
        tripDetailsLabel.setText("Reservations for: " + trip.getTitle());
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
        } catch (Exception e) {
            DialogUtils.showError("Error", "Failed to load reservations: " + e.getMessage());
        }
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    public void setOnReservationDeletedCallback(Runnable callback) {
        this.onReservationDeletedCallback = callback;
    }
} 
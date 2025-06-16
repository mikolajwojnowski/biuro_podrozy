package org.example.frontend.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.frontend.AppContext;
import org.example.frontend.models.Reservation;
import org.example.frontend.models.Trip;
import org.example.frontend.models.ParticipantDTO;
import org.example.frontend.services.ReservationService;
import org.example.frontend.utils.DialogUtils;
import org.example.frontend.models.ReservationDTO;

public class ReservationDialogController {
    @FXML private TextField nameField;
    @FXML private TextField surnameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TableView<ParticipantDTO> participantsTable;
    @FXML private TableColumn<ParticipantDTO, String> participantNameColumn;
    @FXML private TableColumn<ParticipantDTO, String> participantSurnameColumn;
    @FXML private TextField participantNameField;
    @FXML private TextField participantSurnameField;
    @FXML private Button addParticipantButton;
    @FXML private Button removeParticipantButton;
    @FXML private Label errorLabel;

    private ReservationService reservationService;
    private Stage dialogStage;
    private Long tripId;
    private Runnable onReserveCallback;
    private final ObservableList<ParticipantDTO> participantsList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupParticipantsTable();
        errorLabel.setVisible(false);
    }

    private void setupParticipantsTable() {
        participantNameColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        participantSurnameColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSurname()));
        
        participantsTable.setItems(participantsList);
        
        // Enable remove button only when a participant is selected
        removeParticipantButton.setDisable(true);
        participantsTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> removeParticipantButton.setDisable(newSelection == null));
    }

    @FXML
    private void handleAddParticipant() {
        String name = participantNameField.getText().trim();
        String surname = participantSurnameField.getText().trim();

        if (name.isEmpty() || surname.isEmpty()) {
            DialogUtils.showError("Error", "Both name and surname are required for participants");
            return;
        }

        participantsList.add(new ParticipantDTO(name, surname));
        participantNameField.clear();
        participantSurnameField.clear();
    }

    @FXML
    private void handleRemoveParticipant() {
        ParticipantDTO selectedParticipant = participantsTable.getSelectionModel().getSelectedItem();
        if (selectedParticipant != null) {
            participantsList.remove(selectedParticipant);
        }
    }

    public void setReservationService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public void setOnReserveCallback(Runnable callback) {
        this.onReserveCallback = callback;
    }

    @FXML
    private void handleReserve() {
        try {
            errorLabel.setVisible(false);
            
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            if (!validateInput()) {
                return;
            }

            if (participantsList.isEmpty()) {
                DialogUtils.showError("Error", "At least one participant is required");
                return;
            }

            ReservationDTO reservationDTO = new ReservationDTO();
            reservationDTO.setTripId(tripId);
            reservationDTO.setName(name);
            reservationDTO.setSurname(surname);
            reservationDTO.setEmail(email);
            reservationDTO.setPhoneNumber(phone);
            reservationDTO.setParticipants(participantsList);
            
            reservationService.createReservation(reservationDTO);
            
            if (onReserveCallback != null) {
                onReserveCallback.run();
            }
            dialogStage.close();
        } catch (Exception e) {
            String errorMessage = extractUserFriendlyMessage(e.getMessage());
            showError(errorMessage);
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();

        if (nameField.getText().trim().isEmpty()) {
            errorMessage.append("Name is required\n");
        }
        if (surnameField.getText().trim().isEmpty()) {
            errorMessage.append("Surname is required\n");
        }

        // Email is optional but validate format if provided
        String email = emailField.getText().trim();
        if (!email.isEmpty() && !isValidEmail(email)) {
            errorMessage.append("Please provide a valid email address\n");
        }

        // Phone number is optional but validate format if provided
        String phone = phoneField.getText().trim();
        if (!phone.isEmpty() && !isValidPhoneNumber(phone)) {
            errorMessage.append("Please provide a valid phone number (exactly 9 digits, e.g., 123456789)\n");
        }

        if (errorMessage.length() > 0) {
            errorLabel.setText(errorMessage.toString());
            errorLabel.setVisible(true);
            return false;
        }

        errorLabel.setVisible(false);
        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    private boolean isValidPhoneNumber(String phone) {
        String phoneRegex = "^[0-9]{9}$";
        return phone.matches(phoneRegex);
    }

    private String extractUserFriendlyMessage(String fullMessage) {
        if (fullMessage != null) {
            if (fullMessage.contains("Not enough available spots")) {
                return "There are not enough available spots for this reservation.";
            }
        }
        return "Failed to create reservation. Please try again later.";
    }

    private void showError(String message) {
        DialogUtils.showError("Error", message);
    }
} 
package org.example.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.frontend.models.Trip;
import org.example.frontend.services.TripService;
import org.example.frontend.utils.DialogUtils;

import java.time.LocalDate;

public class TripDialogController {
    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker tripDatePicker;
    @FXML private TextField daysField;
    @FXML private TextField capacityField;
    @FXML private TextField priceField;
    @FXML private CheckBox isActiveCheckBox;
    @FXML private Label errorLabel;
    @FXML private Text dialogTitle;

    private TripService tripService;
    private Stage dialogStage;
    private Runnable onSaveCallback;
    private Trip tripToEdit;

    public void initialize() {
        tripDatePicker.setValue(LocalDate.now());
        
        // Add tooltips for numeric fields
        daysField.setTooltip(new Tooltip("Enter number of days (greater than 0)"));
        capacityField.setTooltip(new Tooltip("Enter capacity (greater than 0)"));
        priceField.setTooltip(new Tooltip("Enter price (greater than 0)"));
        
        errorLabel.setVisible(false);
    }

    public void setTripService(TripService tripService) {
        this.tripService = tripService;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    public void setTripToEdit(Trip trip) {
        this.tripToEdit = trip;
        if (trip != null) {
            // Update dialog title
            dialogTitle.setText("Edit Trip");
            dialogStage.setTitle("Edit Trip");
            
            // Populate fields with trip data
            titleField.setText(trip.getTitle());
            descriptionField.setText(trip.getDescription());
            tripDatePicker.setValue(trip.getTripDate());
            daysField.setText(String.valueOf(trip.getDays()));
            capacityField.setText(String.valueOf(trip.getCapacity()));
            priceField.setText(String.format("%.2f", trip.getPrice()));
            isActiveCheckBox.setSelected(trip.isActive());
        } else {
            dialogTitle.setText("Add New Trip");
            dialogStage.setTitle("Add New Trip");
        }
    }

    @FXML
    private void handleSave() {
        if (!validateInput()) {
            return;
        }

        try {
            Trip trip = tripToEdit != null ? tripToEdit : new Trip();
            trip.setTitle(titleField.getText().trim());
            trip.setDescription(descriptionField.getText().trim());
            trip.setTripDate(tripDatePicker.getValue());
            trip.setDays(Integer.parseInt(daysField.getText().trim()));
            trip.setCapacity(Integer.parseInt(capacityField.getText().trim()));
            trip.setPrice(Double.parseDouble(priceField.getText().trim()));
            trip.setActive(isActiveCheckBox.isSelected());

            if (tripToEdit != null) {
                System.out.println("Updating trip with ID: " + trip.getId());
                tripService.updateTrip(trip.getId(), trip);
            } else {
                System.out.println("Creating new trip with date: " + trip.getTripDate());
                tripService.createTrip(trip);
            }
            
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
            dialogStage.close();
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.contains("403")) {
                DialogUtils.showError("Error", "A trip with this title already exists. Please choose a different title.");
                titleField.requestFocus();
            } else {
                DialogUtils.showError("Error", 
                    tripToEdit != null ? "Failed to update trip: " : "Failed to create trip: " + errorMsg);
            }
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();

        // Title validation
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            errorMessage.append("Title is required\n");
        }

        // Description validation
        String description = descriptionField.getText().trim();
        if (description.isEmpty()) {
            errorMessage.append("Description is required\n");
        }

        // Date validation
        if (tripDatePicker.getValue() == null) {
            errorMessage.append("Trip date is required\n");
        } else if (tripDatePicker.getValue().isBefore(LocalDate.now())) {
            errorMessage.append("Trip date cannot be in the past\n");
        }

        // Numeric fields validation
        try {
            // Days validation
            String daysText = daysField.getText().trim();
            if (daysText.isEmpty()) {
                errorMessage.append("Number of days is required\n");
            } else {
                int days = Integer.parseInt(daysText);
                if (days <= 0) {
                    errorMessage.append("Days must be greater than 0\n");
                }
            }

            // Capacity validation
            String capacityText = capacityField.getText().trim();
            if (capacityText.isEmpty()) {
                errorMessage.append("Capacity is required\n");
            } else {
                int capacity = Integer.parseInt(capacityText);
                if (capacity <= 0) {
                    errorMessage.append("Capacity must be greater than 0\n");
                }
            }

            // Price validation
            String priceText = priceField.getText().trim();
            if (priceText.isEmpty()) {
                errorMessage.append("Price is required\n");
            } else {
                double price = Double.parseDouble(priceText);
                if (price <= 0) {
                    errorMessage.append("Price must be greater than 0\n");
                }
            }
        } catch (NumberFormatException e) {
            errorMessage.append("Please enter valid numbers for days, capacity, and price\n");
        }

        if (errorMessage.length() > 0) {
            errorLabel.setText(errorMessage.toString());
            errorLabel.setVisible(true);
            return false;
        }

        errorLabel.setVisible(false);
        return true;
    }
} 
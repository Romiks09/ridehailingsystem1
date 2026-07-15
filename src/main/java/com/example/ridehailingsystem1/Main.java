package com.example.ridehailingsystem1;

import com.ridehailing.model.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Passenger currentPassenger = new Passenger("P01", "Alice", "09171234567", "GCash");
    private Driver currentDriver = new Driver("D01", "Bob", 4.9, new Vehicle("ABC-1234", "Toyota Vios", "Car"));
    private Ride activeRide = null;

    private TextField txtPickup = new TextField();
    private TextField txtDestination = new TextField();
    private Button btnBookRide = new Button("Book Ride");
    private Label lblPassengerStatus = new Label("Status: Idle");
    private Label lblFare = new Label("Fare: ₱0.00");

    private Label lblDriverInfo = new Label();
    private Button btnAcceptRide = new Button("Accept Ride Request");
    private Button btnStartRide = new Button("Start Ride");
    private Button btnEndRide = new Button("End Ride");
    private Label lblDriverStatus = new Label("Status: Available");

    private TextArea systemLogs = new TextArea();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Ride-Hailing Simulator (Uber/Angkas)");

        HBox mainLayout = new HBox(20);
        mainLayout.setPadding(new Insets(15));

        VBox passengerSide = buildPassengerUI();
        VBox driverSide = buildDriverUI();
        VBox systemLogsSide = buildLogsUI();

        mainLayout.getChildren().addAll(passengerSide, driverSide, systemLogsSide);
        HBox.setHgrow(systemLogsSide, Priority.ALWAYS);

        setupEventHandlers();

        Scene scene = new Scene(mainLayout, 950, 480);
        primaryStage.setScene(scene);
        primaryStage.show();

        log("System initialized. Alice (Passenger) and Bob (Driver) are online.");
    }

    private VBox buildPassengerUI() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setStyle("-fx-border-color: #cbd5e1; -fx-border-radius: 8; -fx-background-color: #f8fafc;");
        vbox.setPrefWidth(280);

        Label lblTitle = new Label("Passenger App (Alice)");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        txtPickup.setPromptText("Enter Pickup Location");
        txtDestination.setPromptText("Enter Destination Location");

        lblPassengerStatus.setStyle("-fx-text-fill: #475569;");
        lblFare.setStyle("-fx-font-weight: bold;");

        vbox.getChildren().addAll(
                lblTitle,
                new Separator(),
                new Label("Pickup:"), txtPickup,
                new Label("Destination:"), txtDestination,
                btnBookRide,
                lblFare,
                lblPassengerStatus
        );
        return vbox;
    }

    private VBox buildDriverUI() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setStyle("-fx-border-color: #cbd5e1; -fx-border-radius: 8; -fx-background-color: #fdf2f8;");
        vbox.setPrefWidth(280);

        Label lblTitle = new Label("Driver App (Bob)");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        lblDriverInfo.setText("Vehicle: " + currentDriver.getVehicle().getVehicleInfo());
        lblDriverInfo.setWrapText(true);

        btnAcceptRide.setDisable(true);
        btnStartRide.setDisable(true);
        btnEndRide.setDisable(true);

        vbox.getChildren().addAll(
                lblTitle,
                new Separator(),
                lblDriverInfo,
                lblDriverStatus,
                btnAcceptRide,
                btnStartRide,
                btnEndRide
        );
        return vbox;
    }

    private VBox buildLogsUI() {
        VBox vbox = new VBox(10);
        Label lblLogs = new Label("System Process Logs");
        lblLogs.setStyle("-fx-font-weight: bold;");
        systemLogs.setEditable(false);
        systemLogs.setWrapText(true);
        vbox.getChildren().addAll(lblLogs, systemLogs);
        return vbox;
    }

    private void setupEventHandlers() {
        btnBookRide.setOnAction(e -> {
            String pickup = txtPickup.getText().trim();
            String dest = txtDestination.getText().trim();

            if (pickup.isEmpty() || dest.isEmpty()) {
                showAlert("Validation Error", "Please fill out both pickup and destination locations.");
                return;
            }

            activeRide = new Ride("RIDE-" + System.currentTimeMillis() % 1000, pickup, dest, currentPassenger);
            lblFare.setText(String.format("Fare: ₱%.2f", activeRide.getFare()));
            lblPassengerStatus.setText("Status: Looking for Driver...");

            log("Passenger Alice requested a ride from '" + pickup + "' to '" + dest + "'. Fare: ₱" + String.format("%.2f", activeRide.getFare()));

            btnAcceptRide.setDisable(false);
            btnBookRide.setDisable(true);
            lblDriverStatus.setText("Status: Incoming request found!");
        });

        btnAcceptRide.setOnAction(e -> {
            if (activeRide == null) return;

            activeRide.setDriver(currentDriver);
            activeRide.setStatus("ACCEPTED");
            currentDriver.setStatus("ON_TRIP");

            log("Driver Bob accepted the ride request.");
            lblDriverStatus.setText("Status: En route to Passenger...");
            lblPassengerStatus.setText("Status: Driver Bob is coming!");

            btnAcceptRide.setDisable(true);
            btnStartRide.setDisable(false);
        });

        btnStartRide.setOnAction(e -> {
            if (activeRide == null) return;

            activeRide.setStatus("STARTED");
            log("Ride started! Navigating to " + activeRide.getDestination());
            lblPassengerStatus.setText("Status: In transit...");
            lblDriverStatus.setText("Status: Driving to destination...");

            btnStartRide.setDisable(true);
            btnEndRide.setDisable(false);
        });

        btnEndRide.setOnAction(e -> {
            if (activeRide == null) return;

            activeRide.setStatus("COMPLETED");
            log("Destination reached. Ride complete.");

            Payment payment = new Payment("PAY-" + System.currentTimeMillis() % 1000, activeRide.getFare(), currentPassenger.getPaymentMethod());
            log("Processing payment of ₱" + String.format("%.2f", activeRide.getFare()) + " via " + currentPassenger.getPaymentMethod() + "...");

            if (payment.processPayment() && payment.confirmPayment()) {
                log("Payment successful! Transferred earnings to Bob.");
            }

            lblPassengerStatus.setText("Status: Complete! Rate your driver.");
            lblDriverStatus.setText("Status: Available");

            showRatingAlert();

            btnEndRide.setDisable(true);
            btnBookRide.setDisable(false);
            txtPickup.clear();
            txtDestination.clear();
            lblFare.setText("Fare: ₱0.00");
            activeRide = null;
        });
    }

    private void showRatingAlert() {
        ChoiceDialog<Integer> ratingDialog = new ChoiceDialog<>(5, 1, 2, 3, 4, 5);
        ratingDialog.setTitle("Rate your Driver");
        ratingDialog.setHeaderText("How was your trip with Bob?");
        ratingDialog.setContentText("Select Rating (1-5):");
        ratingDialog.showAndWait().ifPresent(rating -> {
            log("Passenger Alice rated Driver Bob: " + rating + " ⭐");
            lblPassengerStatus.setText("Status: Idle");
        });
    }

    private void log(String message) {
        systemLogs.appendText("[SYSTEM LOG] " + message + "\n");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

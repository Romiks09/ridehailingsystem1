package com.example.ridehailingsystem1;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorMessage;

    @FXML
    private Hyperlink forgotPasswordLink;

    @FXML
    private Hyperlink signupLink;

    // ====== INITIALIZATION ======
    @FXML
    public void initialize() {
        // Set up Enter key listener on the password field
        passwordField.setOnKeyPressed(this::handleEnterKey);
        usernameField.setOnKeyPressed(this::handleEnterKey);

        // Clear error message when user starts typing
        usernameField.textProperty().addListener((obs, old, newVal) -> errorMessage.setText(""));
        passwordField.textProperty().addListener((obs, old, newVal) -> errorMessage.setText(""));
    }

    // ====== HANDLE ENTER KEY ======
    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleLogin(null);
        }
    }

    // ====== LOGIN ACTION ======
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Validation
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        // Simulate authentication (replace with real DB/auth logic)
        if (validateCredentials(username, password)) {
            loginSuccess();
        } else {
            showError("Invalid username or password. Please try again.");
            clearPassword();
        }
    }

    // ====== CREDENTIAL VALIDATION ======
    private boolean validateCredentials(String username, String password) {
        // 🔥 REPLACE THIS WITH YOUR ACTUAL AUTH LOGIC 🔥
        // For demo purposes, accept admin/admin123 or user/password
        return ("admin".equalsIgnoreCase(username) && "admin123".equals(password)) ||
                ("user".equalsIgnoreCase(username) && "password".equals(password));
    }

    // ====== LOGIN SUCCESS ======
    private void loginSuccess() {
        try {
            // Load the dashboard or main application screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("dashboard.css").toExternalForm());

            stage.setTitle("Dashboard - MyApp");
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setWidth(1024);
            stage.setHeight(768);
            stage.show();

            System.out.println("✅ Login successful! Welcome, " + usernameField.getText().trim() + "!");

        } catch (Exception e) {
            e.printStackTrace();
            showError("An error occurred while loading the dashboard.");
        }
    }

    // ====== ERROR HANDLING ======
    private void showError(String message) {
        errorMessage.setText(message);
        errorMessage.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");

        // Fade out error after 5 seconds
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(e -> {
            if (errorMessage != null) {
                errorMessage.setText("");
            }
        });
        delay.play();
    }

    private void clearPassword() {
        passwordField.clear();
        passwordField.requestFocus();
    }

    // ====== FORGOT PASSWORD ======
    @FXML
    private void handleForgotPassword(ActionEvent event) {
        System.out.println("🔑 Forgot password clicked");
        // TODO: Open forgot password dialog/window
        // Example: show a simple alert
        // Alert alert = new Alert(Alert.AlertType.INFORMATION);
        // alert.setTitle("Password Reset");
        // alert.setHeaderText("Reset Password");
        // alert.setContentText("A password reset link will be sent to your email.");
        // alert.showAndWait();
    }

    // ====== SIGN UP ======
    @FXML
    private void handleSignup(ActionEvent event) {
        System.out.println("📝 Sign up clicked");
        // TODO: Open registration/signup screen
        // Example: Load Signup.fxml
        // try {
        //     FXMLLoader loader = new FXMLLoader(getClass().getResource("Signup.fxml"));
        //     Parent root = loader.load();
        //     Stage stage = (Stage) signupLink.getScene().getWindow();
        //     stage.setScene(new Scene(root));
        //     stage.setTitle("Create Account");
        //     stage.show();
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
    }
}
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignupController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    private Stage stage;

    @FXML
    private void handleSignup() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            // Validate input fields (optional)
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showAlert("Error", "Please fill out all fields.");
                return;
            }

            // Insert into database
            insertAdmin(firstName, lastName, email, password);

            // Show success message
            showAlert("Signup Successful", "Admin registered successfully!");

            // Close the signup window
            closeSignupWindow();

            // Open the login window
            openLoginWindow();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while trying to sign up.");
        }
    }

    private void insertAdmin(String firstName, String lastName, String email, String password) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "INSERT INTO Admins (FirstName, LastName, Email, Password) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.executeUpdate();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeSignupWindow() {
        stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void openLoginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Close the signup window
            closeSignupWindow();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

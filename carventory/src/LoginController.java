import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT ID FROM Admins WHERE Email = ? AND Password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int adminID = rs.getInt("ID");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("carpage.fxml"));
                Parent root = loader.load();
                CarPageController controller = loader.getController();
                controller.setAdminID(adminID);

                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setTitle("Car Page");
                stage.setScene(new Scene(root));
            } else {
                showAlert("Login Failed", "Invalid email or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while trying to log in.");
        }
    }

    @FXML
    private void handleSignup() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("signup.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setTitle("Signup");
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while trying to load the signup page.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

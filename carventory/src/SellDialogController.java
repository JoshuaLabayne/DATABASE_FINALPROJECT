import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class SellDialogController {

    @FXML
    private TextField soldQuantityField;
    @FXML
    private void handleCancel() {
        ((Stage) soldQuantityField.getScene().getWindow()).close();
    }
    private int carID;
    private CarPageController mainController;

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public void setMainController(CarPageController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleSell() {
        try {
            int quantitySold = Integer.parseInt(soldQuantityField.getText());
            
            // Update car quantity locally in the UI
            mainController.updateCarQuantity(carID, quantitySold);
            
            // Update sales record in the database
            updateSalesRecord(carID, quantitySold);
            
            // Close the dialog
            ((Stage) soldQuantityField.getScene().getWindow()).close();
        } catch (NumberFormatException e) {
            mainController.showAlert("Error", "Please enter a valid number.");
        } catch (SQLException e) {
            mainController.showAlert("Database Error", "Failed to update sales record: " + e.getMessage());
        }
    }
    
    private void updateSalesRecord(int carID, int quantitySold) throws SQLException {
        // Assuming you have a database connection
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/base", "root", "")) {
            String sql = "INSERT INTO base_sales (carID, quantitySold, saleDate) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, carID);
                stmt.setInt(2, quantitySold);
                stmt.setDate(3, java.sql.Date.valueOf(LocalDate.now())); // Assuming saleDate is a DATE column
                
                stmt.executeUpdate();
            }
        }
    }
}

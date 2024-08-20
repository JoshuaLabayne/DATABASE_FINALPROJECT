import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SalesPageController {

    @FXML
    private TableView<Sale> salesTable;

    @FXML
    private TableColumn<Sale, Integer> salesIDColumn;

    @FXML
    private TableColumn<Sale, Integer> carIDColumn;

    @FXML
    private TableColumn<Sale, Integer> quantitySoldColumn;

    @FXML
    private TableColumn<Sale, BigDecimal> totalAmountSoldColumn;

    @FXML
    private TableColumn<Sale, LocalDateTime> saleDateColumn;

    private ObservableList<Sale> salesData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        salesIDColumn.setCellValueFactory(new PropertyValueFactory<>("salesID"));
        carIDColumn.setCellValueFactory(new PropertyValueFactory<>("carID"));
        quantitySoldColumn.setCellValueFactory(new PropertyValueFactory<>("quantitySold"));
        totalAmountSoldColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmountSold"));

        // Custom cell factory to format the date without time
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        saleDateColumn.setCellValueFactory(new PropertyValueFactory<>("saleDate"));
        saleDateColumn.setCellFactory(column -> new TableCell<Sale, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(item));
                }
            }
        });

        salesTable.setItems(salesData);
        loadSalesData();
    }

    private void loadSalesData() {
        salesData.clear();
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT SalesID, CarID, QuantitySold, TotalAmountSold, SaleDate FROM base_sales";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int salesID = rs.getInt("SalesID");
                int carID = rs.getInt("CarID");
                int quantitySold = rs.getInt("QuantitySold");
                BigDecimal totalAmountSold = rs.getBigDecimal("TotalAmountSold");
                LocalDateTime saleDate = rs.getTimestamp("SaleDate").toLocalDateTime();

                Sale sale = new Sale(salesID, carID, quantitySold, totalAmountSold, saleDate);
                salesData.add(sale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while trying to load the sales data.");
        }
    }
    @FXML
    private void goToCarPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("carpage.fxml"));
            Parent root = loader.load();
    
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Car Page");
            
            // Hide current window (optional)
            ((Stage) salesTable.getScene().getWindow()).close(); 
            
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load Car Page: " + e.getMessage());
        }
    }
    @FXML
    private void handleLogout() {
        try {
            // Close current window
            Stage stage = (Stage) salesTable.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not logout: " + e.getMessage());
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

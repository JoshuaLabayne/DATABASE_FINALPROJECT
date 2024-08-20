import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.cell.PropertyValueFactory;

public class CarPageController {

    @FXML
    private TextField carModelField;

    @FXML
    private TextField yearModelField;

    @FXML
    private ComboBox<String> transmissionComboBox;

    @FXML
    private ComboBox<String> colorComboBox;

    @FXML
    private ComboBox<String> bodyTypeComboBox;

    @FXML
    private TextField priceField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField soldField;

    @FXML
    private DatePicker nextRestockField;

    @FXML
    private ImageView imageView;

    @FXML
    private Label resultLabel;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Car> carTable;

    @FXML
    private TableColumn<Car, Integer> carIDColumn;

    @FXML
    private TableColumn<Car, Integer> adminIDColumn;

    @FXML
    private TableColumn<Car, String> carModelColumn;

    @FXML
    private TableColumn<Car, String> yearModelColumn;

    @FXML
    private TableColumn<Car, String> transmissionColumn;

    @FXML
    private TableColumn<Car, String> colorColumn;

    @FXML
    private TableColumn<Car, String> bodyTypeColumn;

    @FXML
    private TableColumn<Car, String> priceColumn;

    @FXML
    private TableColumn<Car, String> quantityColumn;

    @FXML
    private TableColumn<Car, String> soldColumn;

    @FXML
    private TableColumn<Car, LocalDate> nextRestockColumn;

    @FXML
    private TableColumn<Car, Image> imageColumn;

    private ObservableList<Car> carData = FXCollections.observableArrayList();

    private ObservableList<Car> filteredData = FXCollections.observableArrayList();

    private int adminID; // Placeholder for adminID, replace with actual logic

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    @FXML
    private void initialize() {
        // Initialize ComboBox options
        initializeTransmissionComboBox();
        initializeColorComboBox();
        initializeBodyTypeComboBox();

        // Initialize table columns
        carIDColumn.setCellValueFactory(new PropertyValueFactory<>("carID"));
        adminIDColumn.setCellValueFactory(new PropertyValueFactory<>("adminID"));
        carModelColumn.setCellValueFactory(new PropertyValueFactory<>("carModel"));
        yearModelColumn.setCellValueFactory(new PropertyValueFactory<>("yearModel"));
        transmissionColumn.setCellValueFactory(new PropertyValueFactory<>("transmission"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        bodyTypeColumn.setCellValueFactory(new PropertyValueFactory<>("bodyType"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        soldColumn.setCellValueFactory(new PropertyValueFactory<>("sold"));
        nextRestockColumn.setCellValueFactory(new PropertyValueFactory<>("nextRestock"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        imageColumn.setCellFactory(new ImageViewTableCell());

        carTable.setItems(carData);
        loadCarData();
    }

    private void initializeTransmissionComboBox() {
        ObservableList<String> options = FXCollections.observableArrayList("CVT", "Manual");
        transmissionComboBox.setItems(options);
    }

    private void initializeColorComboBox() {
        ObservableList<String> options = FXCollections.observableArrayList(
                "Black", "White", "Gray", "Red", "Blue", "Orange", "Yellow"
        );
        colorComboBox.setItems(options);
    }

    private void initializeBodyTypeComboBox() {
        ObservableList<String> options = FXCollections.observableArrayList(
                "Sedan", "Hatchback", "Coupe", "Crossover", "SUV", "MPV"
        );
        bodyTypeComboBox.setItems(options);
    }

    @FXML
    private void goToSalesPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SalesPage.fxml"));
            Parent salesPageParent = loader.load();
            Scene salesPageScene = new Scene(salesPageParent);

            Stage window = (Stage) carTable.getScene().getWindow(); // Get the current stage
            window.setScene(salesPageScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load Sales Page.");
        }
    }
@   FXML
    private void handleLogout() {
    Stage stage = (Stage) carTable.getScene().getWindow();
    stage.close();
    Platform.exit(); // Close application (optional)
}
    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim().toLowerCase(); // Get the search query, trimmed and in lowercase
        if (query.isEmpty()) {
            // If the search query is empty, show all data
            carTable.setItems(carData);
        } else {
            // Filter the data based on the search query
            ObservableList<Car> filteredList = carData.filtered(car ->
                    car.getCarModel().toLowerCase().contains(query) ||
                            car.getYearModel().toLowerCase().contains(query) ||
                            car.getTransmission().toLowerCase().contains(query) ||
                            car.getColor().toLowerCase().contains(query) ||
                            car.getBodyType().toLowerCase().contains(query)
            );
            carTable.setItems(filteredList);
        }
    }

    private void loadCarData() {
        carData.clear();
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT CarID, AdminID, CarModel, YearModel, Transmission, Color, BodyType, Price, Quantity, Sold, NextRestock, Image FROM Cars";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int carID = rs.getInt("CarID");
                int adminID = rs.getInt("AdminID");
                String carModel = rs.getString("CarModel");
                String yearModel = rs.getString("YearModel");
                String transmission = rs.getString("Transmission");
                String color = rs.getString("Color");
                String bodyType = rs.getString("BodyType");
                String price = rs.getString("Price");
                String quantity = rs.getString("Quantity");
                String sold = rs.getString("Sold");
                LocalDate nextRestock = null; // Initialize with null

                Date nextRestockDate = rs.getDate("NextRestock");
                if (nextRestockDate != null) {
                    nextRestock = nextRestockDate.toLocalDate();
                }

                Image image = null;

                try (InputStream is = rs.getBinaryStream("Image")) {
                    if (is != null) {
                        image = new Image(is);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Car car = new Car(carID, adminID, carModel, yearModel, transmission, color, bodyType, price, quantity, sold, nextRestock, image);
                carData.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while trying to load the car data.");
        }
    }

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                imageView.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load image.");
            }
        }
    }

    @FXML
    private void handleSubmit() {
        String carModel = carModelField.getText();
        String yearModel = yearModelField.getText();
        String transmission = transmissionComboBox.getValue();
        String color = colorComboBox.getValue();
        String bodyType = bodyTypeComboBox.getValue();
        String price = priceField.getText();
        String quantity = quantityField.getText();
        String sold = soldField.getText();
        LocalDate nextRestock = nextRestockField.getValue();
        Image image = imageView.getImage();

        if (carModel.isEmpty() || yearModel.isEmpty() || transmission == null || color == null
                || bodyType == null || price.isEmpty() || quantity.isEmpty() || sold.isEmpty()
                || nextRestock == null || image == null) {
            showAlert("Error", "Please fill in all fields and select options from ComboBoxes.");
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "INSERT INTO Cars (AdminID, CarModel, YearModel, Transmission, Color, BodyType, Price, Quantity, Sold, NextRestock, Image) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, adminID); // Assuming adminID is set somewhere in your application
            stmt.setString(2, carModel);
            stmt.setString(3, yearModel);
            stmt.setString(4, transmission);
            stmt.setString(5, color);
            stmt.setString(6, bodyType);
            stmt.setString(7, price);
            stmt.setString(8, quantity);
            stmt.setString(9, sold);
            stmt.setDate(10, java.sql.Date.valueOf(nextRestock));

            InputStream inputStream = getImageInputStream(image);
            if (inputStream != null) {
                stmt.setBinaryStream(11, inputStream);
            } else {
                stmt.setNull(11, java.sql.Types.BLOB);
            }

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int generatedCarID = rs.getInt(1);
                    resultLabel.setText("Car Model: " + carModel + " added with ID: " + generatedCarID);
                    loadCarData(); // Reload car data to update the table view
                }
            } else {
                showAlert("Error", "Failed to add car model.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while trying to add the car model.");
        }
    }

    @FXML
    private void handleEdit() {
        Car selectedCar = carTable.getSelectionModel().getSelectedItem();
        if (selectedCar != null) {
            carModelField.setText(selectedCar.getCarModel());
            yearModelField.setText(selectedCar.getYearModel());
            transmissionComboBox.setValue(selectedCar.getTransmission());
            colorComboBox.setValue(selectedCar.getColor());
            bodyTypeComboBox.setValue(selectedCar.getBodyType());
            priceField.setText(selectedCar.getPrice());
            quantityField.setText(selectedCar.getQuantity());
            soldField.setText(selectedCar.getSold());
            nextRestockField.setValue(selectedCar.getNextRestock());
            imageView.setImage(selectedCar.getImage());
        } else {
            showAlert("Error", "Please select a car to edit.");
        }
    }

    @FXML
    private void handleSaveEdit() {
        Car selectedCar = carTable.getSelectionModel().getSelectedItem();
        if (selectedCar != null) {
            int carID = selectedCar.getCarID();
            String carModel = carModelField.getText();
            String yearModel = yearModelField.getText();
            String transmission = transmissionComboBox.getValue();
            String color = colorComboBox.getValue();
            String bodyType = bodyTypeComboBox.getValue();
            String price = priceField.getText();
            String quantity = quantityField.getText();
            String sold = soldField.getText();
            LocalDate nextRestock = nextRestockField.getValue();
            Image image = imageView.getImage();

            if (carModel.isEmpty() || yearModel.isEmpty() || transmission == null || color == null
                    || bodyType == null || price.isEmpty() || quantity.isEmpty() || sold.isEmpty()
                    || nextRestock == null || image == null) {
                showAlert("Error", "Please fill in all fields and select options from ComboBoxes.");
                return;
            }

            try (Connection conn = DatabaseUtil.getConnection()) {
                String sql = "UPDATE Cars SET CarModel = ?, YearModel = ?, Transmission = ?, Color = ?, BodyType = ?, " +
                        "Price = ?, Quantity = ?, Sold = ?, NextRestock = ?, Image = ? WHERE CarID = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, carModel);
                stmt.setString(2, yearModel);
                stmt.setString(3, transmission);
                stmt.setString(4, color);
                stmt.setString(5, bodyType);
                stmt.setString(6, price);
                stmt.setString(7, quantity);
                stmt.setString(8, sold);
                stmt.setDate(9, java.sql.Date.valueOf(nextRestock));

                InputStream inputStream = getImageInputStream(image);
                if (inputStream != null) {
                    stmt.setBinaryStream(10, inputStream);
                } else {
                    stmt.setNull(10, java.sql.Types.BLOB);
                }

                stmt.setInt(11, carID);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    resultLabel.setText("Car ID: " + carID + " updated successfully.");
                    loadCarData(); // Reload car data to update the table view
                } else {
                    showAlert("Error", "Failed to update car with ID: " + carID);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "An error occurred while trying to update the car with ID: " + carID);
            }
        } else {
            showAlert("Error", "Please select a car to save the edits.");
        }
    }

    @FXML
    private void handleDelete() {
        Car selectedCar = carTable.getSelectionModel().getSelectedItem();
        if (selectedCar != null) {
            int carID = selectedCar.getCarID();
            try (Connection conn = DatabaseUtil.getConnection()) {
                String sql = "DELETE FROM Cars WHERE CarID = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, carID);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    resultLabel.setText("Car ID: " + carID + " deleted successfully.");
                    loadCarData(); // Reload car data to update the table view
                } else {
                    showAlert("Error", "Failed to delete car with ID: " + carID);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "An error occurred while trying to delete the car with ID: " + carID);
            }
        } else {
            showAlert("Error", "Please select a car to delete.");
        }
    }

    @FXML
    private void handleSell() {
        Car selectedCar = carTable.getSelectionModel().getSelectedItem();
        if (selectedCar != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("SellDialog.fxml"));
                Parent parent = loader.load();
                
                SellDialogController controller = loader.getController();
                controller.setCarID(selectedCar.getCarID());
                controller.setMainController(this);
                
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Sell Car");
                stage.setScene(new Scene(parent));
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "An error occurred while opening the sell dialog.");
            }
        } else {
            showAlert("Error", "Please select a car to sell.");
        }
    }

    public void updateCarQuantity(int carID, int quantitySold) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "UPDATE Cars SET Quantity = Quantity - ?, Sold = Sold + ? WHERE CarID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, quantitySold);
            stmt.setInt(2, quantitySold);
            stmt.setInt(3, carID);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                loadCarData(); // Reload car data to update the table view
                resultLabel.setText("Car ID: " + carID + " sold successfully.");
            } else {
                showAlert("Error", "Failed to sell car with ID: " + carID);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while trying to sell the car with ID: " + carID);
        }
    }

    private InputStream getImageInputStream(Image image) {
        if (image != null) {
            try {
                java.awt.image.BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", os);
                return new ByteArrayInputStream(os.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

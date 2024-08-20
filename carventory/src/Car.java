import javafx.beans.property.*;
import javafx.scene.image.Image;
import java.time.LocalDate;

public class Car {

    private final StringProperty carModel;
    private final StringProperty yearModel;
    private final StringProperty transmission;
    private final StringProperty color;
    private final StringProperty bodyType;
    private final StringProperty price;
    private final StringProperty quantity;
    private final StringProperty sold;
    private final ObjectProperty<LocalDate> nextRestock;
    private final ObjectProperty<Image> image;
    private final IntegerProperty carID;
    private final IntegerProperty adminID;

    public Car(int carID, int adminID, String carModel, String yearModel, String transmission, String color,
               String bodyType, String price, String quantity, String sold, LocalDate nextRestock, Image image) {
        this.carID = new SimpleIntegerProperty(carID);
        this.adminID = new SimpleIntegerProperty(adminID);
        this.carModel = new SimpleStringProperty(carModel);
        this.yearModel = new SimpleStringProperty(yearModel);
        this.transmission = new SimpleStringProperty(transmission);
        this.color = new SimpleStringProperty(color);
        this.bodyType = new SimpleStringProperty(bodyType);
        this.price = new SimpleStringProperty(price);
        this.quantity = new SimpleStringProperty(quantity);
        this.sold = new SimpleStringProperty(sold);
        this.nextRestock = new SimpleObjectProperty<>(nextRestock);
        this.image = new SimpleObjectProperty<>(image);
    }

    // Getters
    public String getCarModel() {
        return carModel.get();
    }

    public String getYearModel() {
        return yearModel.get();
    }

    public String getTransmission() {
        return transmission.get();
    }

    public String getColor() {
        return color.get();
    }

    public String getBodyType() {
        return bodyType.get();
    }

    public String getPrice() {
        return price.get();
    }

    public String getQuantity() {
        return quantity.get();
    }

    public String getSold() {
        return sold.get();
    }

    public LocalDate getNextRestock() {
        return nextRestock.get();
    }

    public Image getImage() {
        return image.get();
    }

    public int getCarID() {
        return carID.get();
    }

    public int getAdminID() {
        return adminID.get();
    }

    // Property methods
    public StringProperty carModelProperty() {
        return carModel;
    }

    public StringProperty yearModelProperty() {
        return yearModel;
    }

    public StringProperty transmissionProperty() {
        return transmission;
    }

    public StringProperty colorProperty() {
        return color;
    }

    public StringProperty bodyTypeProperty() {
        return bodyType;
    }

    public StringProperty priceProperty() {
        return price;
    }

    public StringProperty quantityProperty() {
        return quantity;
    }

    public StringProperty soldProperty() {
        return sold;
    }

    public ObjectProperty<LocalDate> nextRestockProperty() {
        return nextRestock;
    }

    public ObjectProperty<Image> imageProperty() {
        return image;
    }

    public IntegerProperty carIDProperty() {
        return carID;
    }

    public IntegerProperty adminIDProperty() {
        return adminID;
    }
}

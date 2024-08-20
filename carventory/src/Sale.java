import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Sale {
    private int salesID;
    private int carID;
    private int quantitySold;
    private BigDecimal totalAmountSold;
    private LocalDateTime saleDate;

    public Sale(int salesID, int carID, int quantitySold, BigDecimal totalAmountSold, LocalDateTime saleDate) {
        this.salesID = salesID;
        this.carID = carID;
        this.quantitySold = quantitySold;
        this.totalAmountSold = totalAmountSold;
        this.saleDate = saleDate;
    }

    public int getSalesID() {
        return salesID;
    }

    public void setSalesID(int salesID) {
        this.salesID = salesID;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public BigDecimal getTotalAmountSold() {
        return totalAmountSold;
    }

    public void getTotalAmountSold(BigDecimal totalAmountSold) {
        this.totalAmountSold = totalAmountSold;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "salesID=" + salesID +
                ", carID=" + carID +
                ", quantitySold=" + quantitySold +
                ", totalAmountSold=" + totalAmountSold +
                ", saleDate=" + saleDate +
                '}';
    }
}

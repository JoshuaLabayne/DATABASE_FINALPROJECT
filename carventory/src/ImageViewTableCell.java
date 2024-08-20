import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

public class ImageViewTableCell implements Callback<TableColumn<Car, Image>, TableCell<Car, Image>> {
    @Override
    public TableCell<Car, Image> call(TableColumn<Car, Image> param) {
        return new TableCell<Car, Image>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);
                setGraphic(imageView);
            }

            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    imageView.setImage(null);
                } else {
                    imageView.setImage(item);
                }
            }
        };
    }
}

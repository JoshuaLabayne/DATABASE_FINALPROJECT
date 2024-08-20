import java.io.Serializable;

public class BufferedImage implements Serializable {
    private int width;
    private int height;
    private int[][] pixels;

    public BufferedImage(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[width][height];
    }

    public void setPixel(int x, int y, int color) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            pixels[x][y] = color;
        } else {
            throw new IllegalArgumentException("Pixel coordinates out of bounds");
        }
    }

    public int getPixel(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return pixels[x][y];
        } else {
            throw new IllegalArgumentException("Pixel coordinates out of bounds");
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

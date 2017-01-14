package validation;

import model.Pixel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by niewinskip on 2017-01-12.
 */
public class ImageValidator {

    private String path;
    private BufferedImage image;
    private List<Pixel> pixelList;

    public ImageValidator(String path) {
        this.path = "C:\\Users\\niewinskip\\Desktop\\org.png";//path;
        this.pixelList = new ArrayList<>();
    }

    public boolean loadImage() {
        if (this.path == null) {
            return false;
        }
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path));
            if (image == null) {
                return false;
            }
        } catch (IOException ex) {
            return false;
        } catch (Exception e) {
            return false;
        }
        if (image != null) {
            this.image = image;
            return true;
        }
        return false;
    }

    public boolean isMessageValid(String message, int bits) {
        if (bits == 0) {
            return true;
        }
        String binary = stringToBinary(message);
//        final boolean hasAlphaChannel = image.getAlphaRaster() != null;
        if (image.getHeight() * image.getWidth() * bits < binary.length()) {
            return false;
        }
        return true;
    }

    public void createEncryptedImage(String message, int r, int g, int b) {
        String binary = stringToBinary(message);
        if (r == 0 && g == 0 && b == 0) {
            return;
        }
        System.out.println("Encoded: " + binary);
        int index = 0; // next index to be put
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Pixel pixel = new Pixel(new Color(image.getRGB(j, i)));
                int r1 = r, g1 = g, b1 = b;

                int tr = pixel.getR(), tg = pixel.getG(), tb = pixel.getB();

                // Red
                if (index >= binary.length()) {
                    break;
                }

                int iR = 0;
                for (int x = index; x < index + r1; x++) {
                    if (x >= binary.length()) {
                        break;
                    }
                    if (binary.charAt(x) == '1') {
                        pixel.setR(pixel.getR() | (1 << iR));
                    } else {
                        pixel.setR(pixel.getR() & ~(1 << iR));
                    }
                    iR++;
                }
                index += r1;

                int iG = 0;
                for (int x = index; x < index + g1; x++) {
                    if (x >= binary.length()) {
                        break;
                    }
                    if (binary.charAt(x) == '1') {
                        pixel.setG(pixel.getG() | (1 << iG));
                    } else {
                        pixel.setG(pixel.getG() & ~(1 << iG));
                    }
                    iG++;
                }
                index += g1;

                int iB = 0;
                for (int x = index; x < index + b1; x++) {
                    if (x >= binary.length()) {
                        break;
                    }
                    if (binary.charAt(x) == '1') {
                        pixel.setB(pixel.getB() | (1 << iB));
                    } else {
                        pixel.setB(pixel.getB() & ~(1 << iB));
                    }
                    iB++;
                }
                index += b1;

                // Update image
                int r2 = pixel.getR(), g2 = pixel.getG(), b2 = pixel.getB();
//                System.out.println(tr + " " + tg + " " + tb + " >> " + r2 + " " + g2 + " " + b2);
                image.setRGB(j, i, r2 * 65536 + g2 * 256 + b2);
            }
        }
    }

    public void createEncryptedImage(String message, int gray) {
        String binary = stringToBinary(message);
        int index = 0; // next index to be put
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Pixel pixel = new Pixel(new Color(image.getRGB(j, i)));

                int grayLevel = (pixel.getR() + pixel.getG() + pixel.getB()) / 3;
                if (index >= binary.length()) {
                    int gray1 = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                    image.setRGB(j, i, gray1);
                    continue;
                }

                int g1 = gray;

                // Gray
                if (gray + index >= binary.length()) {
                    g1 = binary.length() - index;
                }
                int iG = 0;
                for (int x = index; x < index + g1; x++) {
                    if (binary.charAt(x) == '1') {
                        grayLevel = grayLevel | (1 << iG);
                    } else {
                        grayLevel = grayLevel & ~(1 << iG);
                    }
                    iG++;
                }
                index += g1;
                // Just set grey scale
                int gray1 = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                image.setRGB(j, i, gray1);
            }
        }
    }


    public BufferedImage getImage() {
        return image;
    }

    private String stringToBinary(String msg) {
        byte[] bytes = msg.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return binary.toString();
    }

}

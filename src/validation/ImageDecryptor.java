package validation;

import model.Pixel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by niewinskip on 2017-01-14.
 */
public class ImageDecryptor {

    private String path;
    private BufferedImage image;

    public ImageDecryptor(String path) {
        this.path = "C:\\Users\\niewinskip\\Desktop\\img.png";//path;
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

    public String decrypt(int r, int g, int b, int length) {
        if (r == 0 && g == 0 && b == 0) {
            return "";
        }
        StringBuilder binary = new StringBuilder();
        int max = length * 8;
        int count = 0;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                if (count >= max) {
                    break;
                }
                Pixel pixel = new Pixel(new Color(image.getRGB(j, i)));
                int r1 = r, g1 = g, b1 = b;

                if (count + r1 >= max) {
                    r1 = max - count;
                }
                count += r1;
                int x = 1;
                while (x <= r1) {
                    int bit = getBit(pixel.getR(), x - 1);
                    binary.append(bit);
                    x++;
                }

                if (count >= max) {
                    break;
                }

                if (count + g1 >= max) {
                    g1 = max - count;
                }
                count += g1;
                x = 1;
                while (x <= g1) {
                    int bit = getBit(pixel.getG(), x - 1);
                    binary.append(bit);
                    x++;
                }

                if (count >= max) {
                    break;
                }

                if (count + b1 >= max) {
                    b1 = max - count;
                }
                count += b1;
                x = 1;
                while (x <= b1) {
                    int bit = getBit(pixel.getB(), x - 1);
                    binary.append(bit);
                    x++;
                }
                System.out.println(pixel.getR() + " " + pixel.getG() + " " + pixel.getB() + " " + count);
            }
        }//ABCDefghij123??0 -=9
        System.out.println("Bits:    " + binary.toString());
        String output = "";
        for (int i = 0; i <= binary.toString().length() - 8; i += 8) {
            int k = Integer.parseInt(binary.toString().substring(i, i + 8), 2);
            output += (char) k;
        }
        return output;
    }

    public String decrypt(int gray, int length) {
        if (gray == 0) {
            return "";
        }
        StringBuilder binary = new StringBuilder();
        int max = length * 8;
        int count = 0;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                if (count >= max) {
                    break;
                }
                Pixel pixel = new Pixel(new Color(image.getRGB(j, i)));
                int gray1 = gray;

                if (count + gray1 >= max) {
                    gray1 = max - count;
                }
                count += gray1;
                int x = 1;
                while (x <= gray1) {
                    int bit = getBit(pixel.getR(), x - 1);
                    binary.append(bit);
                    x++;
                }
//                System.out.println(pixel.getR() + " " + pixel.getG() + " " + pixel.getB() + " " + count);
            }
        }//ABCDefghij123??0 -=9
        System.out.println("Bits:    " + binary.toString());
        String output = "";
        for (int i = 0; i <= binary.toString().length() - 8; i += 8) {
            int k = Integer.parseInt(binary.toString().substring(i, i + 8), 2);
            output += (char) k;
        }
        return output;
    }

    int getBit(int n, int k) {
        return (n >> k) & 1;
    }
}

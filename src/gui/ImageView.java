package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by niewinskip on 2017-01-12.
 */
public class ImageView extends JFrame {

    ImageView(BufferedImage bufferedImage, String title) {
        setTitle(title);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bufferedImage, 0, 0, null);
            }
        };

        add(new JScrollPane(panel));
        pack();
        setVisible(true);
        setSize(800, 500);
    }
}

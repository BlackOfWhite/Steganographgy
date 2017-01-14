package gui;

import validation.ImageDecryptor;
import validation.ImageValidator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by niewinskip on 2016-12-28.
 */
public class FileChooser extends JFrame {

    private JTextField filename = new JTextField(), dir = new JTextField(), textField;
    private JButton open, save, bEncrypt, bDecrypt;
    private JComboBox<Integer> jComboBoxGrey, jComboBoxRed, jComboBoxGreen, jComboBoxBlue;
    private static final Integer[] BIT_VALUES = {0, 1, 2, 3, 4, 5, 6, 7, 8};
    private JLabel msgBox, bitsRed, bitsGreen, bitsBlue, bitsGrey;
    private JCheckBox jCheckBoxRGB, jCheckBoxGrey;
    private BufferedImage currImage;

    private static final int DESCRYPTOR_LIMIT = 200;

    public FileChooser() {
        Container cp = getContentPane();

        JPanel pMain = new JPanel();
        pMain.setLayout(new GridLayout(4, 1));
        filename.setEditable(false);
        filename.setText("File name");
        pMain.add(filename);
        dir.setEditable(false);
        dir.setText("Directory");
        pMain.add(dir);
        textField = new JTextField("Enter text here");
        pMain.add(textField);
        msgBox = new JLabel("Logs");
        pMain.add(msgBox);
        cp.add(pMain, BorderLayout.NORTH);

        // Mode
        JPanel pMode = new JPanel();

        JPanel pRGB = new JPanel();
        jCheckBoxRGB = new JCheckBox();
        jCheckBoxRGB.setSelected(true);
        jCheckBoxRGB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jCheckBoxRGB.isSelected()) {
                    jCheckBoxGrey.setSelected(false);
                } else {
                    jCheckBoxGrey.setSelected(true);
                }
            }
        });
        pRGB.add(jCheckBoxRGB);
        bitsRed = new JLabel("Red");
        pRGB.add(bitsRed);
        jComboBoxRed = new JComboBox<>(BIT_VALUES);
        pRGB.add(jComboBoxRed);
        bitsGreen = new JLabel("Green");
        pRGB.add(bitsGreen);
        jComboBoxGreen = new JComboBox<>(BIT_VALUES);
        pRGB.add(jComboBoxGreen);
        bitsBlue = new JLabel("Blue");
        pRGB.add(bitsBlue);
        jComboBoxBlue = new JComboBox<>(BIT_VALUES);
        pRGB.add(jComboBoxBlue);
        pMode.add(pRGB, BorderLayout.NORTH);

        JPanel pGrey = new JPanel();
        jCheckBoxGrey = new JCheckBox();
        jCheckBoxGrey.setSelected(false);
        jCheckBoxGrey.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jCheckBoxGrey.isSelected()) {
                    jCheckBoxRGB.setSelected(false);
                } else {
                    jCheckBoxRGB.setSelected(true);
                }
            }
        });
        pGrey.add(jCheckBoxGrey);
        bitsGrey = new JLabel("Grey");
        pGrey.add(bitsGrey);
        jComboBoxGrey = new JComboBox<>(BIT_VALUES);
        pGrey.add(jComboBoxGrey);
        pMode.add(pGrey, BorderLayout.SOUTH);

        cp.add(pMode);
        //

        JPanel pButtons = new JPanel();
        open = new JButton("Open");
        open.addActionListener(new OpenL());
        pButtons.add(open);
        bEncrypt = new JButton("Encrypt");
        bEncrypt.addActionListener(new EncryptL());
        pButtons.add(bEncrypt);
        bDecrypt = new JButton("Decrypt");
        bDecrypt.addActionListener(new DecryptL());
        pButtons.add(bDecrypt);
        save = new JButton("Save");
        save.addActionListener(new SaveL());
        pButtons.add(save);
        cp.add(pButtons, BorderLayout.AFTER_LAST_LINE);
    }

    class OpenL implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser c = new JFileChooser();
            // Demonstrate "Open" dialog:
            int rVal = c.showOpenDialog(FileChooser.this);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                filename.setText(c.getSelectedFile().getName());
                dir.setText(c.getCurrentDirectory().toString());
            }
            if (rVal == JFileChooser.CANCEL_OPTION) {
                filename.setText("You pressed cancel");
                dir.setText("");
            }
        }
    }

    class SaveL implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (currImage == null) {
                msgBox.setText("No image loaded!");
                return;
            }
            JFileChooser c = new JFileChooser();
            // Demonstrate "Save" dialog:
            int rVal = c.showSaveDialog(FileChooser.this);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                filename.setText(c.getSelectedFile().getName());
                dir.setText(c.getCurrentDirectory().toString());
                File outputfile = new File(c.getCurrentDirectory().toString() + File.pathSeparator + c.getSelectedFile().getName());
                try {
                    ImageIO.write(currImage, "png", new File("C:\\Users\\niewinskip\\Desktop\\img.png"));
                } catch (IOException e1) {
                    msgBox.setText("Failed to save image!");
                }
            }
            if (rVal == JFileChooser.CANCEL_OPTION) {
                filename.setText("You pressed cancel");
                dir.setText("");
            }
        }
    }

    class EncryptL implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (!dir.getText().isEmpty() && !filename.getText().isEmpty()) {
                String message = textField.getText();
                if (message.isEmpty()) {
                    msgBox.setText("Enter the message to encrypt");
                }
                ImageValidator imageValidator = new ImageValidator(dir.getText() + File.separator + filename.getText());
                boolean loaded = imageValidator.loadImage();
                if (!loaded) {
                    msgBox.setText("Image is not valid or file has wrong extension");
                    return;
                }

                // Check mode
                if (jCheckBoxRGB.isSelected()) {
                    ImageConfig.RGB_MODE = true;
                } else {
                    ImageConfig.RGB_MODE = false;
                }

                // Check if message can be put inside image
                int BIT_COUNT;
                if (ImageConfig.RGB_MODE) {
                    BIT_COUNT = Integer.parseInt(jComboBoxRed.getSelectedItem().toString()) + Integer.parseInt(jComboBoxGreen.getSelectedItem().toString()) + Integer.parseInt(jComboBoxBlue.getSelectedItem().toString());
                } else {
                    BIT_COUNT = Integer.parseInt(jComboBoxGrey.getSelectedItem().toString());
                }
                if (!imageValidator.isMessageValid(message, BIT_COUNT)) {
                    msgBox.setText("Invalid numbers of bits, try larger values or another image.");
                    return;
                }

                if (ImageConfig.RGB_MODE) {
                    imageValidator.createEncryptedImage(message, Integer.parseInt(jComboBoxRed.getSelectedItem().toString()),
                            Integer.parseInt(jComboBoxGreen.getSelectedItem().toString()),
                            Integer.parseInt(jComboBoxBlue.getSelectedItem().toString()));
                } else {
                    imageValidator.createEncryptedImage(message, BIT_COUNT);
                }
                currImage = imageValidator.getImage();
                new ImageView(imageValidator.getImage(), filename.getText());
            }
        }
    }

    class DecryptL implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (!dir.getText().isEmpty() && !filename.getText().isEmpty()) {
                ImageDecryptor imageDecryptor = new ImageDecryptor(dir.getText() + File.separator + filename.getText());
                boolean loaded = imageDecryptor.loadImage();
                if (!loaded) {
                    msgBox.setText("Image is not valid or file has wrong extension");
                    return;
                }

                String message = "";
                if (jCheckBoxRGB.isSelected()) {
                    message = imageDecryptor.decrypt(Integer.parseInt(jComboBoxRed.getSelectedItem().toString()), Integer.parseInt(jComboBoxGreen.getSelectedItem().toString()), Integer.parseInt(jComboBoxBlue.getSelectedItem().toString())
                            , DESCRYPTOR_LIMIT);
                } else {
                    message = imageDecryptor.decrypt(Integer.parseInt(jComboBoxGrey.getSelectedItem().toString()),DESCRYPTOR_LIMIT);
                }
                msgBox.setText(message);
            }
        }
    }

    public void run(JFrame frame, int width, int height) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setVisible(true);
        frame.setTitle("Stenography");
    }
}

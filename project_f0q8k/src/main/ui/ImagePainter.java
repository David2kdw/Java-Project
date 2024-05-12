package ui;/*
 * Package:ui
 * Author:Lenovo
 * Created:2023/11/18  15:38
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// represent a component which paints images
public class ImagePainter extends JPanel {
    String imageLocation;
    BufferedImage image;

    // EFFECTS: constructs an ImagePainter
    public ImagePainter(String imageLocation) {
        try {
            File file = new File(imageLocation);
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setSize(200, 400);
    }

    // EFFECTS: set the new location, fetch the new image
    public void setImageLocation(String imageLocation) {
        System.out.println("From image printer: " + imageLocation);
        this.imageLocation = imageLocation;
        try {
            File file = new File(imageLocation);
            image = ImageIO.read(file);
        } catch (IOException e) {
            System.out.println("File not found! (in set location)");
            e.printStackTrace();
        }
    }

    // EFFECTS: paint the image with the original ratio
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("From imagePainter: " + imageLocation);

        double imgW = image.getWidth();
        double imgH = image.getHeight();
        double fitW = getWidth();
        double fitH = getWidth() * imgH / imgW;
        if (fitH > getHeight()) {
            fitH = getHeight();
            fitW = getHeight() * (imgW / imgH);
        }
        int fitX = (int) ((getWidth() - fitW) / 2);
        int fitY = (int) ((getHeight() - fitH) / 2);
        g.drawImage(image, fitX, fitY, (int)fitW, (int)fitH, null);
    }
}

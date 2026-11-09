package javaapplication3;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LogoUtils {
    public static Image crearImagenLogo(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, size, size);

        try {
            ImageIcon icono = new ImageIcon("src/imagen/logo.jpg");
            if (icono.getIconWidth() > 0) {
                BufferedImage logoBuf = new BufferedImage(icono.getIconWidth(), icono.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D gLogo = logoBuf.createGraphics();
                gLogo.drawImage(icono.getImage(), 0, 0, null);
                gLogo.dispose();

                for (int i = 0; i < logoBuf.getWidth(); i++) {
                    for (int j = 0; j < logoBuf.getHeight(); j++) {
                        int rgba = logoBuf.getRGB(i, j);
                        Color col = new Color(rgba, true);
                        Color rojo = new Color(col.getRed(), 0, 0, col.getAlpha());
                        logoBuf.setRGB(i, j, rojo.getRGB());
                    }
                }
                g2.drawImage(logoBuf, 0, 0, size, size, null);
            } else {
                g2.setColor(new Color(210, 35, 42));
                g2.fillOval(4, 4, size - 8, size - 8);
            }
        } catch (Exception e) {
            g2.setColor(new Color(210, 35, 42));
            g2.fillOval(4, 4, size - 8, size - 8);
        }
        g2.dispose();
        return img;
    }
}
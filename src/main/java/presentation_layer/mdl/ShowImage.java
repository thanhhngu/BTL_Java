package presentation_layer.mdl;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ShowImage extends JPanel {

    private static JLabel lblImage = new JLabel();

    public ShowImage() {
    }

    public static JPanel showImg(String imagePath) {
        loadImage(imagePath);

        JPanel pn = new JPanel(new BorderLayout());
        pn.add(lblImage, BorderLayout.CENTER);

        return pn;
    }

    private static void loadImage(String imagePath) {
        // load ảnh từ resources (src/main/resources)
        URL url = ShowImage.class.getClassLoader().getResource(imagePath);

        if (url != null) {
            ImageIcon icon = new ImageIcon(url);

            // lấy kích thước gốc của ảnh
            int width = icon.getIconWidth();
            int height = icon.getIconHeight();

            int maxW = 400, maxH = 400;
            if (width > maxW || height > maxH) {
                Image img = icon.getImage().getScaledInstance(maxW, maxH, Image.SCALE_SMOOTH);
                icon = new ImageIcon(img);
                width = icon.getIconWidth();
                height = icon.getIconHeight();
            }

            lblImage.setIcon(icon);
            lblImage.setPreferredSize(new Dimension(width, height));
        } else {
            lblImage.setText("Không tìm thấy ảnh: " + imagePath);
        }
    }
}
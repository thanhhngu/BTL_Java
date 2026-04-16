package presentation_layer.mdl;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

class ShowImage extends JFrame {

    private JLabel lblImage;

    public ShowImage(String imagePath) {
        super("Show Image");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        lblImage = new JLabel();
        add(lblImage);

        loadImage(imagePath);

        setVisible(true);
    }

    private void loadImage(String imagePath) {
        // load ảnh từ resources (src/main/resources)
        URL url = ShowImage.class.getClassLoader().getResource(imagePath);

        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(img));
        } else {
            lblImage.setText("Không tìm thấy ảnh: " + imagePath);
        }
    }

    // test nhanh
    public static void main(String[] args) {
        new ShowImage("images/P001.png");
    }
}
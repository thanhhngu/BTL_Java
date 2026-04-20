import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class mainTestAnh {
    public static void main(String[] args) {

        // tạo frame
        JFrame frame = new JFrame("Test Image");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        // label để hiển thị ảnh
        JLabel lblImage = new JLabel();

        // giả lập path lấy từ database
        String imagePath = "images/P001.png";

        // load ảnh từ resources
        URL url = mainTestAnh.class.getClassLoader().getResource(imagePath);

        if (url != null) {
            ImageIcon icon = new ImageIcon(url);

            // resize ảnh
            Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);

            lblImage.setIcon(new ImageIcon(img));
        } else {
            System.out.println("Không tìm thấy ảnh: " + imagePath);
        }

        // add vào frame
        frame.add(lblImage);

        // hiển thị
        frame.setVisible(true);
    }
}
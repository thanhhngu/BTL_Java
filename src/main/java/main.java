import javax.swing.SwingUtilities;
import presentation_layer.loginFrame;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            loginFrame login = new loginFrame();
            login.setVisible(true);
            
        });
    }
}
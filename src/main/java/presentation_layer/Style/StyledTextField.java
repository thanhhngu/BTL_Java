package presentation_layer.Style;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class StyledTextField extends JTextField {
    public StyledTextField(String text) {
        super(text);
        this.setPreferredSize(new Dimension(300, 38));
        this.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        this.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 210, 210), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
    }
}

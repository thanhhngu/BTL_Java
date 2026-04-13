package presentation_layer.mdl;

import javax.swing.*;
import java.awt.*;

public class RatioSplitPanel extends JPanel {

    public RatioSplitPanel(JPanel mainPanel, JPanel sidePanel) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Panel chính (2/3)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 2.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 10); // Khoảng cách bên phải
        add(mainPanel, gbc);

        // Panel phụ (1/3)
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0); // Không có insets bên phải
        add(sidePanel, gbc);
    }
}

package presentation_layer.mdl;

import javax.swing.*;
import java.awt.*;

public class SideBarr extends JPanel {

    public SideBarr(String[] buttons, SidebarCallback callback) {
        initUI(buttons, callback);
    }

    private void initUI(String[] buttons, SidebarCallback callback) {
        setLayout(new GridLayout(buttons.length, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setPreferredSize(new Dimension(220, 0));

        for (String text : buttons) {
            JButton btn = createMenuButton(text);
            btn.addActionListener(e -> callback.onMenuClick(text)); // gọi callback
            add(btn);
        }
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        return button;
    }
}
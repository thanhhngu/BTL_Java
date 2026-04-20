package presentation_layer.Customer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class SideBarPanel extends JPanel {

    private JButton btnHome;
    private JButton btnOrder;
    private JButton btnHistory;
    private JButton btnAccount;

    public SideBarPanel() {
        initUI();
    }

    private void initUI() {
        setPreferredSize(new Dimension(210, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 15, 20, 15));
        setBackground(new Color(245, 245, 245));

        btnHome = createMenuButton("HOME");
        btnOrder = createMenuButton("ORDER");
        btnHistory = createMenuButton("HISTORY");
        btnAccount = createMenuButton("ACCOUNT");

        add(btnHome);
        add(Box.createVerticalStrut(20));
        add(btnOrder);
        add(Box.createVerticalStrut(20));
        add(btnHistory);
        add(Box.createVerticalStrut(20));
        add(btnAccount);
        add(Box.createVerticalGlue());
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 17));
        button.setFocusPainted(false);
        button.setBackground(new Color(214, 228, 240));
        button.setMaximumSize(new Dimension(180, 90));
        button.setPreferredSize(new Dimension(180, 70));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }

    public JButton getBtnHome() {
        return btnHome;
    }

    public JButton getBtnOrder() {
        return btnOrder;
    }

    public JButton getBtnHistory() {
        return btnHistory;
    }

    public JButton getBtnAccount() {
        return btnAccount;
    }
}
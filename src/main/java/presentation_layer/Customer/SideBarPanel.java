package presentation_layer.Customer;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class SideBarPanel extends JPanel {
    private JButton btnHome;
    private JButton btnCategory;
    private JButton btnOrder;
    private JButton btnHistory;
    private JButton btnAccount;

    public SideBarPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new GridLayout(5, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setPreferredSize(new Dimension(220, 0));

        btnHome = createMenuButton("Home");
        btnCategory = createMenuButton("Category");
        btnOrder = createMenuButton("Order");
        btnHistory = createMenuButton("History");
        btnAccount = createMenuButton("Account");

        add(btnHome);
        add(btnCategory);
        add(btnOrder);
        add(btnHistory);
        add(btnAccount);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        return button;
    }

    public JButton getBtnHome() {
        return btnHome;
    }

    public JButton getBtnCategory() {
        return btnCategory;
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
package presentation_layer.mdl;

import presentation_layer.Login.loginFrame;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {
    private JLabel lblUsername;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnLogout;

    private String username;

    public HeaderPanel(String un, Component parent) {
        this.username = un;
        JPanel header = createHeader();
        add(header);
        btnLogout.addActionListener(e -> logout(parent));

    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout(20, 10));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblUsername = new JLabel("Xin chào, " + username);
        lblUsername.setFont(new Font("Arial", Font.BOLD, 18));
        leftPanel.add(lblUsername);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        txtSearch = new JTextField(28);
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 16));
        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setFont(new Font("Arial", Font.BOLD, 14));

        centerPanel.add(txtSearch);
        centerPanel.add(btnSearch);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLogout = new JButton("Đăng xuất");
        btnLogout.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogout.setPreferredSize(new Dimension(130, 40));
        rightPanel.add(btnLogout);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(centerPanel, BorderLayout.CENTER);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private void logout(Component parent) {
        int confirm = JOptionPane.showConfirmDialog(
                parent,
                "Bạn có chắc muốn đăng xuất không?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            Window window = SwingUtilities.getWindowAncestor(parent);
            if (window != null) {
                window.dispose(); // đóng frame hiện tại
            }

            new loginFrame().setVisible(true);
        }
    }
}

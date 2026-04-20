package presentation_layer;

import model_layer.account;
import service_layer.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class loginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cbRole;
    private JButton btnLogin;
    private JButton btnRegister;
    private JButton btnForgot;

    private AuthService authService;

    public loginFrame() {
        authService = new AuthService();
        initUI();
    }

    private void initUI() {
        setTitle("Login");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Panel nền chính
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(62, 67, 157));
        add(mainPanel);

        // Card form ở giữa
        JPanel formPanel = new JPanel();
        formPanel.setPreferredSize(new Dimension(700, 470));
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Tiêu đề
        JLabel lblTitle = new JLabel("LOGIN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(new Color(40, 60, 90));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubTitle = new JLabel("Đăng nhập vào hệ thống", SwingConstants.CENTER);
        lblSubTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubTitle.setForeground(new Color(120, 120, 120));
        lblSubTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(lblTitle);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(lblSubTitle);
        formPanel.add(Box.createVerticalStrut(30));

        // Username
        JLabel lblUsername = createLabel("USERNAME");
        txtUsername = createTextField();

        // Password
        JLabel lblPassword = createLabel("PASSWORD");
        txtPassword = createPasswordField();

        // Role
        JLabel lblRole = createLabel("ROLE");
        cbRole = new JComboBox<>(new String[]{"CUSTOMER", "SHOP", "SHIPPER"});
        cbRole.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbRole.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cbRole.setPreferredSize(new Dimension(300, 40));
        cbRole.setBackground(Color.WHITE);

        formPanel.add(lblUsername);
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(txtUsername);
        formPanel.add(Box.createVerticalStrut(18));

        formPanel.add(lblPassword);
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(txtPassword);
        formPanel.add(Box.createVerticalStrut(18));

        formPanel.add(lblRole);
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(cbRole);
        formPanel.add(Box.createVerticalStrut(25));

        // Panel nút phụ
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        actionPanel.setBackground(Color.WHITE);

        btnForgot = new JButton("Quên mật khẩu");
        styleSecondaryButton(btnForgot);

        btnRegister = new JButton("Đăng ký");
        styleSecondaryButton(btnRegister);

        actionPanel.add(btnForgot);
        actionPanel.add(btnRegister);

        formPanel.add(actionPanel);
        formPanel.add(Box.createVerticalStrut(30));

        // Nút login
        btnLogin = new JButton("LOGIN");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnLogin.setBackground(new Color(52, 104, 192));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        formPanel.add(btnLogin);

        mainPanel.add(formPanel);

        // Sự kiện
        btnLogin.addActionListener(e -> handleLogin());

        btnRegister.addActionListener(e -> {
            new RegisterChooseRoleFrame().setVisible(true);
            dispose();
        });

        btnForgot.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Chức năng quên mật khẩu sẽ phát triển sau")
        );
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(70, 70, 70));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        textField.setPreferredSize(new Dimension(300, 40));
        return textField;
    }

    private JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setPreferredSize(new Dimension(300, 40));
        return passwordField;
    }

    private void styleSecondaryButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setBackground(new Color(245, 245, 245));
        button.setForeground(new Color(60, 60, 60));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String role = cbRole.getSelectedItem().toString();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ username và password");
            return;
        }

        account acc = authService.login(username, password, role);

        if (acc == null) {
            JOptionPane.showMessageDialog(this, "Sai username, password hoặc role");
            return;
        }

        JOptionPane.showMessageDialog(this, "Đăng nhập thành công với role: " + acc.getRole());

        switch (acc.getRole().toUpperCase()) {
            case "CUSTOMER":
                new customerFrame(acc.getUsername(),acc.getCustomerID()).setVisible(true);
                break;
            case "SHOP":
                new shopFrame(acc.getUsername()).setVisible(true);
                break;
            case "SHIPPER":
                new shipperFrame(acc.getUsername()).setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Role không hợp lệ");
                return;
        }

        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new loginFrame().setVisible(true));
    }
}
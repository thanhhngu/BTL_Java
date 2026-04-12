package presentation_layer.Login;

import service_layer.AuthService;

import javax.swing.*;
import java.awt.*;

public class RegisterShipperFrame extends JFrame {
    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtCompanyName;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnRegister;
    private JButton btnBack;

    private AuthService authService;

    public RegisterShipperFrame() {
        authService = new AuthService();
        initUI();
    }

    private void initUI() {
        setTitle("Register Shipper");
        setSize(500, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        JLabel lblTitle = new JLabel("shipper");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setForeground(new Color(45, 132, 197));
        lblTitle.setBounds(155, 30, 200, 40);
        add(lblTitle);

        addLabel("NAME", 70, 100);
        txtName = addTextField(70, 125);

        addLabel("PHONE", 70, 170);
        txtPhone = addTextField(70, 195);

        addLabel("COMPANY NAME", 70, 240);
        txtCompanyName = addTextField(70, 265);

        addLabel("USERNAME", 70, 310);
        txtUsername = addTextField(70, 335);

        addLabel("PASSWORD", 70, 380);
        txtPassword = new JPasswordField();
        txtPassword.setBounds(70, 405, 320, 35);
        add(txtPassword);

        btnRegister = new JButton("REGIS");
        btnRegister.setFont(new Font("Arial", Font.BOLD, 18));
        btnRegister.setBounds(170, 470, 140, 40);
        add(btnRegister);

        btnBack = new JButton("Back");
        btnBack.setBounds(330, 470, 80, 40);
        add(btnBack);

        btnRegister.addActionListener(e -> handleRegister());
        btnBack.addActionListener(e -> {
            new RegisterChooseRoleFrame().setVisible(true);
            dispose();
        });
    }

    private void addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 200, 20);
        add(lbl);
    }

    private JTextField addTextField(int x, int y) {
        JTextField txt = new JTextField();
        txt.setBounds(x, y, 320, 35);
        add(txt);
        return txt;
    }

    private void handleRegister() {
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        String companyName = txtCompanyName.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (name.isEmpty() || phone.isEmpty() || companyName.isEmpty()
                || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
            return;
        }

        String result = authService.registerShipper(name, phone, companyName, username, password);

        if ("SUCCESS".equals(result)) {
            JOptionPane.showMessageDialog(this, "Đăng ký shipper thành công");
            new loginFrame().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, result);
        }
    }
}
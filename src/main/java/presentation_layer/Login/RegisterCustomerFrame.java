package presentation_layer.Login;

import service_layer.AuthService;

import javax.swing.*;
import java.awt.*;

public class RegisterCustomerFrame extends JFrame {
    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtBirthday;
    private JComboBox<String> cbGender;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnRegister;
    private JButton btnBack;

    private AuthService authService;

    public RegisterCustomerFrame() {
        authService = new AuthService();
        initUI();
    }

    private void initUI() {
        setTitle("Register Customer");
        setSize(500, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        JLabel lblTitle = new JLabel("customer");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setForeground(new Color(45, 132, 197));
        lblTitle.setBounds(150, 30, 200, 40);
        add(lblTitle);

        addLabel("NAME", 70, 100);
        txtName = addTextField(70, 125);

        addLabel("PHONE", 70, 170);
        txtPhone = addTextField(70, 195);

        addLabel("BIRTHDAY (yyyy-mm-dd)", 70, 240);
        txtBirthday = addTextField(70, 265);

        addLabel("GENDER", 70, 310);
        cbGender = new JComboBox<>(new String[]{"1 - Nam", "0 - Nữ"});
        cbGender.setBounds(70, 335, 320, 35);
        add(cbGender);

        addLabel("USERNAME", 70, 380);
        txtUsername = addTextField(70, 405);

        addLabel("PASSWORD", 70, 450);
        txtPassword = new JPasswordField();
        txtPassword.setBounds(70, 475, 320, 35);
        add(txtPassword);

        btnRegister = new JButton("REGIS");
        btnRegister.setFont(new Font("Arial", Font.BOLD, 18));
        btnRegister.setBounds(170, 530, 140, 40);
        add(btnRegister);

        btnBack = new JButton("Back");
        btnBack.setBounds(330, 530, 80, 40);
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
        String birthday = txtBirthday.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        int gender = cbGender.getSelectedIndex() == 0 ? 1 : 0;

        if (name.isEmpty() || phone.isEmpty() || birthday.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
            return;
        }

        String result = authService.registerCustomer(name, phone, gender, birthday, username, password);

        if ("SUCCESS".equals(result)) {
            JOptionPane.showMessageDialog(this, "Đăng ký customer thành công");
            new loginFrame().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, result);
        }
    }
}
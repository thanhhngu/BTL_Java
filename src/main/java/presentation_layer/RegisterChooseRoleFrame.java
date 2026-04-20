package presentation_layer;

import javax.swing.*;
import java.awt.*;

public class RegisterChooseRoleFrame extends JFrame {
    private JButton btnCustomer;
    private JButton btnShop;
    private JButton btnShipper;
    private JButton btnBack;

    public RegisterChooseRoleFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("Register");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        JLabel lblTitle = new JLabel("register");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 38));
        lblTitle.setBounds(150, 50, 220, 50);
        add(lblTitle);

        btnCustomer = new JButton("customer");
        btnCustomer.setFont(new Font("Arial", Font.BOLD, 18));
        btnCustomer.setBounds(30, 180, 120, 120);
        btnCustomer.setBackground(new Color(45, 132, 197));
        btnCustomer.setForeground(Color.WHITE);
        add(btnCustomer);

        btnShop = new JButton("shop");
        btnShop.setFont(new Font("Arial", Font.BOLD, 18));
        btnShop.setBounds(180, 180, 120, 120);
        btnShop.setBackground(new Color(45, 132, 197));
        btnShop.setForeground(Color.WHITE);
        add(btnShop);

        btnShipper = new JButton("shipper");
        btnShipper.setFont(new Font("Arial", Font.BOLD, 18));
        btnShipper.setBounds(330, 180, 120, 120);
        btnShipper.setBackground(new Color(45, 132, 197));
        btnShipper.setForeground(Color.WHITE);
        add(btnShipper);

        btnBack = new JButton("Quay lại");
        btnBack.setBounds(190, 370, 110, 35);
        add(btnBack);

        btnCustomer.addActionListener(e -> {
            new RegisterCustomerFrame().setVisible(true);
            dispose();
        });

        btnShop.addActionListener(e -> {
            new RegisterShopFrame().setVisible(true);
            dispose();
        });

        btnShipper.addActionListener(e -> {
            new RegisterShipperFrame().setVisible(true);
            dispose();
        });

        btnBack.addActionListener(e -> {
            new loginFrame().setVisible(true);
            dispose();
        });
    }
}
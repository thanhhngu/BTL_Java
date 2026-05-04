package presentation_layer.mdl;

import model_layer.account;
import presentation_layer.Style.StyledTextField;
import repository_layer.accountRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

import static presentation_layer.mdl.HandleAction.*;
import static presentation_layer.mdl.ShowImage.showImg;

public class AccountPanel extends JPanel {
    String id;
    account acc;

    public AccountPanel(String ID) {
        this.id = ID;
        System.out.println(id);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        initUI();
    }

    public void initUI(){
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        initMain(mainPanel);
        JPanel sidePanel = new JPanel(new BorderLayout());
        initControl(sidePanel, mainPanel);

        sidePanel.setPreferredSize(new Dimension(300, 0));

        RatioSplitPanel splitPanel = new RatioSplitPanel(mainPanel, sidePanel);
        add(splitPanel, BorderLayout.CENTER);
    }

    public void initMain(JPanel mainPanel) {
        accountRepository repo = new accountRepository();
        acc = repo.getInfo(id);

        String displayName = "";
        String displayPhone = "";
        String displayEmail = "";
        String displayAddress = "";

        if (acc != null) {
            if (acc.getShop() != null) {
                displayName = acc.getShop().getName();
                displayPhone = acc.getShop().getPhone();
                displayEmail = acc.getShop().getEmail();
                displayAddress = acc.getShop().getAddress();
            } else if (acc.getCustomer() != null) {
                displayName = acc.getCustomer().getName();
                displayPhone = acc.getCustomer().getPhone();
            } else if (acc.getShipper() != null) {
                displayName = acc.getShipper().getName();
                displayPhone = acc.getShipper().getPhone();
                displayAddress = acc.getShipper().getCompanyName();
            }
        }

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));//chieu doc
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));
        // text header
        JLabel lblTitle = new JLabel("HỒ SƠ CÁ NHÂN (" + (acc != null ? acc.getRole() : "UNKNOWN") + ")");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(lblTitle);
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));

        // info
        JLabel lblPersonalInfo = new JLabel("Thông tin tài khoản: " + (acc != null ? acc.getUsername() : ""));
        lblPersonalInfo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPersonalInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(lblPersonalInfo);
        mainContent.add(Box.createRigidArea(new Dimension(0, 15)));
        //avatar
        JPanel personalPanel = new JPanel(new BorderLayout(50, 0));
        personalPanel.setBackground(Color.WHITE);
        personalPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        //create form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        formPanel.add(new JLabel("Họ và tên"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        StyledTextField txtName = new StyledTextField(displayName);
        formPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2;
        formPanel.add(new JLabel("Số điện thoại"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        StyledTextField txtPhone = new StyledTextField(displayPhone);
        formPanel.add(txtPhone, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.2;
        formPanel.add(new JLabel("Email"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        StyledTextField txtEmail = new StyledTextField(displayEmail);
        if (displayEmail.isEmpty()) {
            txtEmail.setText("Không hỗ trợ cho tài khoản này");
            txtEmail.setBackground(new Color(240, 242, 245));
            txtEmail.setEditable(false);
        }
        formPanel.add(txtEmail, gbc);

        personalPanel.add(formPanel, BorderLayout.CENTER);

        // create Avatar
        JPanel avatarPanel = new JPanel();
        avatarPanel.setLayout(new BoxLayout(avatarPanel, BoxLayout.Y_AXIS));
        avatarPanel.setBackground(Color.WHITE);
        JPanel imgPanel = showImg("images/P001.png");

        Dimension avatarSize = new Dimension(120, 120);
        if (imgPanel != null) {
            imgPanel.setPreferredSize(avatarSize);
            imgPanel.setMinimumSize(avatarSize);
            imgPanel.setMaximumSize(avatarSize);

            imgPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            avatarPanel.add(imgPanel);
        }

        //Style button - btn change avatar
        avatarPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton btnChangeAvatar = new JButton("Đổi ảnh");
        btnChangeAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnChangeAvatar.setBackground(Color.WHITE);
        btnChangeAvatar.setFocusPainted(false);
        avatarPanel.add(btnChangeAvatar);

        btnChangeAvatar.addActionListener(e -> handleChangeAva());

        personalPanel.add(avatarPanel, BorderLayout.EAST);

        mainContent.add(personalPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));
        mainContent.add(new JSeparator());//line
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));

        // --- KHU VỰC 2: ĐỊA CHỈ ---
        JLabel lblAddressTitle = new JLabel("Địa chỉ / Đơn vị");
        lblAddressTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblAddressTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(lblAddressTitle);
        mainContent.add(Box.createRigidArea(new Dimension(0, 15)));

        StyledTextField txtAddress = new StyledTextField(displayAddress);
        txtAddress.setMaximumSize(new Dimension(800, 38));
        txtAddress.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainContent.add(txtAddress);
        mainContent.add(Box.createRigidArea(new Dimension(0, 40)));

        //place button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        //style button - btn save
        JButton btnSave = new JButton("Lưu thay đổi");
        btnSave.addActionListener(e -> handleChangeInf());
        bottomPanel.add(btnSave);
        mainContent.add(bottomPanel);

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

    }

    public void initControl(JPanel sidePanel, JPanel mainPanel) {
        JButton btnInfo = new JButton("View Info");
        JButton btnChangePassword = new JButton("Change Password");


        JPanel controlPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        controlPanel.add(btnInfo);
        controlPanel.add(btnChangePassword);

        sidePanel.add(controlPanel, BorderLayout.NORTH);


        btnInfo.addActionListener(e -> {
            mainPanel.removeAll();
            initMain(mainPanel);
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        btnChangePassword.addActionListener(e -> handleChangePassword(mainPanel, acc));
    }
}

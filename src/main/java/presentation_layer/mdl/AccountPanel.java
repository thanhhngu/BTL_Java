package presentation_layer.mdl;

import model_layer.account;
import repository_layer.accountRepository;

import javax.swing.*;
import java.awt.*;

import static presentation_layer.mdl.HandleAction.handleChangePassword;

public class AccountPanel extends JPanel {
    String id;
    account acc;

    public AccountPanel(String ID) {
        this.id = ID;
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

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 20);
        Font valueFont = new Font("Segoe UI", Font.BOLD, 20);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        addRow(infoPanel, gbc, "Account ID:", acc.getAccountID(), labelFont, valueFont, 0);
        addRow(infoPanel, gbc, "Username:", acc.getUsername(), labelFont, valueFont, 1);
        addRow(infoPanel, gbc, "Role:", acc.getRole(), labelFont, valueFont, 2);
        addRow(infoPanel, gbc, "RoleID:", acc.getRoleID(), labelFont, valueFont, 3);

        mainPanel.add(infoPanel, BorderLayout.CENTER);
    }
    private void addRow(JPanel panel, GridBagConstraints gbc, String label, String value, Font lFont, Font vFont, int y) {
        gbc.gridx = 0; gbc.gridy = y;
        JLabel lbl = new JLabel(label);
        lbl.setFont(lFont);
        lbl.setForeground(Color.DARK_GRAY);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        JLabel val = new JLabel(value);
        val.setFont(vFont);
        panel.add(val, gbc);
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

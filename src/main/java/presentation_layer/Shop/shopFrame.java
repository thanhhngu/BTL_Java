package presentation_layer.Shop;

import presentation_layer.Shop.MenuPanel.*;
import presentation_layer.mdl.AccountPanel;
import presentation_layer.mdl.HeaderPanel;
import presentation_layer.mdl.SideBarr;
import presentation_layer.itf.SidebarCallback;

import javax.swing.*;
import java.awt.*;

public class shopFrame extends JFrame implements SidebarCallback {
    private String id = "S001";

    private JPanel content = new JPanel(new BorderLayout());
    private JPanel mainPanel = new JPanel(new BorderLayout());
    private SideBarr sb;

    private HomePanel homePanel;
    private ConfirmPanel confirmPanel;
    private StatusPanel statusPanel;
    private RevenuePanel revenuePanel;
    private AccountPanel accountPanel = new AccountPanel(id);

    public shopFrame(String username,String id) {
        this.id = id;

        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Sidebar
        sb = new SideBarr(new String[]{"Home", "Confirm", "Status", "Revenue", "Account"}, this);
        mainPanel.add(sb, BorderLayout.WEST);

        // Header
        JPanel header = new HeaderPanel(username);
        mainPanel.add(header, BorderLayout.NORTH);

        // Content
        homePanel = new HomePanel(id);
        confirmPanel = new ConfirmPanel(id);
        statusPanel = new StatusPanel(id);
        revenuePanel = new RevenuePanel(id);

        content.add(homePanel, BorderLayout.CENTER);
        mainPanel.add(content, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);

    }


    @Override
    public void onMenuClick(String cmd) {
        content.removeAll();

        switch (cmd) {
            case "Home":
                content.add(homePanel, BorderLayout.CENTER);
                break;
            case "Confirm":
                content.add(confirmPanel, BorderLayout.CENTER);
                break;
            case "Status":
                content.add(statusPanel, BorderLayout.CENTER);
                break;
            case "Revenue":
                content.add(revenuePanel, BorderLayout.CENTER);
                break;
            case "Account":
                content.add(accountPanel, BorderLayout.CENTER);
                break;
        }

        content.revalidate();
        content.repaint();
    }

    public static void main(String[] args) {
        new shopFrame("Shop Owner", "S001").setVisible(true);
    }
}
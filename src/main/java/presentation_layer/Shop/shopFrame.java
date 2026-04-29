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
    private HeaderPanel header;
    private AccountPanel accountPanel = new AccountPanel(id);

    public shopFrame(String username,String id) {
        this.id = id;

        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Sidebar
        sb = new SideBarr(new String[]{"Home", "Confirm", "Status", "Revenue", "Account"}, this);
        mainPanel.add(sb, BorderLayout.WEST);

        homePanel = new HomePanel(id);
        confirmPanel = new ConfirmPanel(id);
        statusPanel = new StatusPanel(id);
        revenuePanel = new RevenuePanel(id);

        // Header
        header = new HeaderPanel(username, homePanel.getTable(), homePanel.getModel());
        mainPanel.add(header, BorderLayout.NORTH);

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
                header.setTarget(homePanel.getTable(), homePanel.getModel());
                break;
            case "Confirm":
                content.add(confirmPanel, BorderLayout.CENTER);
                header.setTarget(confirmPanel.getTable(), confirmPanel.getModel());
                break;
            case "Status":
                content.add(statusPanel, BorderLayout.CENTER);
                header.setTarget(statusPanel.getTable(), statusPanel.getModel());
                break;
            case "Revenue":
                content.add(revenuePanel, BorderLayout.CENTER);
                header.setTarget(revenuePanel.getTable(), revenuePanel.getModel());
                break;
            case "Account":
                content.add(accountPanel, BorderLayout.CENTER);
                header.setTarget(null, null);
                break;
        }

        content.revalidate();
        content.repaint();
    }

    public static void main(String[] args) {
        new shopFrame("Shop Owner", "S001").setVisible(true);
    }
}
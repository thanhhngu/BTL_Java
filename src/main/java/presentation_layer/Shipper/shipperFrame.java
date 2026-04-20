package presentation_layer.Shipper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import presentation_layer.Login.loginFrame;
import presentation_layer.Shipper.MenuPanel.FreePickPanel;
import presentation_layer.Shipper.MenuPanel.OrdersPanel;
import presentation_layer.Shipper.MenuPanel.WorkPerPanel;
import presentation_layer.Shop.MenuPanel.ConfirmPanel;
import presentation_layer.Shop.MenuPanel.HomePanel;
import presentation_layer.Shop.MenuPanel.RevenuePanel;
import presentation_layer.Shop.MenuPanel.StatusPanel;
import presentation_layer.Shop.shopFrame;
import presentation_layer.itf.SidebarCallback;
import presentation_layer.mdl.AccountPanel;
import presentation_layer.mdl.HeaderPanel;
import presentation_layer.mdl.SideBarr;

public class shipperFrame extends JFrame implements SidebarCallback {
    private String id = "SH003";

    private JPanel content = new JPanel(new BorderLayout());
    private JPanel mainPanel = new JPanel(new BorderLayout());
    private SideBarr sb;

    private FreePickPanel freePickPanel;
    private OrdersPanel ordersPanel;
    private WorkPerPanel workPerPanel;

    private AccountPanel accountPanel = new AccountPanel();

    public shipperFrame(String un, String id) {
        this.id = id;

        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        sb = new SideBarr(new String[]{"Orders", "FreePick", "WorkPerformance", "Account"}, this);
        mainPanel.add(sb, BorderLayout.WEST);

        JPanel header = new HeaderPanel(un);
        mainPanel.add(header, BorderLayout.NORTH);


        ordersPanel = new OrdersPanel(id);
        freePickPanel = new FreePickPanel(id);
        workPerPanel = new WorkPerPanel(id);

        content.add(ordersPanel, BorderLayout.CENTER);
        mainPanel.add(content, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    @Override
    public void onMenuClick(String cmd) {
        content.removeAll();

        switch (cmd) {
            case "Orders":
                content.add(ordersPanel, BorderLayout.CENTER);
                break;
            case "FreePick":
                content.add(freePickPanel, BorderLayout.CENTER);
                break;
            case "WorkPerformance":
                content.add(workPerPanel, BorderLayout.CENTER);
                break;
            case "Account":
                content.add(accountPanel, BorderLayout.CENTER);
                break;
        }

        content.revalidate();
        content.repaint();
    }

    public static void main(String[] args) {
        new shipperFrame("Shipper Owner", "SH003").setVisible(true);
    }

   
}
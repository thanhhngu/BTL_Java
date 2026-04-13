package presentation_layer.Shop;

import presentation_layer.Shop.MenuPanel.ConfirmPanel;
import presentation_layer.Shop.MenuPanel.DeliveredPanel;
import presentation_layer.Shop.MenuPanel.HomePanel;
import presentation_layer.Shop.MenuPanel.RevenuePanel;
import presentation_layer.mdl.AccountPanel;
import presentation_layer.mdl.SideBarr;
import presentation_layer.mdl.SidebarCallback;

import javax.swing.*;
import java.awt.*;

public class shopFrame extends JFrame implements SidebarCallback {
    private String id = "S001";

    private JPanel content = new JPanel(new BorderLayout());
    private JPanel mainPanel = new JPanel(new BorderLayout());
    private SideBarr sb;

    private HomePanel homePanel;
    private ConfirmPanel confirmPanel;
    private DeliveredPanel deliveredPanel;
    private RevenuePanel revenuePanel;
    private AccountPanel accountPanel = new AccountPanel();

    public shopFrame(String id) {
        this.id = id;

        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Sidebar
        sb = new SideBarr(new String[]{"Home", "Confirm", "Delivered", "Revenue", "Account"}, this);
        mainPanel.add(sb, BorderLayout.WEST);

        // Header
        JPanel header = createHeader();
        mainPanel.add(header, BorderLayout.NORTH);

        // Content
        homePanel = new HomePanel(id);
        confirmPanel = new ConfirmPanel(id);
        deliveredPanel = new DeliveredPanel(id);
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
            case "Delivered":
                content.add(deliveredPanel, BorderLayout.CENTER);
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


    private String username;

    // Header
    private JLabel lblUsername;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnLogout;


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

    public static void main(String[] args) {
        new shopFrame("S001").setVisible(true);
    }

}
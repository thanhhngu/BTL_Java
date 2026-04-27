package presentation_layer.Customer;
import presentation_layer.Login.loginFrame;
import presentation_layer.mdl.AccountPanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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

public class customerFrame extends JFrame {

    public static final String HOME = "HOME";
    public static final String ORDER = "ORDER";
    public static final String HISTORY = "HISTORY";
    public static final String ACCOUNT = "ACCOUNT";

    private String username;
    private String customerID;

    private JLabel lblUsername;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnLogout;

    private SideBarPanel sidebarPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    private ProductListPanel homePanel;
    private OrderPanel orderPanel;
    private HistoryPanel historyPanel;
    private AccountPanel accountPanel ;

    public customerFrame(String username, String customerID) {
        this.username = username;
        this.customerID = customerID;

        initUI();
        initEvents();
        showPanel(HOME);
    }

    private void initUI() {
        setTitle("Customer Main");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(createHeader(), BorderLayout.NORTH);

        sidebarPanel = new SideBarPanel();
        add(sidebarPanel, BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        
        orderPanel = new OrderPanel(customerID);
        homePanel = new ProductListPanel(orderPanel);
        historyPanel = new HistoryPanel(customerID);
        accountPanel = new AccountPanel(customerID);

        contentPanel.add(homePanel, HOME);
        contentPanel.add(orderPanel, ORDER);
        contentPanel.add(historyPanel, HISTORY);
        contentPanel.add(accountPanel, ACCOUNT);

        add(contentPanel, BorderLayout.CENTER);
    }
// header
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

    private JPanel createPlaceholderPanel(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private void initEvents() {
        sidebarPanel.getBtnHome().addActionListener(e -> goHome());
        sidebarPanel.getBtnOrder().addActionListener(e -> showPanel(ORDER));
        sidebarPanel.getBtnHistory().addActionListener(e -> showPanel(HISTORY));
        sidebarPanel.getBtnAccount().addActionListener(e -> showPanel(ACCOUNT));

        btnSearch.addActionListener(e -> searchProductByName());
        txtSearch.addActionListener(e -> searchProductByName());
        btnLogout.addActionListener(e -> logout());
    }

    private void goHome() {
        txtSearch.setText("");
        showPanel(HOME);

        if (homePanel != null) {
            homePanel.reloadDefaultState();
        }
    }

    private void searchProductByName() {
        String keyword = txtSearch.getText().trim();
        showPanel(HOME);

        if (homePanel != null) {
            homePanel.searchProduct(keyword);
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn đăng xuất không?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new loginFrame().setVisible(true);
        }
    }

    public void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
    }

}
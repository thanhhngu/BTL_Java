package presentation_layer.Customer;

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
import javax.swing.SwingConstants;

import presentation_layer.Login.loginFrame;

public class customerFrame extends JFrame {

    public static final String HOME = "HOME";
    public static final String CATEGORY = "CATEGORY";
    public static final String ORDER = "ORDER";
    public static final String HISTORY = "HISTORY";
    public static final String ACCOUNT = "ACCOUNT";

    private String username;

    // Header
    private JLabel lblUsername;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnLogout;

    // Main layout parts
    private SideBarPanel sidebarPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private CartPanel cartPanel;

    // Center cards
    private ProductlistPanel homePanel;
    private JPanel categoryPanel;
    private JPanel orderPanel;
    private JPanel historyPanel;
    private JPanel accountPanel;

    public customerFrame(String username) {
        this.username = username;
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

        homePanel = new ProductlistPanel();
        categoryPanel = createPlaceholderPanel("CATEGORY PANEL");
        orderPanel = createPlaceholderPanel("ORDER PANEL - đơn đang chờ xử lí / đang giao");
        historyPanel = createPlaceholderPanel("HISTORY PANEL - đơn đã mua");
        accountPanel = createPlaceholderPanel("ACCOUNT PANEL - form sửa thông tin");

        contentPanel.add(homePanel, HOME);
        contentPanel.add(categoryPanel, CATEGORY);
        contentPanel.add(orderPanel, ORDER);
        contentPanel.add(historyPanel, HISTORY);
        contentPanel.add(accountPanel, ACCOUNT);

        add(contentPanel, BorderLayout.CENTER);

        cartPanel = new CartPanel();
        add(cartPanel, BorderLayout.EAST);
    }

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
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private void initEvents() {
        sidebarPanel.getBtnHome().addActionListener(e -> showPanel(HOME));

        sidebarPanel.getBtnCategory().addActionListener(e -> {
            showPanel(CATEGORY);
            // TODO: load category data
        });

        sidebarPanel.getBtnOrder().addActionListener(e -> {
            showPanel(ORDER);
            // TODO: query đơn đang chờ xử lí / đang giao theo customer
        });

        sidebarPanel.getBtnHistory().addActionListener(e -> {
            showPanel(HISTORY);
            // TODO: query lịch sử đơn hàng theo customer
        });

        sidebarPanel.getBtnAccount().addActionListener(e -> {
            showPanel(ACCOUNT);
            // TODO: mở form hoặc load panel update thông tin
        });

        btnSearch.addActionListener(e -> searchProductByName());
        txtSearch.addActionListener(e -> searchProductByName());

        btnLogout.addActionListener(e -> logout());
        homePanel.setProductSelectListener(product -> {
            if (cartPanel.isEmpty()) {
                cartPanel.addProduct(product);
            } else if (cartPanel.getCurrentShopId().equals(product.getShopID())) {
                cartPanel.addProduct(product);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Chỉ được đặt sản phẩm cùng 1 shop trong một đơn hàng.");
            }
        });
        cartPanel.getBtnCheckout().addActionListener(e -> {
            if (cartPanel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống.");
                return;
            }

            JOptionPane.showMessageDialog(this, "Đặt hàng thành công (tạm thời mới là demo UI).");

            cartPanel.clearCart();
        });
        
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

    public CartPanel getCartPanel() {
        return cartPanel;
    }

    public ProductlistPanel getHomePanel() {
        return homePanel;
    }

}
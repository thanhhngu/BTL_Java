package presentation_layer.Customer;

import model_layer.category;
import model_layer.products;
import service_layer.CategoryService;
import service_layer.ProductService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProductListPanel extends JPanel {

    private JPanel productContainer;
    private ProductService productService;
    private CategoryService categoryService;
    private List<products> productList;

    private JComboBox<category> cbCategory;
    private JButton btnFilterCategory;

    private OrderPanel orderPanel;

    public ProductListPanel(OrderPanel orderPanel) {
        this.orderPanel = orderPanel;
        productService = new ProductService();
        categoryService = new CategoryService();
        productList = new ArrayList<>();

        initUI();
        loadCategories();
        loadProducts();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("DANH SÁCH SẢN PHẨM", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setOpaque(false);

        JLabel lblCategory = new JLabel("Lọc theo danh mục:");
        lblCategory.setFont(new Font("Arial", Font.BOLD, 15));

        cbCategory = new JComboBox<>();
        cbCategory.setPreferredSize(new Dimension(220, 32));

        btnFilterCategory = new JButton("Lọc");
        btnFilterCategory.setFont(new Font("Arial", Font.BOLD, 13));

        filterPanel.add(lblCategory);
        filterPanel.add(cbCategory);
        filterPanel.add(btnFilterCategory);

        topPanel.add(lblTitle, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.SOUTH);
// chua toan bo khoi san pham chinh cai nay
        productContainer = new JPanel();
        productContainer.setLayout(new GridLayout(0, 5, 10, 10));
        productContainer.setBackground(new Color(245, 245, 245));
        productContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(new Color(245, 245, 245));
        wrapperPanel.add(productContainer, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(wrapperPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnFilterCategory.addActionListener(e -> filterByCategory());
    }

    public void loadProducts() {
        productList = productService.getAllAvailableProducts();
        renderProducts(productList);
    }

    public void reloadDefaultState() {
        cbCategory.setSelectedIndex(0);
        loadProducts();
    }

    public void searchProduct(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            loadProducts();
        } else {
            productList = productService.searchProductsByName(keyword.trim());
            renderProducts(productList);
        }
    }

    private void loadCategories() {
        cbCategory.removeAllItems();

        category allCategory = new category("ALL", "Tất cả");
        cbCategory.addItem(allCategory);

        List<category> categories = categoryService.getAllCategories();
        for (category c : categories) {
            cbCategory.addItem(c);
        }
    }

    private void filterByCategory() {
        category selected = (category) cbCategory.getSelectedItem();

        if (selected == null || "ALL".equals(selected.getCatgID())) {
            loadProducts();
        } else {
            productList = productService.getProductsByCategory(selected.getCatgID());
            renderProducts(productList);
        }
    }

    private void renderProducts(List<products> list) {
        productContainer.removeAll();
        productContainer.setLayout(new GridLayout(0, 5, 10, 10));

        if (list == null || list.isEmpty()) {
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setBackground(new Color(245, 245, 245));

            JLabel lblEmpty = new JLabel("Không có sản phẩm nào", SwingConstants.CENTER);
            lblEmpty.setFont(new Font("Arial", Font.BOLD, 22));
            emptyPanel.add(lblEmpty, BorderLayout.CENTER);

            productContainer.setLayout(new GridLayout(1, 1));
            productContainer.add(emptyPanel);
            productContainer.setPreferredSize(new Dimension(1000, 120));
        } else {
            for (products p : list) {
                productContainer.add(createProductCard(p));
            }

            int columns = 5;
            int cardWidth = 185;
            int cardHeight = 285;
            int hGap = 10;
            int vGap = 10;
            int padding = 20;

            int itemCount = list.size();
            int rows = (int) Math.ceil((double) itemCount / columns);

            int preferredWidth = columns * cardWidth + (columns - 1) * hGap + padding;
            int preferredHeight = rows * cardHeight + (rows - 1) * vGap + padding;

            productContainer.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
        }

        productContainer.revalidate();
        productContainer.repaint();
    }
    // sua lai ham nay
    private JPanel createProductCard(products p) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 1));
        card.setPreferredSize(new Dimension(185, 285));

        JLabel lblImage = createImageLabel(p.getImagePath());
        lblImage.setPreferredSize(new Dimension(185, 140));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JLabel lblName = new JLabel(p.getName());
        lblName.setFont(new Font("Arial", Font.BOLD, 13));
        lblName.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblName.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel lblStock = new JLabel("Tồn kho: " + p.getUnitInStock());
        lblStock.setFont(new Font("Arial", Font.PLAIN, 13));
        lblStock.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStock.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel lblQuantityPerUnit = new JLabel("Đơn vị: " + p.getQuantityPerUnit());
        lblQuantityPerUnit.setFont(new Font("Arial", Font.PLAIN, 13));
        lblQuantityPerUnit.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblQuantityPerUnit.setHorizontalAlignment(SwingConstants.LEFT);

        String priceText;
        if (p.getUnitPrice() == 0) {
            priceText = "Giá: Chưa có giá";
        } else {
            java.text.DecimalFormat df = new java.text.DecimalFormat("#,###");
            priceText = "Giá: " + df.format(p.getUnitPrice()) + " VNĐ";
        }

        JLabel lblPrice = new JLabel(priceText);
        lblPrice.setFont(new Font("Arial", Font.BOLD, 13));
        lblPrice.setForeground(new Color(220, 30, 30));
        lblPrice.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblPrice.setHorizontalAlignment(SwingConstants.LEFT);

        JButton btnAdd = new JButton("Thêm");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 13));
        btnAdd.setFocusPainted(false);
        btnAdd.setPreferredSize(new Dimension(90, 30));

        btnAdd.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Đã chọn sản phẩm: " + p.getName());
        });
        btnAdd.addActionListener(e -> {
            if (orderPanel != null) {
                orderPanel.addProductToCart(p);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.add(btnAdd);

        infoPanel.add(lblName);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(lblStock);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(lblQuantityPerUnit);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(lblPrice);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(buttonPanel);

        card.add(lblImage, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }
    // ham nay chay thanh cong
    private JLabel createImageLabel(String imagePath) {
        JLabel lblImage = new JLabel("Không có ảnh", SwingConstants.CENTER);
        lblImage.setPreferredSize(new Dimension(210, 150));
        lblImage.setOpaque(true);
        lblImage.setBackground(new Color(230, 230, 230));
        lblImage.setFont(new Font("Arial", Font.BOLD, 13));

        try {
            if (imagePath != null && !imagePath.trim().isEmpty()) {

                // load từ resources giống code test của m
                java.net.URL url = getClass().getClassLoader().getResource(imagePath);

                if (url != null) {
                    ImageIcon icon = new ImageIcon(url);

                    // resize ảnh
                    Image img = icon.getImage().getScaledInstance(190, 130, Image.SCALE_SMOOTH);

                    lblImage.setText(""); // xóa chữ "Không có ảnh"
                    lblImage.setIcon(new ImageIcon(img));
                } else {
                    System.out.println("Không tìm thấy ảnh: " + imagePath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lblImage;
    }
}
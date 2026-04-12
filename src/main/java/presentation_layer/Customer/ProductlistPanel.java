package presentation_layer.Customer;

import model_layer.products;
import service_layer.ProductService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductlistPanel extends JPanel {

    private JPanel productContainer;
    private JScrollPane scrollPane;
    private ProductService productService;
    private ProductSelectListener productSelectListener;

    public ProductlistPanel() {
        productService = new ProductService();
        initUI();
        loadAllProducts();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        productContainer = new JPanel();
        productContainer.setBackground(Color.WHITE);
        productContainer.setLayout(new GridLayout(0, 4, 15, 15));

        scrollPane = new JScrollPane(productContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void loadAllProducts() {
        List<products> productList = productService.getAllProducts();
        renderProducts(productList);
    }

    public void searchProduct(String keyword) {
        List<products> productList;

        if (keyword == null || keyword.trim().isEmpty()) {
            productList = productService.getAllProducts();
        } else {
            productList = productService.searchProductsByName(keyword.trim());
        }

        renderProducts(productList);
    }

    public void setProductSelectListener(ProductSelectListener listener) {
        this.productSelectListener = listener;
    }

    private void renderProducts(List<products> productList) {
        productContainer.removeAll();

        if (productList == null || productList.isEmpty()) {
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setBackground(Color.WHITE);

            JLabel lblEmpty = new JLabel("Không tìm thấy sản phẩm", SwingConstants.CENTER);
            lblEmpty.setFont(new Font("Arial", Font.BOLD, 22));
            lblEmpty.setForeground(Color.GRAY);

            emptyPanel.add(lblEmpty, BorderLayout.CENTER);

            productContainer.setLayout(new GridLayout(1, 1));
            productContainer.add(emptyPanel);
        } else {
            productContainer.setLayout(new GridLayout(0, 4, 15, 15));

            for (products product : productList) {
                productContainer.add(createProductCard(product));
            }
        }

        productContainer.revalidate();
        productContainer.repaint();
    }

    private JPanel createProductCard(products product) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(8, 8));
        card.setBackground(new Color(235, 243, 250));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setPreferredSize(new Dimension(180, 250));

        JLabel lblImage = new JLabel();
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblImage.setPreferredSize(new Dimension(140, 100));
        lblImage.setOpaque(true);
        lblImage.setBackground(new Color(52, 132, 196));

        if (product.getImagePath() != null && !product.getImagePath().trim().isEmpty()) {
            try {
                File file = new File(product.getImagePath());
                if (file.exists()) {
                    ImageIcon icon = new ImageIcon(product.getImagePath());
                    Image img = icon.getImage().getScaledInstance(120, 80, Image.SCALE_SMOOTH);
                    lblImage.setIcon(new ImageIcon(img));
                    lblImage.setText("");
                } else {
                    lblImage.setText("No Image");
                    lblImage.setForeground(Color.WHITE);
                }
            } catch (Exception e) {
                lblImage.setText("No Image");
                lblImage.setForeground(Color.WHITE);
            }
        } else {
            lblImage.setText("No Image");
            lblImage.setForeground(Color.WHITE);
        }

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(card.getBackground());

        JLabel lblName = new JLabel(product.getName());
        lblName.setFont(new Font("Arial", Font.BOLD, 14));
        lblName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPrice = new JLabel("Giá: " + formatMoney(product.getUnitPrice()));
        lblPrice.setFont(new Font("Arial", Font.PLAIN, 13));
        lblPrice.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblQuantityPerUnit = new JLabel("ĐVT: " + product.getQuantityPerUnit());
        lblQuantityPerUnit.setFont(new Font("Arial", Font.PLAIN, 13));
        lblQuantityPerUnit.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblStock = new JLabel("Tồn kho: " + product.getUnitInStock());
        lblStock.setFont(new Font("Arial", Font.PLAIN, 13));
        lblStock.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(lblName);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(lblPrice);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(lblQuantityPerUnit);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(lblStock);

        JButton btnAdd = new JButton("THÊM");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 13));
        btnAdd.setBackground(new Color(52, 73, 94));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);

        if (product.getUnitInStock() <= 0) {
            btnAdd.setEnabled(false);
            btnAdd.setText("HẾT HÀNG");
        }

        btnAdd.addActionListener(e -> {
            if (productSelectListener != null) {
                productSelectListener.onProductSelected(product);
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(card.getBackground());
        bottomPanel.add(btnAdd);

        card.add(lblImage, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }

    private String formatMoney(double amount) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return nf.format(amount);
    }

    public interface ProductSelectListener {
        void onProductSelected(products product);
    }
}
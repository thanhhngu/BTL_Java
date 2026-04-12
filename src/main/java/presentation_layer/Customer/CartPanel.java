package presentation_layer.Customer;

import model_layer.products;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class CartPanel extends JPanel {

    private Map<String, CartItem> cartItems;
    private String currentShopId;

    private JPanel itemContainer;
    private JScrollPane scrollPane;
    private JLabel lblTotal;
    private JButton btnCheckout;

    public CartPanel() {
        cartItems = new LinkedHashMap<>();
        currentShopId = null;
        initUI();
        refreshCartUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(320, 0));

        JLabel lblTitle = new JLabel("ORDER DETAIL", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        itemContainer = new JPanel();
        itemContainer.setLayout(new BoxLayout(itemContainer, BoxLayout.Y_AXIS));
        itemContainer.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(itemContainer);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(Color.WHITE);

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        bottomPanel.add(separator);
        bottomPanel.add(Box.createVerticalStrut(10));

        lblTotal = new JLabel("Tổng số tiền: 0 đ");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 20));
        lblTotal.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomPanel.add(lblTotal);

        bottomPanel.add(Box.createVerticalStrut(15));

        btnCheckout = new JButton("Đặt hàng");
        btnCheckout.setFont(new Font("Arial", Font.BOLD, 18));
        btnCheckout.setBackground(new Color(210, 225, 238));
        btnCheckout.setFocusPainted(false);
        btnCheckout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCheckout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        bottomPanel.add(btnCheckout);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    public void addProduct(products product) {
        if (product == null) {
            return;
        }

        String productId = product.getProductID();

        if (cartItems.isEmpty()) {
            currentShopId = product.getShopID();
        }

        if (cartItems.containsKey(productId)) {
            CartItem item = cartItems.get(productId);

            if (item.quantity < product.getUnitInStock()) {
                item.quantity++;
            } else {
                JOptionPane.showMessageDialog(this,
                        "Số lượng thêm vượt quá tồn kho của sản phẩm.");
            }
        } else {
            if (product.getUnitInStock() <= 0) {
                JOptionPane.showMessageDialog(this, "Sản phẩm đã hết hàng.");
                return;
            }

            cartItems.put(productId, new CartItem(product, 1));
        }

        refreshCartUI();
    }

    public Object getCurrentShopId() {
        return currentShopId;
    }

    public void clearCart() {
        cartItems.clear();
        currentShopId = null;
        refreshCartUI();
    }

    public JButton getBtnCheckout() {
        return btnCheckout;
    }

    public Map<String, CartItem> getCartItems() {
        return cartItems;
    }

    private void refreshCartUI() {
        itemContainer.removeAll();

        if (cartItems.isEmpty()) {
            JLabel lblEmpty = new JLabel("Chưa có sản phẩm nào trong giỏ hàng");
            lblEmpty.setFont(new Font("Arial", Font.ITALIC, 16));
            lblEmpty.setForeground(Color.GRAY);
            lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
            itemContainer.add(Box.createVerticalStrut(20));
            itemContainer.add(lblEmpty);
        } else {
            for (CartItem item : cartItems.values()) {
                itemContainer.add(createCartItemPanel(item));
                itemContainer.add(Box.createVerticalStrut(8));
            }
        }

        lblTotal.setText("Tổng số tiền: " + formatMoney(calculateTotal()));
        itemContainer.revalidate();
        itemContainer.repaint();
    }

    private JPanel createCartItemPanel(CartItem item) {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(new Color(245, 248, 250));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(8, 8, 8, 8)
        ));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(panel.getBackground());

        JLabel lblName = new JLabel(item.product.getName());
        lblName.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel lblPrice = new JLabel("Đơn giá: " + formatMoney(item.product.getUnitPrice()));
        lblPrice.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel lblQuantity = new JLabel("Số lượng: " + item.quantity);
        lblQuantity.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel lblSubtotal = new JLabel("Thành tiền: " + formatMoney(item.getSubtotal()));
        lblSubtotal.setFont(new Font("Arial", Font.PLAIN, 13));

        infoPanel.add(lblName);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(lblPrice);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(lblQuantity);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(lblSubtotal);

        JPanel actionPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        actionPanel.setBackground(panel.getBackground());

        JButton btnPlus = new JButton("+");
        JButton btnMinus = new JButton("-");
        JButton btnRemove = new JButton("Xóa");

        btnPlus.addActionListener(e -> increaseQuantity(item.product.getProductID()));
        btnMinus.addActionListener(e -> decreaseQuantity(item.product.getProductID()));
        btnRemove.addActionListener(e -> removeProduct(item.product.getProductID()));

        actionPanel.add(btnPlus);
        actionPanel.add(btnMinus);
        actionPanel.add(btnRemove);

        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.EAST);

        return panel;
    }

    private void increaseQuantity(String productId) {
        CartItem item = cartItems.get(productId);
        if (item == null) {
            return;
        }

        if (item.quantity < item.product.getUnitInStock()) {
            item.quantity++;
            refreshCartUI();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Không thể tăng thêm. Đã đạt tối đa tồn kho.");
        }
    }

    private void decreaseQuantity(String productId) {
        CartItem item = cartItems.get(productId);
        if (item == null) {
            return;
        }

        item.quantity--;

        if (item.quantity <= 0) {
            cartItems.remove(productId);
        }

        if (cartItems.isEmpty()) {
            currentShopId = null;
        }

        refreshCartUI();
    }

    private void removeProduct(String productId) {
        cartItems.remove(productId);

        if (cartItems.isEmpty()) {
            currentShopId = null;
        }

        refreshCartUI();
    }

    private double calculateTotal() {
        double total = 0;
        for (CartItem item : cartItems.values()) {
            total += item.getSubtotal();
        }
        return total;
    }

    private String formatMoney(double amount) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return nf.format(amount);
    }

    public static class CartItem {
        private products product;
        private int quantity;

        public CartItem(products product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public products getProduct() {
            return product;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getSubtotal() {
            return product.getUnitPrice() * quantity;
        }
    }
}
package presentation_layer;

import model_layer.CartItem;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class ShopCartPanel extends JPanel {

    private String shopID;
    private String shopName;
    private List<CartItem> items;

    private JButton btnSelectShop;
    private JPanel productListPanel;
    private JLabel lblShopTotal;

    private ShopCartListener listener;

    public ShopCartPanel(String shopID, String shopName, List<CartItem> items, ShopCartListener listener) {
        this.shopID = shopID;
        this.shopName = shopName;
        this.items = items;
        this.listener = listener;

        initUI();
        renderItems();
        updateShopTotal();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(createBlockBorder(false));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        titlePanel.setOpaque(false);

        JLabel lblShopTitle = new JLabel(shopName);
        lblShopTitle.setFont(new Font("Arial", Font.BOLD, 18));

        titlePanel.add(lblShopTitle);

        JPanel headerWrap = new JPanel(new BorderLayout());
        headerWrap.setOpaque(false);
        headerWrap.setBorder(new EmptyBorder(10, 0, 0, 0));
        headerWrap.add(createHeaderRow(), BorderLayout.CENTER);

        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(headerWrap, BorderLayout.CENTER);

        productListPanel = new JPanel();
        productListPanel.setOpaque(false);
        productListPanel.setLayout(new BoxLayout(productListPanel, BoxLayout.Y_AXIS));

        lblShopTotal = new JLabel("Tổng shop: 0 VND");
        lblShopTotal.setFont(new Font("Arial", Font.BOLD, 14));

        btnSelectShop = new JButton("Đặt đơn");
        btnSelectShop.setFont(new Font("Arial", Font.BOLD, 14));
        btnSelectShop.setFocusPainted(false);
        btnSelectShop.setPreferredSize(new Dimension(150, 35));

        btnSelectShop.addActionListener(e -> {
            if (listener != null) {
                listener.onSelectShop(shopID, shopName, items);
            }
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        bottomPanel.add(lblShopTotal, BorderLayout.WEST);

        JPanel buttonWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonWrap.setOpaque(false);
        buttonWrap.add(btnSelectShop);

        bottomPanel.add(buttonWrap, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(productListPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderRow() {
        JPanel row = new JPanel();
        row.setOpaque(false);
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setBorder(new EmptyBorder(0, 4, 0, 4));

        row.add(createCellLabel("Sản phẩm", 260, SwingConstants.LEFT, true));
        row.add(Box.createHorizontalStrut(10));
        row.add(createCellLabel("SL", 70, SwingConstants.CENTER, true));
        row.add(Box.createHorizontalStrut(10));
        row.add(createCellLabel("Đơn giá", 120, SwingConstants.RIGHT, true));
        row.add(Box.createHorizontalStrut(10));
        row.add(createCellLabel("Thành tiền", 140, SwingConstants.RIGHT, true));
        row.add(Box.createHorizontalStrut(10));
        row.add(createCellLabel("", 80, SwingConstants.CENTER, true));

        return row;
    }

    private void renderItems() {
        productListPanel.removeAll();

        if (items == null || items.isEmpty()) {
            JLabel lblEmpty = new JLabel("Chưa có sản phẩm nào.");
            lblEmpty.setFont(new Font("Arial", Font.ITALIC, 13));
            lblEmpty.setForeground(Color.GRAY);
            lblEmpty.setAlignmentX(Component.LEFT_ALIGNMENT);
            productListPanel.add(lblEmpty);
        } else {
            for (CartItem item : items) {
                productListPanel.add(createItemRow(item));
                productListPanel.add(Box.createVerticalStrut(8));
            }
        }

        productListPanel.revalidate();
        productListPanel.repaint();
    }

    private JPanel createItemRow(CartItem item) {
        JPanel row = new JPanel();
        row.setOpaque(true);
        row.setBackground(new Color(250, 250, 250));
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 225, 225), 1),
                new EmptyBorder(8, 8, 8, 8)
        ));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        JLabel lblName = createCellLabel(item.getProduct().getName(), 260, SwingConstants.LEFT, false);

        int maxQty = Math.max(1, Math.max(item.getQuantity(), item.getProduct().getUnitInStock()));
        JSpinner spQuantity = new JSpinner(new SpinnerNumberModel(item.getQuantity(), 1, maxQty, 1));
        Dimension spinnerSize = new Dimension(70, 28);
        spQuantity.setPreferredSize(spinnerSize);
        spQuantity.setMaximumSize(spinnerSize);
        spQuantity.setMinimumSize(spinnerSize);

        JComponent editor = spQuantity.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JFormattedTextField txt = ((JSpinner.DefaultEditor) editor).getTextField();
            txt.setHorizontalAlignment(JTextField.CENTER);
            if (txt.getFormatter() instanceof DefaultFormatter) {
                ((DefaultFormatter) txt.getFormatter()).setCommitsOnValidEdit(true);
            }
        }

        JLabel lblUnitPrice = createCellLabel(formatMoney(item.getProduct().getUnitPrice()), 120, SwingConstants.RIGHT, false);
        JLabel lblLineTotal = createCellLabel(formatMoney(item.getLineTotal()), 140, SwingConstants.RIGHT, true);

        JButton btnRemove = new JButton("Xóa");
        btnRemove.setFont(new Font("Arial", Font.BOLD, 12));
        btnRemove.setFocusPainted(false);
        btnRemove.setPreferredSize(new Dimension(80, 28));
        btnRemove.setMaximumSize(new Dimension(80, 28));

        btnRemove.addActionListener(e -> {
            if (listener != null) {
                listener.onRemoveProduct(shopID, item);
            }
        });

        spQuantity.addChangeListener(e -> {
            int newQty = (Integer) spQuantity.getValue();
            if (newQty == item.getQuantity()) {
                return;
            }

            item.setQuantity(newQty);
            lblLineTotal.setText(formatMoney(item.getLineTotal()));
            updateShopTotal();

            if (listener != null) {
                listener.onQuantityChanged(shopID, item, newQty);
            }
        });

        row.add(lblName);
        row.add(Box.createHorizontalStrut(10));
        row.add(spQuantity);
        row.add(Box.createHorizontalStrut(10));
        row.add(lblUnitPrice);
        row.add(Box.createHorizontalStrut(10));
        row.add(lblLineTotal);
        row.add(Box.createHorizontalGlue());
        row.add(btnRemove);

        return row;
    }

    private JLabel createCellLabel(String text, int width, int horizontalAlignment, boolean bold) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(horizontalAlignment);
        label.setFont(new Font("Arial", bold ? Font.BOLD : Font.PLAIN, 13));
        Dimension size = new Dimension(width, 28);
        label.setPreferredSize(size);
        label.setMaximumSize(size);
        label.setMinimumSize(size);
        return label;
    }

    private void updateShopTotal() {
        lblShopTotal.setText("Tổng shop: " + formatMoney(calculateShopTotal()));
    }

    private double calculateShopTotal() {
        double total = 0;
        if (items != null) {
            for (CartItem item : items) {
                total += item.getLineTotal();
            }
        }
        return total;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
        renderItems();
        updateShopTotal();
    }

    public void setBlockSelected(boolean selected) {
        setBorder(createBlockBorder(selected));
        repaint();
    }

    private Border createBlockBorder(boolean selected) {
        Color borderColor = selected ? new Color(59, 130, 246) : new Color(210, 210, 210);
        int thickness = selected ? 2 : 1;
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, thickness),
                new EmptyBorder(10, 0, 10, 0)
        );
    }

    private String formatMoney(double value) {
        return new DecimalFormat("#,##0").format(value) + " VND";
    }

    public interface ShopCartListener {
        void onSelectShop(String shopID, String shopName, List<CartItem> items);
        void onQuantityChanged(String shopID, CartItem item, int newQuantity);
        void onRemoveProduct(String shopID, CartItem item);
    }
}
package presentation_layer;

import model_layer.CartItem;
import model_layer.addressCustomer;
import model_layer.methodPayment;
import model_layer.products;
import service_layer.AddressService;
import service_layer.OrderService;
import service_layer.PaymentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderPanel extends JPanel {

    private static final Logger LOGGER = Logger.getLogger(OrderPanel.class.getName());

    private final String customerID;
    private final Map<String, List<CartItem>> cartByShop;
    private final Map<String, ShopCartPanel> shopPanels;

    private final AddressService addressService;
    private final PaymentService paymentService;

    private JPanel shopContainer;
    private JScrollPane scrollPane;
    private OrderDetailPanel detailPanel;

    private String selectedShopID;
    private String selectedShopName;

    public OrderPanel(String customerID) {
        this.customerID = customerID;
        this.cartByShop = new LinkedHashMap<String, List<CartItem>>();
        this.shopPanels = new LinkedHashMap<String, ShopCartPanel>();
        this.addressService = new AddressService();
        this.paymentService = new PaymentService();

        initUI();
        bindEvents();
        loadReferenceDataAsync();
        renderShopBlocks();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        shopContainer = new JPanel();
        shopContainer.setLayout(new BoxLayout(shopContainer, BoxLayout.Y_AXIS));
        shopContainer.setBackground(new Color(245, 245, 245));
        shopContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        scrollPane = new JScrollPane(shopContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        detailPanel = new OrderDetailPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, detailPanel);
        splitPane.setDividerLocation(760);
        splitPane.setResizeWeight(0.65);
        splitPane.setBorder(null);

        add(splitPane, BorderLayout.CENTER);
    }

    private void bindEvents() {
        detailPanel.getBtnPlaceOrder().addActionListener(e -> handlePlaceOrder());
    }

    public void addProductToCart(final products product) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    addProductToCart(product);
                }
            });
            return;
        }

        if (product == null) {
            return;
        }

        String shopID = product.getShopID();
        List<CartItem> items = cartByShop.get(shopID);
        if (items == null) {
            items = new ArrayList<CartItem>();
            cartByShop.put(shopID, items);
        }

        CartItem existing = findItemByProductID(items, product.getProductID());
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + 1);
        } else {
            items.add(new CartItem(product, 1));
        }

        renderShopBlocks();

        if (shopID != null && shopID.equals(selectedShopID)) {
            refreshSelectedDetail();
        }
    }

    private CartItem findItemByProductID(List<CartItem> items, String productID) {
        if (items == null || productID == null) {
            return null;
        }
        for (CartItem item : items) {
            if (item.getProduct() != null && productID.equals(item.getProduct().getProductID())) {
                return item;
            }
        }
        return null;
    }

    private void renderShopBlocks() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    renderShopBlocks();
                }
            });
            return;
        }

        shopContainer.removeAll();
        shopPanels.clear();

        if (cartByShop.isEmpty()) {
            JLabel lblEmpty = new JLabel("Chưa có sản phẩm nào trong giỏ hàng.");
            lblEmpty.setFont(new Font("Arial", Font.BOLD, 18));
            lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
            shopContainer.add(Box.createVerticalStrut(50));
            shopContainer.add(lblEmpty);
        } else {
            for (Map.Entry<String, List<CartItem>> entry : cartByShop.entrySet()) {
                final String shopID = entry.getKey();
                final List<CartItem> items = entry.getValue();
                final String shopName = resolveShopName(shopID, items);

                ShopCartPanel panel = new ShopCartPanel(
                        shopID,
                        shopName,
                        items,
                        new ShopCartPanel.ShopCartListener() {
                            @Override
                            public void onSelectShop(String shopID, String shopName, List<CartItem> items) {
                                handleSelectShop(shopID, shopName, items);
                            }

                            @Override
                            public void onQuantityChanged(String shopID, CartItem item, int newQuantity) {
                                handleQuantityChanged(shopID, item, newQuantity);
                            }

                            @Override
                            public void onRemoveProduct(String shopID, CartItem item) {
                                removeProduct(shopID, item);
                            }
                        }
                );

                panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                panel.setBlockSelected(shopID.equals(selectedShopID));
                shopPanels.put(shopID, panel);

                shopContainer.add(panel);
                shopContainer.add(Box.createVerticalStrut(12));
            }
        }

        shopContainer.revalidate();
        shopContainer.repaint();
    }

    private String resolveShopName(String shopID, List<CartItem> items) {
        if (items != null && !items.isEmpty() && items.get(0).getProduct() != null) {
            String name = items.get(0).getProduct().getShopName();
            if (name != null && !name.trim().isEmpty()) {
                return name;
            }
        }
        return shopID;
    }

    private void highlightSelectedShop(String shopID) {
        for (Map.Entry<String, ShopCartPanel> entry : shopPanels.entrySet()) {
            entry.getValue().setBlockSelected(entry.getKey().equals(shopID));
        }
    }

    private void handleSelectShop(String shopID, String shopName, List<CartItem> items) {
        this.selectedShopID = shopID;
        this.selectedShopName = shopName;

        detailPanel.showBill(shopID, shopName, items);
        highlightSelectedShop(shopID);
    }

    private void handleQuantityChanged(final String shopID, final CartItem changedItem, final int newQuantity) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    handleQuantityChanged(shopID, changedItem, newQuantity);
                }
            });
            return;
        }

        List<CartItem> items = cartByShop.get(shopID);
        if (items == null || changedItem == null || changedItem.getProduct() == null) {
            return;
        }

        CartItem modelItem = findItemByProductID(items, changedItem.getProduct().getProductID());
        if (modelItem == null) {
            return;
        }

        if (newQuantity <= 0) {
            removeProduct(shopID, modelItem);
            return;
        }

        modelItem.setQuantity(newQuantity);

        renderShopBlocks();

        if (shopID != null && shopID.equals(selectedShopID)) {
            refreshSelectedDetail();
        } else {
            highlightSelectedShop(selectedShopID);
        }
    }

    private void removeProduct(final String shopID, final CartItem item) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    removeProduct(shopID, item);
                }
            });
            return;
        }

        List<CartItem> items = cartByShop.get(shopID);
        if (items == null || item == null || item.getProduct() == null) {
            return;
        }

        Iterator<CartItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            CartItem current = iterator.next();
            if (current.getProduct() != null
                    && current.getProduct().getProductID().equals(item.getProduct().getProductID())) {
                iterator.remove();
                break;
            }
        }

        if (items.isEmpty()) {
            cartByShop.remove(shopID);
            shopPanels.remove(shopID);

            if (shopID != null && shopID.equals(selectedShopID)) {
                selectedShopID = null;
                selectedShopName = null;
                detailPanel.clearBill();
            }
        }

        renderShopBlocks();

        if (shopID != null && shopID.equals(selectedShopID)) {
            refreshSelectedDetail();
        }
    }

    private void refreshSelectedDetail() {
        if (selectedShopID == null) {
            detailPanel.clearBill();
            return;
        }

        List<CartItem> items = cartByShop.get(selectedShopID);
        if (items == null || items.isEmpty()) {
            selectedShopID = null;
            selectedShopName = null;
            detailPanel.clearBill();
            return;
        }

        if (selectedShopName == null || selectedShopName.trim().isEmpty()) {
            selectedShopName = resolveShopName(selectedShopID, items);
        }

        detailPanel.showBill(selectedShopID, selectedShopName, items);
        highlightSelectedShop(selectedShopID);
    }

    private void loadReferenceDataAsync() {
        SwingWorker<ReferenceData, Void> worker = new SwingWorker<ReferenceData, Void>() {
            @Override
            protected ReferenceData doInBackground() {
                List<methodPayment> paymentMethods = paymentService.getAllPaymentMethods();
                List<addressCustomer> addresses = addressService.getAddressesByCustomer(customerID);
                return new ReferenceData(paymentMethods, addresses);
            }

            @Override
            protected void done() {
                try {
                    ReferenceData data = get();
                    detailPanel.setPaymentMethodData(data.paymentMethods);
                    detailPanel.setAddressData(data.addresses);
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Không tải được dữ liệu địa chỉ / thanh toán", ex);
                    JOptionPane.showMessageDialog(OrderPanel.this,
                            "Không tải được dữ liệu địa chỉ hoặc phương thức thanh toán.",
                            "Lỗi dữ liệu",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void handlePlaceOrder() {
        if (selectedShopID == null) {
            JOptionPane.showMessageDialog(this, "Hãy chọn một shop trước khi đặt hàng.");
            return;
        }

        List<CartItem> items = cartByShop.get(selectedShopID);
        if (items == null || items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Shop đang chọn không còn sản phẩm nào.");
            detailPanel.clearBill();
            return;
        }

        methodPayment payment = detailPanel.getSelectedPaymentMethod();
        addressCustomer address = detailPanel.getSelectedAddress();

        if (payment == null) {
            JOptionPane.showMessageDialog(this, "Hãy chọn phương thức thanh toán.");
            return;
        }

        if (address == null) {
            JOptionPane.showMessageDialog(this, "Hãy chọn địa chỉ nhận hàng.");
            return;
        }

        double total = calculateTotal(items);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Shop: " + selectedShopName + "\n"
                        + "Địa chỉ: " + address.getAddress() + "\n"
                        + "Thanh toán: " + payment.getPaymentMethod() + "\n"
                        + "Tổng tiền: " + new java.text.DecimalFormat("#,##0").format(total) + " VND\n\n"
                        + "Bạn có chắc muốn đặt hàng không?",
                "Xác nhận đặt hàng",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        OrderService orderService = new OrderService();

        boolean success = orderService.placeOrder(
                customerID,
                selectedShopID,
                address.getAddressID(),
                payment.getPayID(),
                total,
                items
        );

        if (success) {
            JOptionPane.showMessageDialog(this, "Đặt hàng thành công.");

            cartByShop.remove(selectedShopID);
            selectedShopID = null;
            selectedShopName = null;

            renderShopBlocks();
            detailPanel.clearBill();
        } else {
            JOptionPane.showMessageDialog(this, "Đặt hàng thất bại.");
        }
    }
    private double calculateTotal(List<CartItem> items) {
        double total = 0;
        for (CartItem item : items) {
            total += item.getLineTotal();
        }
        return total;
    }

    private static class ReferenceData {
        private final List<methodPayment> paymentMethods;
        private final List<addressCustomer> addresses;

        private ReferenceData(List<methodPayment> paymentMethods, List<addressCustomer> addresses) {
            this.paymentMethods = paymentMethods;
            this.addresses = addresses;
        }
    }
}
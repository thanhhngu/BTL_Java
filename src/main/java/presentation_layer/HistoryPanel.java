package presentation_layer;

import model_layer.OrderHistoryDetailItem;
import model_layer.OrderHistoryItem;
import service_layer.HistoryService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HistoryPanel extends JPanel {

    private final String customerID;
    private final HistoryService historyService;

    private JTable table;
    private DefaultTableModel tableModel;
    private List<OrderHistoryItem> orders;

    public HistoryPanel(String customerID) {
        this.customerID = customerID;
        this.historyService = new HistoryService();
        this.orders = new ArrayList<>();

        initUI();
        loadData();
        handleDoubleClickRow();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("Lịch sử đơn hàng");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));

        String[] columns = {
                "Mã đơn", "Ngày đặt", "Ngày giao", "Shipper", "Địa chỉ", "Thanh toán", "Phí ship", "Tổng tiền", "Trạng thái"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);

        add(lblTitle, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadData() {
        orders = historyService.getDeliveredOrdersByCustomer(customerID);
        tableModel.setRowCount(0);

        DecimalFormat df = new DecimalFormat("#,##0");

        for (OrderHistoryItem item : orders) {
            tableModel.addRow(new Object[]{
                    item.getOrderID(),
                    item.getOrderDate(),
                    item.getShippedDate(),
                    item.getShipperID(),
                    item.getAddressID(),
                    item.getPaymentID(),
                    df.format(item.getFreight()) + " VND",
                    df.format(item.getAmount()) + " VND",
                    item.getStatus()
            });
        }
    }

    private void handleDoubleClickRow() {
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int row = table.getSelectedRow();
                    String orderID = table.getValueAt(row, 0).toString();

                    OrderHistoryItem selectedOrder = orders.stream()
                            .filter(o -> o.getOrderID().equals(orderID))
                            .findFirst()
                            .orElse(null);

                    if (selectedOrder != null) {
                        showOrderDetailDialog(selectedOrder);
                    }
                }
            }
        });
    }

    private void showOrderDetailDialog(OrderHistoryItem selectedOrder) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(700, 450));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Arial", Font.PLAIN, 14));
        infoArea.setBackground(new Color(245, 245, 245));

        DecimalFormat df = new DecimalFormat("#,##0");

        StringBuilder message = new StringBuilder();
        message.append("Order ID: ").append(selectedOrder.getOrderID()).append("\n");
        message.append("Customer ID: ").append(selectedOrder.getCustomerID()).append("\n");
        message.append("Shipper ID: ").append(selectedOrder.getShipperID()).append("\n");
        message.append("Order Date: ").append(selectedOrder.getOrderDate()).append("\n");
        message.append("Shipped Date: ").append(selectedOrder.getShippedDate()).append("\n");
        message.append("Freight: ").append(df.format(selectedOrder.getFreight())).append(" VND\n");
        message.append("Amount: ").append(df.format(selectedOrder.getAmount())).append(" VND\n");
        message.append("Address ID: ").append(selectedOrder.getAddressID()).append("\n");
        message.append("Payment ID: ").append(selectedOrder.getPaymentID()).append("\n");
        message.append("Status: ").append(selectedOrder.getStatus()).append("\n");
        message.append("Shop ID: ").append(selectedOrder.getShopID()).append("\n");

        infoArea.setText(message.toString());

        String[] cols = {"Mã SP", "Tên sản phẩm", "SL", "Đơn giá", "Giảm giá"};
        DefaultTableModel detailModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (OrderHistoryDetailItem item : selectedOrder.getDetails()) {
            detailModel.addRow(new Object[]{
                    item.getProductID(),
                    item.getProductName(),
                    item.getQuantity(),
                    df.format(item.getUnitPrice()) + " VND",
                    item.getDiscount()
            });
        }

        JTable detailTable = new JTable(detailModel);
        detailTable.setRowHeight(28);
        detailTable.setFont(new Font("Arial", Font.PLAIN, 13));
        detailTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        panel.add(new JScrollPane(infoArea), BorderLayout.NORTH);
        panel.add(new JScrollPane(detailTable), BorderLayout.CENTER);

        JOptionPane.showMessageDialog(
                this,
                panel,
                "Chi tiết đơn hàng",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
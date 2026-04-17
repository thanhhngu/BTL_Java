package presentation_layer.mdl;

import model_layer.order;
import model_layer.order_detail;
import model_layer.products;
import presentation_layer.itf.HandleActionCallBack;
import repository_layer.OrderReponsitory;
import repository_layer.ProductRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.function.Function;

import static presentation_layer.mdl.ShowImage.showImg;

public class HandleAction {

    public void HandleAction() {
    }

//    public void handleDClickRowTable(JTable table, List<order> orders) {
//        table.addMouseListener(new java.awt.event.MouseAdapter() {
//            @Override
//            public void mouseClicked(java.awt.event.MouseEvent e) {
//                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
//                    int row = table.getSelectedRow();
//
//                    String orderID = table.getValueAt(row, 0).toString();
//
//                    order selectedOrder = orders.stream()
//                            .filter(o -> o.getOrderID().equals(orderID))
//                            .findFirst()
//                            .orElse(null);
//
//                    if (selectedOrder != null) {
//
//                        // ===== Tạo nội dung dialog =====
//                        StringBuilder message = new StringBuilder();
//
//                        message.append("Order ID: ").append(selectedOrder.getOrderID()).append("\n");
//                        message.append("Customer ID: ").append(selectedOrder.getCustomerID()).append("\n");
//                        message.append("Shipper ID: ").append(selectedOrder.getShipperID()).append("\n");
//                        message.append("Order Date: ").append(selectedOrder.getOrderDate()).append("\n");
//                        message.append("Freight: ").append(selectedOrder.getFreight()).append("\n");
//                        message.append("Amount: ").append(selectedOrder.getAmount()).append("\n");
//                        message.append("Address ID: ").append(selectedOrder.getAddressID()).append("\n");
//                        message.append("Payment ID: ").append(selectedOrder.getPaymentID()).append("\n");
//                        message.append("Status: ").append(selectedOrder.getStatus()).append("\n\n");
//
//                        message.append("=== Products ===\n");
//
//                        for (order_detail item : selectedOrder.getItems()) {
//                            message.append("- ")
//                                    .append(item.getProduct().getName())
//                                    .append(" | Qty: ")
//                                    .append(item.getQuantity())
//                                    .append("\n");
//                        }
//
//                        // ===== Hiển thị dialog =====
//                        JOptionPane.showMessageDialog(
//                                null,
//                                message.toString(),
//                                "Order Details",
//                                JOptionPane.INFORMATION_MESSAGE
//                        );
//                    }
//                }
//            }
//        });
//    }

    public static <T> void handleDClickRowTable(
            JTable table,
            List<T> data, // list data cua table
            Function<T, String> idExtractor, // lay id de so sanh voi id trong table
            HandleActionCallBack.RowDoubleClickHandler<T> callback // hien thi dialog khi double click
    ) {
        table.setDefaultEditor(Object.class, null);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int row = table.getSelectedRow();
                    String id = table.getValueAt(row, 0).toString();

                    T selectedItem = data.stream()
                            .filter(item -> idExtractor.apply(item).equals(id))
                            .findFirst()
                            .orElse(null);

                    if (selectedItem != null) {
                        JPanel panel = callback.buildMessage(selectedItem); // nhan vao 1 panel

                        JOptionPane.showOptionDialog(
                                null,
                                panel,
                                "Details",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.PLAIN_MESSAGE,// Hoặc JOptionPane.OK_CANCEL_OPTION
                                null,
                                new Object[]{}, // Mảng rỗng = không có nút
                                null
                        );

                    }
                }
            }
        });
    }

    //show for dbclick

    public static JPanel showImageProduct(products p, Component parent) {
        String imgPath = new ProductRepository().getImagePath(p.getProductID());
        if (imgPath == null) {
            JOptionPane.showMessageDialog(parent, "Failed to load image for product: " + p.getName());
            return null;
        }
        return showImg(imgPath);
    }

    public static JPanel showOrderDetail(order o, DefaultTableModel model, String shopId, Component parent) {

        JPanel detailPanel = new JPanel(new BorderLayout());

        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 5));

        JLabel lblOrderID = new JLabel();
        JLabel lblCustomerID = new JLabel();
        JLabel lblShipperID = new JLabel();
        JLabel lblOrderDate = new JLabel();
        JLabel lblShippedDate = new JLabel();
        JLabel lblFreight = new JLabel();
        JLabel lblAmount = new JLabel();
        JLabel lblStatus = new JLabel();

        infoPanel.add(new JLabel("Order ID:"));
        infoPanel.add(lblOrderID);
        infoPanel.add(new JLabel("Customer ID:"));
        infoPanel.add(lblCustomerID);
        infoPanel.add(new JLabel("Shipper ID:"));
        infoPanel.add(lblShipperID);
        infoPanel.add(new JLabel("Order Date:"));
        infoPanel.add(lblOrderDate);
        infoPanel.add(new JLabel("Shipped Date:"));
        infoPanel.add(lblShippedDate);
        infoPanel.add(new JLabel("Freight:"));
        infoPanel.add(lblFreight);
        infoPanel.add(new JLabel("Amount:"));
        infoPanel.add(lblAmount);
        infoPanel.add(new JLabel("Status:"));
        infoPanel.add(lblStatus);

        DefaultTableModel productModel = new DefaultTableModel(
                new String[]{"Product", "Qty"}, 0
        );
        JTable productTable = new JTable(productModel);

        detailPanel.add(infoPanel, BorderLayout.NORTH);
        detailPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnClose = new JButton("Close");
        JButton btnConfirm = new JButton("Confirm");

        btnClose.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(detailPanel).dispose();
        });

        btnConfirm.addActionListener(e -> {
            boolean success = new OrderReponsitory().updateOrderStatus(o.getOrderID(), "CONFIRMED");
            if (success) {
                JOptionPane.showMessageDialog(parent, "Product deleted successfully!");
                refreshTable(model, shopId);
            }
        });

        if(o.getShippedDate()==null || o.getStatus().equals("SHIPPING")) {
            buttonPanel.add(btnConfirm);
        }
        buttonPanel.add(btnClose);

        detailPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ===== Gán dữ liệu =====
        lblOrderID.setText(o.getOrderID());
        lblCustomerID.setText(o.getCustomerID());
        lblShipperID.setText(o.getShipperID());
        lblOrderDate.setText(o.getOrderDate().toString());
        if (o.getShippedDate() != null) {
            lblShippedDate.setText(o.getShippedDate().toString());
        } else {
            lblShippedDate.setText("Not shipped yet");
        }
        lblFreight.setText(String.valueOf(o.getFreight()));
        lblAmount.setText(String.valueOf(o.getAmount()));
        lblStatus.setText(o.getStatus());

        productModel.setRowCount(0);
        for (order_detail item : o.getItems()) {
            productModel.addRow(new Object[]{
                    item.getProduct().getName(),
                    item.getQuantity()
            });
        }

        return detailPanel;
    }

    //-----------------------------------------------------------------------------------------------
    public static void refreshTable(DefaultTableModel model, String id) {
        ProductRepository productRepo = new ProductRepository();
        List<products> productList = productRepo.findByShopID(id);

        // Xóa dữ liệu cũ
        model.setRowCount(0);

        // Thêm dữ liệu mới
        for (products p : productList) {
            model.addRow(new Object[]{
                    p.getProductID(),
                    p.getName(),
                    p.getUnitPrice(),
                    p.getUnitInStock(),
                    p.getQuantityPerUnit()
            });
        }
    }

    //--------------------- handleAction for HomePanel------------------
    public static void handleEventAddProduct(JTable table, DefaultTableModel model, String shopId, Component parent) {
        // Tạo dialog
        JDialog dialog = new JDialog((Frame) null, "Add Product", true);
        dialog.setLayout(new GridLayout(0, 2, 10, 10));

        // Các trường nhập liệu
        JTextField txtCatgID = new JTextField();
        JTextField txtName = new JTextField();
        JTextField txtUnitPrice = new JTextField();
        JTextField txtUnitInStock = new JTextField();

        // shopID hiển thị nhưng không sửa
        JTextField txtShopID = new JTextField(shopId);
        txtShopID.setEditable(false);

        // quantityPerUnit là combobox
        JComboBox<String> cbQuantity = new JComboBox<>(new String[]{
                "1 box", "1 dozen", "1 pack", "1 unit"
        });

        JCheckBox chkDiscontinued = new JCheckBox("Discontinued");
        JTextField txtImagePath = new JTextField();

        // Thêm label + field vào dialog
        dialog.add(new JLabel("Shop ID:"));
        dialog.add(txtShopID);
        dialog.add(new JLabel("Category ID:"));
        dialog.add(txtCatgID);
        dialog.add(new JLabel("Name:"));
        dialog.add(txtName);
        dialog.add(new JLabel("Unit Price:"));
        dialog.add(txtUnitPrice);
        dialog.add(new JLabel("Unit In Stock:"));
        dialog.add(txtUnitInStock);
        dialog.add(new JLabel("Quantity Per Unit:"));
        dialog.add(cbQuantity);
        dialog.add(new JLabel("Discontinued:"));
        dialog.add(chkDiscontinued);
        dialog.add(new JLabel("Image Path:"));
        dialog.add(txtImagePath);

        // Nút Save và Cancel
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(e -> {
            // Lấy dữ liệu từ form
            String productID = new ProductRepository().generateNextProductID();
            String catgID = txtCatgID.getText();
            String name = txtName.getText();
            double unitPrice = Double.parseDouble(txtUnitPrice.getText());
            int unitInStock = Integer.parseInt(txtUnitInStock.getText());
            String quantityPerUnit = (String) cbQuantity.getSelectedItem();
            boolean discontinued = chkDiscontinued.isSelected();
            String imagePath = txtImagePath.getText();

            products newProduct = new products(productID, shopId, catgID, name, unitPrice, unitInStock, quantityPerUnit, discontinued, imagePath);

            // ps
            boolean success = new ProductRepository().insertProduct(newProduct);
            if (success) {
                JOptionPane.showMessageDialog(dialog, "Product added successfully!");
            }

            dialog.dispose();
            refreshTable(model, shopId);
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.add(btnSave);
        dialog.add(btnCancel);

        dialog.setMinimumSize(new Dimension(500, 400));
        dialog.setSize(600, 450);

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    public static void handleEventEditProduct(JTable table, DefaultTableModel model, String shopId, Component parent) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(parent, "Choose a product to edit!");
            return;
        }

        String productID = (String) model.getValueAt(row, 0);
        String catagID = (String) model.getValueAt(row, 1);
        String name = (String) model.getValueAt(row, 2);
        double unitPrice = (double) model.getValueAt(row, 3);
        int unitInStock = (int) model.getValueAt(row, 4);
        String quantityPerUnit = (String) model.getValueAt(row, 5);

        // Tạo dialog sửa
        JTextField txtCatgID = new JTextField(catagID);
        JTextField txtName = new JTextField(name);
        JTextField txtUnitPrice = new JTextField(String.valueOf(unitPrice));
        JTextField txtUnitInStock = new JTextField(String.valueOf(unitInStock));
        JComboBox<String> cbQuantity = new JComboBox<>(new String[]{"1 box", "1 dozen", "1 pack", "1 unit"});
        cbQuantity.setSelectedItem(quantityPerUnit);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Category ID:"));
        panel.add(txtCatgID);
        panel.add(new JLabel("Name:"));
        panel.add(txtName);
        panel.add(new JLabel("Unit Price:"));
        panel.add(txtUnitPrice);
        panel.add(new JLabel("Unit In Stock:"));
        panel.add(txtUnitInStock);
        panel.add(new JLabel("Quantity Per Unit:"));
        panel.add(cbQuantity);

        int result = JOptionPane.showConfirmDialog(parent, panel, "Edit Product", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            ProductRepository repo = new ProductRepository();
            repo.updateProduct(productID, txtName.getText(),
                    Double.parseDouble(txtUnitPrice.getText()),
                    Integer.parseInt(txtUnitInStock.getText()),
                    (String) cbQuantity.getSelectedItem());
            JOptionPane.showMessageDialog(parent, "Product updated successfully!");
            refreshTable(model, shopId);
        }
    }

    public static void handeEventDeleteProduct(JTable table, DefaultTableModel model, String shopId, Component parent) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(parent, "Choose a product to delete!");
            return;
        }
        String productID = (String) model.getValueAt(row, 0);
        boolean success = new ProductRepository().deleteProduct(productID);
        if (success) {
            JOptionPane.showMessageDialog(parent, "Product deleted successfully!");
            refreshTable(model, shopId);
        }
    }


    //--------------------- handleAction for ConfirmPanel------------------
    public static void handleConfirmAll(JTable table, DefaultTableModel model, String shopId, Component parent) {
        int rowCount = table.getRowCount();
        if (rowCount == 0) {
            JOptionPane.showMessageDialog(parent, "No orders to confirm!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(parent, "Are you sure you want to confirm all orders?", "Confirm All", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            OrderReponsitory orderRepo = new OrderReponsitory();
            boolean allSuccess = true;
            for (int i = 0; i < rowCount; i++) {
                String orderID = (String) model.getValueAt(i, 0);
                boolean success = orderRepo.updateOrderStatus(orderID, "CONFIRMED");
                if (!success) {
                    allSuccess = false;
                    JOptionPane.showMessageDialog(parent, "Failed to confirm order: " + orderID);
                }
            }
            if (allSuccess) {
                JOptionPane.showMessageDialog(parent, "All orders confirmed successfully!");
            }
            // refresh table
            refreshTable(model, shopId);
        }

    }

    //--------------------- handleAction for StatusPanel------------------

    public static void showOrdersStatus(JTable table, DefaultTableModel model, String shopId, String status, Component parent) {
        OrderReponsitory orderRepo = new OrderReponsitory();
        List<order> orderList = orderRepo.getOrdersWithProducts(status, shopId);

        // clear dữ liệu cũ
        model.setRowCount(0);

        for (order o : orderList) {
            model.addRow(new Object[]{
                    o.getOrderID(),
                    o.getCustomerID(),
                    o.getShipperID(),
                    o.getOrderDate(),
                    o.getShippedDate()
            });
        }

        // cập nhật lại sự kiện double click với danh sách mới
        handleDClickRowTable(table,
                orderList,
                o -> o.getOrderID(),
                o -> showOrderDetail(o, model, shopId, parent)
        );

    }


    //----------------------handleAction for RevenuePanel------------------

    public static void showQtySold(String shopID, JPanel mainPanel) {
        DefaultTableModel model;
        JTable table;

        String[] columnNames = {"Product ID", "CategoryID", "Name", "Unit In Stock", "Quantity Sold"};

        ProductRepository productRepo = new ProductRepository();
        List<products> productList = productRepo.getQuantitySold(shopID);

        Object[][] data = new Object[productList.size()][5];

        for (int i = 0; i < productList.size(); i++) {
            products p = productList.get(i);
            data[i][0] = p.getProductID();
            data[i][1] = p.getCatgID();
            data[i][2] = p.getName();
            data[i][3] = p.getUnitInStock();
            data[i][4] = p.getQtySold();
        }

        mainPanel.removeAll();
        model = new DefaultTableModel(data, columnNames);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}


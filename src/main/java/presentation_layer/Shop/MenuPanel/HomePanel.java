package presentation_layer.Shop.MenuPanel;

import presentation_layer.mdl.RatioSplitPanel;
import repository_layer.ProductRepository;
import model_layer.products;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HomePanel extends JPanel {

    private String id;

    DefaultTableModel model;
    JTable table;

    public HomePanel(String id) {
        this.id = id;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        initUI();
    }

    public void initUI() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        initTable(tablePanel);
        JPanel sidePanel = new JPanel(new BorderLayout());
        initControl(sidePanel);

        sidePanel.setPreferredSize(new Dimension(300, 0));

        RatioSplitPanel splitPanel = new RatioSplitPanel(tablePanel, sidePanel);
        add(splitPanel, BorderLayout.CENTER);


    }

    public void initTable(JPanel tablePanel) {
        ProductRepository productRepo = new ProductRepository();
        List<products> productList = productRepo.findByShopID(id);

        String[] columnNames = {"Product ID", "Name", "Unit Price", "Unit In Stock", "Quantity Per Unit"};

        Object[][] data = new Object[productList.size()][5];

        for (int i = 0; i < productList.size(); i++) {
            products p = productList.get(i);
            data[i][0] = p.getProductID();
            data[i][1] = p.getName();
            data[i][2] = p.getUnitPrice();
            data[i][3] = p.getUnitInStock();
            data[i][4] = p.getQuantityPerUnit();
        }

        model = new DefaultTableModel(data, columnNames);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
    }

    public void initControl(JPanel sidePanel) {
        JButton btnAdd = new JButton("Add Product");
        JButton btnEdit = new JButton("Edit Product");
        JButton btnRemove = new JButton("Delete Product");


        JPanel controlPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        controlPanel.add(btnAdd);
        controlPanel.add(btnEdit);
        controlPanel.add(btnRemove);

        sidePanel.add(controlPanel, BorderLayout.NORTH);

        btnAdd.addActionListener(e -> handleEventAddProduct(id));
        btnEdit.addActionListener(e -> handleEventEditProduct());
        btnRemove.addActionListener(e -> handeEventDeleteProduct());
    }

    public void handleEventAddProduct(String shopId) {
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

            products newProduct = new products(productID, this.id, catgID, name, unitPrice, unitInStock, quantityPerUnit, discontinued, imagePath);

            // ps
            boolean success = new ProductRepository().insertProduct(newProduct);
            if (success) {
                JOptionPane.showMessageDialog(dialog, "Product added successfully!");
            }

            dialog.dispose();
            refreshTable();
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.add(btnSave);
        dialog.add(btnCancel);

        dialog.setMinimumSize(new Dimension(500, 400));
        dialog.setSize(600, 450);

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    public void handleEventEditProduct() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Choose a product to edit!");
            return;
        }

        String productID = (String) model.getValueAt(row, 0);
        String name = (String) model.getValueAt(row, 1);
        double unitPrice = (double) model.getValueAt(row, 2);
        int unitInStock = (int) model.getValueAt(row, 3);
        String quantityPerUnit = (String) model.getValueAt(row, 4);

        // Tạo dialog sửa
        JTextField txtName = new JTextField(name);
        JTextField txtUnitPrice = new JTextField(String.valueOf(unitPrice));
        JTextField txtUnitInStock = new JTextField(String.valueOf(unitInStock));
        JComboBox<String> cbQuantity = new JComboBox<>(new String[]{"1 box", "1 dozen", "1 pack", "1 unit"});
        cbQuantity.setSelectedItem(quantityPerUnit);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Name:"));
        panel.add(txtName);
        panel.add(new JLabel("Unit Price:"));
        panel.add(txtUnitPrice);
        panel.add(new JLabel("Unit In Stock:"));
        panel.add(txtUnitInStock);
        panel.add(new JLabel("Quantity Per Unit:"));
        panel.add(cbQuantity);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Product", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            ProductRepository repo = new ProductRepository();
            repo.updateProduct(productID, txtName.getText(),
                    Double.parseDouble(txtUnitPrice.getText()),
                    Integer.parseInt(txtUnitInStock.getText()),
                    (String) cbQuantity.getSelectedItem());
            JOptionPane.showMessageDialog(this, "Product updated successfully!");
            refreshTable();
        }
    }

    public void handeEventDeleteProduct() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Choose a product to delete!");
            return;
        }
        String productID = (String) model.getValueAt(row, 0);
        boolean success = new ProductRepository().deleteProduct(productID);
        if (success) {
            JOptionPane.showMessageDialog(this, "Product deleted successfully!");
            refreshTable();
        }
    }

    public void refreshTable() {
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

}

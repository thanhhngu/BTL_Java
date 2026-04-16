package presentation_layer.Shop.MenuPanel;

import presentation_layer.mdl.RatioSplitPanel;
import repository_layer.ProductRepository;
import model_layer.products;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static presentation_layer.mdl.HandleAction.*;

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

        btnAdd.addActionListener(e -> handleEventAddProduct(table, model, this.id, this));
        btnEdit.addActionListener(e -> handleEventEditProduct(table, model, this.id, this));
        btnRemove.addActionListener(e -> handeEventDeleteProduct(table, model, this.id, this));
    }



}

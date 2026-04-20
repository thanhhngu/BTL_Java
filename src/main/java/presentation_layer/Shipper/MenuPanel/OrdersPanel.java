package presentation_layer.Shipper.MenuPanel;

import model_layer.order;
import presentation_layer.mdl.RatioSplitPanel;
import repository_layer.OrderReponsitory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static presentation_layer.mdl.HandleAction.handleDClickRowTable;
import static presentation_layer.mdl.HandleAction.showOrderDetailForShipper;

public class OrdersPanel extends JPanel {

    public String id;

    DefaultTableModel model;
    JTable table;

    public OrdersPanel(String id) {
        this.id = id;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        initUI();
    }

    public void initUI() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        initTable(tablePanel);
        JPanel sidePanel = new JPanel(new BorderLayout());
        initControl(sidePanel, tablePanel);

        sidePanel.setPreferredSize(new Dimension(300, 0));

        RatioSplitPanel splitPanel = new RatioSplitPanel(tablePanel, sidePanel);
        add(splitPanel, BorderLayout.CENTER);
    }

    public void initTable(JPanel tablePanel) {
        OrderReponsitory orderRepo = new OrderReponsitory();
        List<order> orderList = orderRepo.getOrdersWithProductsForShipper("SHIPPING", id);

        String[] columnNames = {"orderID", "CustomerID", "shipperID", "orderDate", "AddressID"};

        Object[][] data = new Object[orderList.size()][5];

        for (int i = 0; i < orderList.size(); i++) {
            order o = orderList.get(i);
            data[i][0] = o.getOrderID();
            data[i][1] = o.getCustomerID();
            data[i][2] = o.getShipperID();
            data[i][3] = o.getOrderDate();
            data[i][4] = o.getAddressID();
        }

        model = new DefaultTableModel(data, columnNames);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        handleDClickRowTable(table,
                orderList,
                o -> o.getOrderID(),
                o -> showOrderDetailForShipper(o, table, model, this.id, this, "DELIVERED")
        );
    }

    public void initControl(JPanel sidePanel, JPanel tablePanel) {
        JButton btnRefesh = new JButton("Refresh");

        JPanel controlPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        controlPanel.add(btnRefesh);

        sidePanel.add(controlPanel, BorderLayout.NORTH);


        btnRefesh.addActionListener(e -> {
            model.setRowCount(0);
            initTable(tablePanel);
            table.repaint();
        });
    }
}

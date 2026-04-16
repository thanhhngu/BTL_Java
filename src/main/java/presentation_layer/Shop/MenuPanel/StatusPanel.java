package presentation_layer.Shop.MenuPanel;

import model_layer.order;
import presentation_layer.mdl.RatioSplitPanel;
import repository_layer.OrderReponsitory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static presentation_layer.mdl.HandleAction.*;

public class StatusPanel extends JPanel {

    public String id;

    DefaultTableModel model;
    JTable table;

    public StatusPanel(String id) {
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
        OrderReponsitory orderRepo = new OrderReponsitory();
        List<order> orderList = orderRepo.getOrdersWithProducts("DELIVERED", id);

        String[] columnNames = {"orderID", "customerID", "shipperID", "orderDate", "shippedDate"};

        Object[][] data = new Object[orderList.size()][5];

        for (int i = 0; i < orderList.size(); i++) {
            order o = orderList.get(i);
            data[i][0] = o.getOrderID();
            data[i][1] = o.getCustomerID();
            data[i][2] = o.getShipperID();
            data[i][3] = o.getOrderDate();
            data[i][4] = o.getShippedDate();
        }

        model = new DefaultTableModel(data, columnNames);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        handleDClickRowTable(table,
                orderList,
                o -> o.getOrderID(),
                o -> showOrderDetail(o, model, this.id, this)
        );
    }

    public void initControl(JPanel sidePanel) {
        JButton btnDelivered = new JButton("Show Delivered");
        JButton btnShipping = new JButton("Show Shipping");

        JPanel controlPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        controlPanel.add(btnDelivered);
        controlPanel.add(btnShipping);

        sidePanel.add(controlPanel, BorderLayout.NORTH);

        btnDelivered.addActionListener(e -> showDelivered(table, model, this.id, "DELIVERED", this));
        btnShipping.addActionListener(e -> showShipping(table, model, this.id, "SHIPPING", this));


    }

}

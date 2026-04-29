package presentation_layer.Shipper.MenuPanel;

import presentation_layer.Style.StyledTable;
import service_layer.ShipperService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Map;

public class WorkPerPanel extends JPanel {

    private final String shipperID;
    private final ShipperService shipperService;

    private StyledTable table;
    private DefaultTableModel model;

    public WorkPerPanel(String shipperID) {
        this.shipperID = shipperID;
        this.shipperService = new ShipperService();

        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("Thống kê phí vận chuyển theo tháng");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));

        String[] columns = {"Tháng", "Tổng phí vận chuyển"};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new StyledTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);

        add(lblTitle, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadData() {
        Map<String, Double> stats = shipperService.getMonthlyFreightStats(shipperID);
        model.setRowCount(0);

        DecimalFormat df = new DecimalFormat("#,##0");

        for (Map.Entry<String, Double> entry : stats.entrySet()) {
            model.addRow(new Object[]{
                    entry.getKey(), // "YYYY-MM"
                    df.format(entry.getValue()) + " VND"
            });
        }
    }

    public JTable getTable() {
        return table;
    }

    public DefaultTableModel getModel() {
        return model;
    }
}
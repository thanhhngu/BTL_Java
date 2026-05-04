package presentation_layer.Shop.MenuPanel;

import presentation_layer.Style.StyledTable;
import presentation_layer.mdl.RatioSplitPanel;
import repository_layer.OrderReponsitory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

import static presentation_layer.mdl.HandleAction.showQtySold;

public class RevenuePanel extends JPanel {
    private String id;
    DefaultTableModel model;
    StyledTable table;

    public RevenuePanel(String id) {
        this.id = id;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        initUI();
    }

    public void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setAlignmentX(CENTER_ALIGNMENT);
        initMain(mainPanel);
        JPanel sidePanel = new JPanel(new BorderLayout());
        initControl(sidePanel, mainPanel);

        sidePanel.setPreferredSize(new Dimension(300, 0));

        RatioSplitPanel splitPanel = new RatioSplitPanel(mainPanel, sidePanel);
        add(splitPanel, BorderLayout.CENTER);

    }

    public void initMain(JPanel mainPanel) {
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        headerPanel.setBackground(Color.WHITE);

        JLabel lblTotal = new JLabel("Tổng doanh thu:");
        Double totalRevenue = new OrderReponsitory().getTotalAmount(this.id, null);
        JLabel lblRevenueValue = new JLabel(String.format("$%,.2f", totalRevenue));

        lblTotal.setFont(new Font("Arial", Font.BOLD, 26));
        lblRevenueValue.setFont(new Font("Arial", Font.BOLD, 26));
        lblRevenueValue.setForeground(new Color(0, 153, 76));

        headerPanel.add(lblTotal);
        headerPanel.add(lblRevenueValue);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        String[] columns = {"Month", "(Monthly Revenue)"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new StyledTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Chi tiết doanh thu theo tháng"));

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        LocalDate now = LocalDate.now();
        LocalDate minDate = new OrderReponsitory().getMinOrderDate(this.id);

        if (minDate != null) {
            LocalDate current = LocalDate.of(now.getYear(), now.getMonthValue(), 1);
            LocalDate min = LocalDate.of(minDate.getYear(), minDate.getMonthValue(), 1);

            model.setRowCount(0);

            while (!current.isBefore(min)) {
                double monthlyRevenue = new OrderReponsitory().getTotalAmount(this.id, current);

                String monthLabel = String.format("%02d/%d", current.getMonthValue(), current.getYear());
                String revenueValue = String.format("$%,.2f", monthlyRevenue);

                model.addRow(new Object[]{monthLabel, revenueValue});

                current = current.minusMonths(1);
            }
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public StyledTable getTable() {
        return table;
    }

    public DefaultTableModel getModel() {
        return model;
    }

    public void initControl(JPanel sidePanel, JPanel mainPanel) {
        JButton btnQtySold = new JButton("Số lượng đã bán");
        JButton btnReneuve = new JButton("Doanh thu");

        JPanel controlPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        controlPanel.add(btnReneuve);
        controlPanel.add(btnQtySold);

        sidePanel.add(controlPanel, BorderLayout.NORTH);

        btnReneuve.addActionListener(e -> initMain(mainPanel));
        btnQtySold.addActionListener(e -> showQtySold(this.id, mainPanel));
    }
}

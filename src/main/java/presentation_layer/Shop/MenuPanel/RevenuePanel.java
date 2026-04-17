package presentation_layer.Shop.MenuPanel;

import presentation_layer.mdl.RatioSplitPanel;
import repository_layer.OrderReponsitory;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

import static presentation_layer.mdl.HandleAction.showQtySold;

public class RevenuePanel extends JPanel {
    private String id;

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

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JLabel lblTotal = new JLabel("Total Revenue:");
        Double totalRevenue = new OrderReponsitory().getTotalAmount(this.id, null);
        JLabel lblRevenueValue = new JLabel(String.format("$%,.2f", totalRevenue));

        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblRevenueValue.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblRevenueValue.setForeground(new Color(0, 153, 76));

        headerPanel.add(lblTotal);
        headerPanel.add(lblRevenueValue);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        LocalDate now = LocalDate.now();
        LocalDate minDate = new OrderReponsitory().getMinOrderDate(this.id);

        if (minDate != null) {
            LocalDate current = LocalDate.of(now.getYear(), now.getMonthValue(), 1);
            LocalDate min = LocalDate.of(minDate.getYear(), minDate.getMonthValue(), 1);

            while (!current.isBefore(min)) {
                double monthlyRevenue = new OrderReponsitory()
                        .getTotalAmount(this.id, current);

                JLabel lbl = new JLabel(String.format(
                        "Revenue %02d/%d: $%,.2f",
                        current.getMonthValue(),
                        current.getYear(),
                        monthlyRevenue
                ));

                lbl.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                lbl.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

                contentPanel.add(lbl);

                current = current.minusMonths(1);
            }
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Monthly Revenue"));

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void initControl(JPanel sidePanel, JPanel mainPanel) {
        JButton btnQtySold = new JButton("Quantity Sold");
        JButton btnReneuve = new JButton("Revenue");

        JPanel controlPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        controlPanel.add(btnReneuve);
        controlPanel.add(btnQtySold);

        sidePanel.add(controlPanel, BorderLayout.NORTH);

        btnReneuve.addActionListener(e -> initMain(mainPanel));
        btnQtySold.addActionListener(e -> showQtySold(this.id, mainPanel));
    }
}

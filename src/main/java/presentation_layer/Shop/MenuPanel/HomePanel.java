package presentation_layer.Shop.MenuPanel;

import repository_layer.ProductRepository;
import model_layer.products;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HomePanel extends JPanel {

    private String id;

    public HomePanel(String id) {
        this.id = id;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initUI();
    }

    public void initUI() {
        initTable();
    }

    public void initTable() {
        ProductRepository productRepo = new ProductRepository();
        List<products> productList = productRepo.findByShopID(id);

        System.out.println(productList.size());

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

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        // Tạo panel chứa table để control size
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Tạo panel trống cho phần còn lại (1/3 width)
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(Color.LIGHT_GRAY); // Để dễ nhận biết, có thể xóa sau

        // Sử dụng JSplitPane để chia tỷ lệ 2:1
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablePanel, emptyPanel);
        splitPane.setDividerLocation(0.67); // 2/3 cho table
        splitPane.setResizeWeight(0.67); // Giữ tỷ lệ khi resize
        splitPane.setDividerSize(0); // Ẩn divider
        splitPane.setOneTouchExpandable(false); // Tắt nút expand
        splitPane.setContinuousLayout(true); // Layout liên tục khi resize

        // Thêm splitPane vào center
        add(splitPane, BorderLayout.CENTER);

        // Override để set kích thước khi component được hiển thị
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                tablePanel.setPreferredSize(new Dimension((int)(panelWidth * 0.67), panelHeight));
                revalidate();
            }
        });
    }

}

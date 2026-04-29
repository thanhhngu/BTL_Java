package presentation_layer.Style;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class StyledTable extends JTable {

    // Constructor nhận trực tiếp Model từ bên ngoài truyền vào
    public StyledTable(DefaultTableModel model) {
        super(model);
        initTableStyle(); // Gọi hàm làm đẹp sau khi đã gán model
    }

    // Hàm chứa toàn bộ cài đặt giao diện
    private void initTableStyle() {
        // 1. Cài đặt cơ bản: Font, Chiều cao, Chọn 1 dòng
        setFont(new Font("Arial", Font.PLAIN, 13));
        setRowHeight(30);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 2. Xóa toàn bộ viền bảng (Không viền)
        setShowGrid(false); // Tắt viền lưới mặc định
        setIntercellSpacing(new Dimension(0, 0)); // Xóa khoảng cách giữa các ô
        setBorder(BorderFactory.createEmptyBorder()); // Xóa viền ngoài của bảng

        // 3. Cài đặt màu khi chọn dòng
        setSelectionBackground(new Color(123, 207, 255)); // Màu xanh lơ nhạt
        setSelectionForeground(Color.BLACK); // Chữ màu đen khi chọn

        // 4. Cài đặt Header (Tiêu đề)
        getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        getTableHeader().setReorderingAllowed(false); // Không cho kéo thả cột
        getTableHeader().setBackground(new Color(240, 240, 240)); // Xám nhạt
        getTableHeader().setBorder(BorderFactory.createEmptyBorder()); // Xóa viền header
    }

    // 5. Tô màu dòng chẵn/dòng lẻ (Zebra striping)
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component comp = super.prepareRenderer(renderer, row, column);

        // Chỉ tô màu xen kẽ nếu dòng đó KHÔNG được chọn
        if (!isCellSelected(row, column)) {
            comp.setBackground(row % 2 == 0 ? Color.WHITE : Color.decode("#D9D9D9"));
            comp.setForeground(Color.BLACK);
        }
        return comp;
    }

    // 6. Xóa viền của thanh cuộn JScrollPane chứa bảng này
    @Override
    protected void configureEnclosingScrollPane() {
        super.configureEnclosingScrollPane();
        if (getParent() instanceof javax.swing.JViewport && getParent().getParent() instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) getParent().getParent();
            scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Xóa viền của JScrollPane
            scrollPane.getViewport().setBackground(Color.decode("#F5F5F5")); // Màu nền của vùng cuộn
        }
    }
}
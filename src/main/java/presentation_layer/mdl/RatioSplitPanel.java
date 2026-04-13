package presentation_layer.mdl;

import javax.swing.*;
import java.awt.*;

public class RatioSplitPanel extends JPanel {

    public RatioSplitPanel(JPanel mainPanel, JPanel sidePanel) {
        setLayout(new BorderLayout());

        // Thêm viền cho 2 panel
        mainPanel.setBorder(BorderFactory.createTitledBorder("main"));
        sidePanel.setBorder(BorderFactory.createTitledBorder("side"));

        // JSplitPane chia ngang
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainPanel, sidePanel);

        // Đặt tỉ lệ: mainPanel chiếm 2/3, sidePanel chiếm 1/3
        splitPane.setResizeWeight(0.8); // 80% cho panel bên trái
        splitPane.setDividerSize(5);     // độ dày đường chia
        splitPane.setContinuousLayout(true); // cập nhật liên tục khi kéo

        add(splitPane, BorderLayout.CENTER);
    }
}
package presentation_layer.mdl;

import model_layer.products;
import org.apache.poi.ss.usermodel.*;
import repository_layer.ProductRepository;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelService {
    public List<products> selectAndImportFile(Component parent, String shopID) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel files", "xlsx", "xls"));

        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            return readExcel(file, shopID);
        }
        return null;
    }

    private List<products> readExcel(File file, String shopID) {
        List<products> list = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                JOptionPane.showMessageDialog(null, "Ignored file!");
                return null;
            }

            String cellA1 = formatter.formatCellValue(headerRow.getCell(0));
            if (!"Product".equalsIgnoreCase(cellA1)) {
                JOptionPane.showMessageDialog(null, "Error! Not file Product.");
                return null;
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                products p = new products();
                ProductRepository repo = new ProductRepository();

                p.setProductID(repo.generateNextProductID());
                p.setCatgID(formatter.formatCellValue(row.getCell(1)));
                p.setName(formatter.formatCellValue(row.getCell(2)));
                p.setShopID(shopID);
                p.setUnitPrice(Double.parseDouble(formatter.formatCellValue(row.getCell(3))));
                p.setUnitInStock(Integer.parseInt(formatter.formatCellValue(row.getCell(4))));

                p.setQuantityPerUnit(formatter.formatCellValue(row.getCell(5)));
                p.setDiscontinued(Boolean.parseBoolean(formatter.formatCellValue(row.getCell(6))));
                p.setImagePath(formatter.formatCellValue(row.getCell(7)));

                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

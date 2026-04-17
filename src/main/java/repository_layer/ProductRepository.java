package repository_layer;
import config.*;
import model_layer.products;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.DBconnection;

public class ProductRepository {

    public List<products> findAll() {
        List<products> list = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE discontinued = 0";

        try (Connection con =DBconnection.openConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products p = new products();
                p.setProductID(rs.getString("ProductID"));
                p.setName(rs.getString("name"));
                p.setShopID(rs.getString("shopID"));
                p.setCatgID(rs.getString("catgID"));
                p.setUnitPrice(rs.getDouble("unitPrice"));
                p.setQuantityPerUnit(rs.getString("quantityPerUnit"));
                p.setUnitInStock(rs.getInt("unitInStock"));
                p.setDiscontinued(rs.getBoolean("discontinued"));
                p.setImagePath(rs.getString("imagePath"));
                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<products> findByName(String keyword) {
        List<products> list = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE discontinued = 0 AND productName LIKE ?";

        try (Connection con = DBconnection.openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products p = new products();
                    p.setProductID(rs.getString("ProductID"));
                    p.setName(rs.getString("name"));
                    p.setShopID(rs.getString("shopID"));
                    p.setCatgID(rs.getString("catgID"));
                    p.setUnitPrice(rs.getDouble("unitPrice"));
                    p.setQuantityPerUnit(rs.getString("quantityPerUnit"));
                    p.setUnitInStock(rs.getInt("unitInStock"));
                    p.setDiscontinued(rs.getBoolean("discontinued"));
                    p.setImagePath(rs.getString("imagePath"));
                    list.add(p);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<products> findByShopID(String keyword) {
        List<products> list = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE discontinued = 0 AND shopID = ? order by productID desc";

        try (Connection con = DBconnection.openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, keyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products p = new products();
                    p.setProductID(rs.getString("ProductID"));
                    p.setName(rs.getString("name"));
                    p.setShopID(rs.getString("shopID"));
                    p.setCatgID(rs.getString("catgID"));
                    p.setUnitPrice(rs.getDouble("unitPrice"));
                    p.setQuantityPerUnit(rs.getString("quantityPerUnit"));
                    p.setUnitInStock(rs.getInt("unitInStock"));
                    p.setDiscontinued(rs.getBoolean("discontinued"));
                    p.setImagePath(rs.getString("imagePath"));
                    list.add(p);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public String generateNextProductID() {
        //String sql = "SELECT TOP 1 productID FROM Product ORDER BY productID DESC";
        String sql = "SELECT productID FROM Product ORDER BY productID DESC LIMIT 1;";


        try (Connection conn = DBconnection.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastID = rs.getString("productID"); // CU015
                int number = Integer.parseInt(lastID.substring(2));
                return String.format("P%03d", number + 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "P001";
    }

    public boolean insertProduct(products product) {
        String sql = "INSERT INTO Product(productID, shopID, catgID, name, unitPrice, unitInStock, quantityPerUnit, discontinued, imagePath) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBconnection.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getProductID());
            ps.setString(2, product.getShopID());
            ps.setString(3, product.getCatgID());
            ps.setString(4, product.getName());
            ps.setDouble(5, product.getUnitPrice());
            ps.setInt(6, product.getUnitInStock());
            ps.setString(7, product.getQuantityPerUnit());
            ps.setBoolean(8, product.isDiscontinued());
            ps.setString(9, product.getImagePath());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateProduct(String productID, String name, double unitPrice, int unitInStock, String quantityPerUnit) {
        String sql = "UPDATE Product SET name=?, unitPrice=?, unitInStock=?, quantityPerUnit=? WHERE productID=?";
        try (Connection conn = DBconnection.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, unitPrice);
            ps.setInt(3, unitInStock);
            ps.setString(4, quantityPerUnit);
            ps.setString(5, productID);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteProduct(String productID) {
        String sql = "DELETE FROM Product WHERE productID=?";
        try (Connection conn = DBconnection.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productID);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getImagePath(String productID) {

        String sql = "SELECT imagePath FROM Product WHERE productID=?";
        try (Connection conn = DBconnection.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String imagePath = rs.getString("imagePath");
                    return imagePath;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<products> getQuantitySold(String shopID) {
        List<products> list = new ArrayList<>();
        String sql = "SELECT p.productID, p.catgID, p.name, SUM(od.quantity) AS totalQuantity " +
                "FROM orders o " +
                "JOIN order_detail od ON o.orderID = od.orderID " +
                "JOIN product p ON p.productID = od.productID " +
                "WHERE p.shopID = ? AND o.status = 'DELIVERED'" +
                "GROUP BY p.productID, p.catgID, p.name " +
                "ORDER BY totalQuantity DESC";

        try (Connection conn = DBconnection.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, shopID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()){
                    products p = new products();
                    p.setProductID(rs.getString("productID"));
                    p.setCatgID(rs.getString("catgID"));
                    p.setName(rs.getString("name"));
                    p.setQtySold(rs.getInt("totalQuantity"));
                    list.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
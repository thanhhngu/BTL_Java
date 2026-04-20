package repository_layer;
import config.*;
import model_layer.products;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import config.DBconnection;

public class ProductRepository {

    private static final Logger LOGGER = Logger.getLogger(ProductRepository.class.getName());

    public List<products> getAllAvailableProducts() {
        List<products> list = new ArrayList<products>();

        String sql =
                "SELECT p.productID, p.shopID, p.catgID, p.name, p.unitPrice, p.unitInStock, " +
                        "p.quantityPerUnit, p.discontinued, p.imagePath, s.name AS shopName " +
                        "FROM Product p " +
                        "JOIN Shop s ON p.shopID = s.shopID " +
                        "WHERE p.discontinued = 0 " +
                        "ORDER BY p.productID";

        try (
                Connection con = DBconnection.openConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(mapRowToProduct(rs));
            }
        } catch (SQLException ex) {
            logSqlException("getAllAvailableProducts", ex);
        }

        return list;
    }

    public List<products> searchProductsByName(String keyword) {
        List<products> list = new ArrayList<products>();

        String sql =
                "SELECT p.productID, p.shopID, p.catgID, p.name, p.unitPrice, p.unitInStock, " +
                        "p.quantityPerUnit, p.discontinued, p.imagePath, s.name AS shopName " +
                        "FROM Product p " +
                        "JOIN Shop s ON p.shopID = s.shopID " +
                        "WHERE p.discontinued = 0 AND p.name LIKE ? " +
                        "ORDER BY p.productID";

        try (
                Connection con = DBconnection.openConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, "%" + (keyword == null ? "" : keyword.trim()) + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToProduct(rs));
                }
            }
        } catch (SQLException ex) {
            logSqlException("searchProductsByName", ex);
        }

        return list;
    }

    public List<products> getProductsByCategory(String catgID) {
        List<products> list = new ArrayList<products>();

        String sql =
                "SELECT p.productID, p.shopID, p.catgID, p.name, p.unitPrice, p.unitInStock, " +
                        "p.quantityPerUnit, p.discontinued, p.imagePath, s.name AS shopName " +
                        "FROM Product p " +
                        "JOIN Shop s ON p.shopID = s.shopID " +
                        "WHERE p.discontinued = 0 AND p.catgID = ? " +
                        "ORDER BY p.productID";

        try (
                Connection con = DBconnection.openConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, catgID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToProduct(rs));
                }
            }
        } catch (SQLException ex) {
            logSqlException("getProductsByCategory", ex);
        }

        return list;
    }

    private products mapRowToProduct(ResultSet rs) throws SQLException {
        products p = new products();
        p.setProductID(rs.getString("productID"));
        p.setShopID(rs.getString("shopID"));
        p.setShopName(rs.getString("shopName"));
        p.setCatgID(rs.getString("catgID"));
        p.setName(rs.getString("name"));

        double unitPrice = rs.getDouble("unitPrice");
        if (rs.wasNull()) {
            unitPrice = 0;
        }
        p.setUnitPrice(unitPrice);

        p.setUnitInStock(rs.getInt("unitInStock"));
        p.setQuantityPerUnit(rs.getString("quantityPerUnit"));
        p.setDiscontinued(rs.getBoolean("discontinued"));
        p.setImagePath(rs.getString("imagePath"));

        return p;
    }

    private void logSqlException(String context, SQLException ex) {
        LOGGER.log(Level.SEVERE,
                context + " failed. SQLState=" + ex.getSQLState() + ", ErrorCode=" + ex.getErrorCode(),
                ex);
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

    public boolean insertProduct(products p) {
        String checkSql = "SELECT unitInStock FROM Product WHERE name = ?";
        String updateSql = "UPDATE Product SET unitInStock = unitInStock + ? WHERE name = ?";
        String insertSql = "INSERT INTO Product(productID, shopID, catgID, name, unitPrice, unitInStock, quantityPerUnit, discontinued, imagePath) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBconnection.openConnection()) {
            try (PreparedStatement psCheck = con.prepareStatement(checkSql)) {
                psCheck.setString(1, p.getName());
                ResultSet rs = psCheck.executeQuery();

                if (rs.next()) {
                    try (PreparedStatement psUpdate = con.prepareStatement(updateSql)) {
                        psUpdate.setInt(1, p.getUnitInStock());
                        psUpdate.setString(2, p.getName());

                        return psUpdate.executeUpdate() > 0;
                    }
                } else {
                    try (PreparedStatement psInsert = con.prepareStatement(insertSql)) {
                        psInsert.setString(1, p.getProductID());
                        psInsert.setString(2, p.getShopID());
                        psInsert.setString(3, p.getCatgID());
                        psInsert.setString(4, p.getName());
                        psInsert.setDouble(5, p.getUnitPrice());
                        psInsert.setInt(6, p.getUnitInStock());
                        psInsert.setString(7, p.getQuantityPerUnit());
                        psInsert.setBoolean(8, p.isDiscontinued());
                        psInsert.setString(9, p.getImagePath());

                        return psInsert.executeUpdate() > 0;
                    }
                }
            }
        } catch (SQLException e) {
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

    public boolean updateProductUnitInStock(String productId) {
        String sql = "UPDATE Product p " +
                "JOIN order_detail od ON p.productID = od.productID " +
                "JOIN orders o ON o.orderID = od.orderID " +
                "SET p.unitInStock = p.unitInStock - od.quantity " +
                "WHERE o.status = 'CONFIRMED'";

        if (productId != null) {
            sql += " AND p.productID = ?";
        }

        try (Connection con = DBconnection.openConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            if (productId != null) {
                ps.setString(1, productId);
            }
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return false;
    }
}
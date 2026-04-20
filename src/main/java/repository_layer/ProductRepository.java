package repository_layer;

import config.DBconnection;
import model_layer.products;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
}

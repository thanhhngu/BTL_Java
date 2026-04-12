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
        String sql = "SELECT * FROM Product WHERE discontinued = 0 AND shopID = ?";

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
}
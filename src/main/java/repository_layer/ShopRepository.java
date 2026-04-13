package repository_layer;

import config.DBconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ShopRepository {

    public String generateNextShopID() {
        String sql = "SELECT TOP 1 shopID FROM Shop ORDER BY shopID DESC";

        try (Connection conn = DBconnection.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastID = rs.getString("shopID"); // ví dụ S015
                int number = Integer.parseInt(lastID.substring(1));
                return String.format("S%03d", number + 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "S001";
    }

    public boolean insertShop(String shopID, String name, String phone, String address, String email) {
        String sql = "INSERT INTO Shop(shopID, name, phone, address, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBconnection.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shopID);
            ps.setString(2, name);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setString(5, email);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


}
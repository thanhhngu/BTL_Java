package repository_layer;

import config.DBconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ShipperRepository {

    public String generateNextShipperID() {
        String sql = "SELECT TOP 1 shipperID FROM Shipper ORDER BY shipperID DESC";

        try (Connection conn = DBconnection.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastID = rs.getString("shipperID"); // SH015
                int number = Integer.parseInt(lastID.substring(2));
                return String.format("SH%03d", number + 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "SH001";
    }

    public boolean insertShipper(String shipperID, String name, String phone, String companyName) {
        String sql = "INSERT INTO Shipper(shipperID, name, phone, companyName) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBconnection.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shipperID);
            ps.setString(2, name);
            ps.setString(3, phone);
            ps.setString(4, companyName);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
package repository_layer;

import config.DBconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerRepository {

    public String generateNextCustomerID() {
        String sql = "SELECT TOP 1 customerID FROM Customer ORDER BY customerID DESC";

        try (Connection conn = DBconnection.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastID = rs.getString("customerID"); // CU015
                int number = Integer.parseInt(lastID.substring(2));
                return String.format("CU%03d", number + 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "CU001";
    }

    public boolean insertCustomer(String customerID, String name, String phone, int gender, String birthdate) {
        String sql = "INSERT INTO Customer(customerID, name, phone, gender, birthdate) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBconnection.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customerID);
            ps.setString(2, name);
            ps.setString(3, phone);
            ps.setInt(4, gender);
            ps.setString(5, birthdate);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
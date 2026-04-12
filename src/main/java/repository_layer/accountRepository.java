package repository_layer;

import config.DBconnection;
import model_layer.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class accountRepository {

    public account login(String username, String password, String role) {
        String sql = "SELECT * FROM Account WHERE username = ? AND password = ? AND role = ?";

        try (Connection conn = DBconnection.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                account acc = new account();
                acc.setAccountID(rs.getInt("accountID"));
                acc.setUsername(rs.getString("username"));
                acc.setPassword(rs.getString("password"));
                acc.setRole(rs.getString("role"));
                acc.setCustomerID(rs.getString("customerID"));
                acc.setShopID(rs.getString("shopID"));
                acc.setShipperID(rs.getString("shipperID"));
                return acc;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean existsUsername(String username) {
        String sql = "SELECT 1 FROM Account WHERE username = ?";

        try (Connection conn = DBconnection.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean insertAccount(account acc) {
        String sql = "INSERT INTO Account(username, password, role, customerID, shopID, shipperID) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBconnection.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, acc.getUsername());
            ps.setString(2, acc.getPassword());
            ps.setString(3, acc.getRole());

            if (acc.getCustomerID() != null) {
                ps.setString(4, acc.getCustomerID());
            } else {
                ps.setNull(4, java.sql.Types.VARCHAR);
            }

            if (acc.getShopID() != null) {
                ps.setString(5, acc.getShopID());
            } else {
                ps.setNull(5, java.sql.Types.VARCHAR);
            }

            if (acc.getShipperID() != null) {
                ps.setString(6, acc.getShipperID());
            } else {
                ps.setNull(6, java.sql.Types.VARCHAR);
            }

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
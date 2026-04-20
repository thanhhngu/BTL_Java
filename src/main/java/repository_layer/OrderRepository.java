package repository_layer;

import config.DBconnection;
import model_layer.CartItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderRepository {

    private static final Logger LOGGER = Logger.getLogger(OrderRepository.class.getName());

    public boolean insertOrder(String customerID,
                               String shopID,
                               String addressID,
                               String payID,
                               double amount,
                               double freight,
                               List<CartItem> items) {

        Connection con = null;

        try {
            con = DBconnection.openConnection();

            if (con == null) {
                LOGGER.severe("Không mở được kết nối database.");
                return false;
            }

            if (items == null || items.isEmpty()) {
                LOGGER.severe("Danh sách sản phẩm trống.");
                return false;
            }

            con.setAutoCommit(false);

            String orderID = generateNextOrderID(con);
            String paymentID = generateNextPaymentID(con);

            System.out.println("orderID = " + orderID);
            System.out.println("paymentID = " + paymentID);
            System.out.println("payID = " + payID);

            insertPayment(con, paymentID, amount, payID);
            insertOrderRow(con, orderID, customerID, shopID, addressID, paymentID, amount, freight);
            insertOrderDetails(con, orderID, items);

            con.commit();
            return true;

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "insertOrder failed", ex);

            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Rollback failed", e);
                }
            }

            return false;

        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Close connection failed", e);
                }
            }
        }
    }

    private void insertPayment(Connection con, String paymentID, double amount, String payID) throws SQLException {
        String sql = "INSERT INTO Payment(paymentID, amount, paymentDate, payID) "
                   + "VALUES (?, ?, GETDATE(), ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, paymentID);
            ps.setDouble(2, amount);
            ps.setString(3, payID);

            int row = ps.executeUpdate();
            if (row <= 0) {
                throw new SQLException("Insert Payment thất bại.");
            }
        }
    }

    private void insertOrderRow(Connection con,
                                String orderID,
                                String customerID,
                                String shopID,
                                String addressID,
                                String paymentID,
                                double amount,
                                double freight) throws SQLException {

        String sql = "INSERT INTO Orders "
                   + "(orderID, customerID, shipperID, orderDate, shippedDate, freight, addressID, paymentID, status, shopID, amount) "
                   + "VALUES (?, ?, NULL, GETDATE(), NULL, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, orderID);
            ps.setString(2, customerID);
            ps.setDouble(3, freight);
            ps.setString(4, addressID);
            ps.setString(5, paymentID);
            ps.setString(6, "PENDING");
            ps.setString(7, shopID);
            ps.setDouble(8, amount);

            int row = ps.executeUpdate();
            if (row <= 0) {
                throw new SQLException("Insert Orders thất bại.");
            }
        }
    }

    private void insertOrderDetails(Connection con, String orderID, List<CartItem> items) throws SQLException {
        String sql = "INSERT INTO Order_Detail(orderID, productID, unitPrice, quantity, discount) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (CartItem item : items) {
                if (item == null || item.getProduct() == null) {
                    throw new SQLException("CartItem hoặc Product bị null.");
                }

                ps.setString(1, orderID);
                ps.setString(2, item.getProduct().getProductID());
                ps.setDouble(3, item.getProduct().getUnitPrice());
                ps.setInt(4, item.getQuantity());
                ps.setDouble(5, 0);
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    private String generateNextOrderID(Connection con) throws SQLException {
        String sql = "SELECT MAX(orderID) AS maxID FROM Orders";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String maxID = rs.getString("maxID");

                if (maxID == null || maxID.trim().isEmpty()) {
                    return "O001";
                }

                int number = Integer.parseInt(maxID.substring(1));
                return String.format("O%03d", number + 1);
            }
        }

        return "O001";
    }

    private String generateNextPaymentID(Connection con) throws SQLException {
        String sql = "SELECT paymentID FROM Payment";

        int max = 0;

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("paymentID");

                try {
                    int number = Integer.parseInt(id.substring(3));
                    if (number > max) {
                        max = number;
                    }
                } catch (Exception e) {
                    // bỏ qua ID lỗi format
                }
            }
        }

        return String.format("PAY%03d", max + 1);
    }
}
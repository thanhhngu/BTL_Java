package repository_layer;

import config.DBconnection;
import model_layer.CartItem;
import model_layer.order;
import model_layer.order_detail;
import model_layer.products;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderReponsitory {
    private static final Logger LOGGER = Logger.getLogger(OrderReponsitory.class.getName());

    public List<order> getOrdersWithProducts(String status, String shopID) {
        Map<String, order> map = new LinkedHashMap<>();

        String sql = "SELECT o.*, p.*, od.quantity " +
                "FROM qldonhang.orders o " +
                "JOIN qldonhang.order_detail od ON o.orderID = od.orderID " +
                "JOIN qldonhang.product p ON p.productID = od.productID " +
                "WHERE o.status = ? ";

        if (shopID != null && !shopID.trim().isEmpty()) {
            sql += "AND p.shopID = ?";
        }
        try (Connection con = DBconnection.openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            if (shopID != null && !shopID.trim().isEmpty()) {
                ps.setString(2, shopID);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String orderID = rs.getString("orderID");

                // lấy order từ map (tránh duplicate)
                order o = map.get(orderID);

                if (o == null) {
                    o = new order();
                    o.setOrderID(orderID);
                    o.setCustomerID(rs.getString("customerID"));
                    o.setShipperID(rs.getString("shipperID"));

                    java.sql.Date orderDateSql = rs.getDate("orderDate");
                    if (orderDateSql != null) {
                        o.setOrderDate(orderDateSql.toLocalDate());
                    }

                    java.sql.Date shippedDateSql = rs.getDate("shippedDate");
                    if (shippedDateSql != null) {
                        o.setShippedDate(shippedDateSql.toLocalDate());
                    }

                    o.setAmount(rs.getDouble("amount"));
                    o.setFreight(rs.getDouble("freight"));
                    o.setAddressID(rs.getString("addressID"));
                    o.setPaymentID(rs.getString("paymentID"));
                    o.setStatus(rs.getString("status"));

                    o.setItems(new ArrayList<>());

                    map.put(orderID, o);
                }

                // tạo product
                products p = new products();
                p.setProductID(rs.getString("productID"));
                p.setName(rs.getString("name"));
                p.setUnitPrice(rs.getDouble("unitPrice"));

                // tạo order item (chứa quantity)
                order_detail item = new order_detail();
                item.setProduct(p);
                item.setQuantity(rs.getInt("quantity"));

                o.getItems().add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>(map.values());
    }

    public List<order> getOrdersWithProductsForShipper(String status, String shipperID) {
        Map<String, order> map = new LinkedHashMap<>();

        String sql = "SELECT o.*, p.*, od.quantity " +
                "FROM qldonhang.orders o " +
                "JOIN qldonhang.order_detail od ON o.orderID = od.orderID " +
                "JOIN qldonhang.product p ON p.productID = od.productID " +
                "WHERE o.status = ? ";

        if (shipperID != null && !shipperID.trim().isEmpty()) {
            sql += "AND o.shipperID = ?";
        }
        try (Connection con = DBconnection.openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            if (shipperID != null && !shipperID.trim().isEmpty()) {
                ps.setString(2, shipperID);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String orderID = rs.getString("orderID");

                // lấy order từ map (tránh duplicate)
                order o = map.get(orderID);

                if (o == null) {
                    o = new order();
                    o.setOrderID(orderID);
                    o.setCustomerID(rs.getString("customerID"));
                    o.setShipperID(rs.getString("shipperID"));

                    java.sql.Date orderDateSql = rs.getDate("orderDate");
                    if (orderDateSql != null) {
                        o.setOrderDate(orderDateSql.toLocalDate());
                    }

                    java.sql.Date shippedDateSql = rs.getDate("shippedDate");
                    if (shippedDateSql != null) {
                        o.setShippedDate(shippedDateSql.toLocalDate());
                    }

                    o.setAmount(rs.getDouble("amount"));
                    o.setFreight(rs.getDouble("freight"));
                    o.setAddressID(rs.getString("addressID"));
                    o.setPaymentID(rs.getString("paymentID"));
                    o.setStatus(rs.getString("status"));

                    o.setItems(new ArrayList<>());

                    map.put(orderID, o);
                }

                // tạo product
                products p = new products();
                p.setProductID(rs.getString("productID"));
                p.setName(rs.getString("name"));
                p.setUnitPrice(rs.getDouble("unitPrice"));

                // tạo order item (chứa quantity)
                order_detail item = new order_detail();
                item.setProduct(p);
                item.setQuantity(rs.getInt("quantity"));

                o.getItems().add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>(map.values());
    }

    public boolean updateOrderStatus(String orderID, String newStatus) {
        String sql = "UPDATE orders SET status = ? WHERE orderID = ?";

        try (Connection con = DBconnection.openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setString(2, orderID);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public double getTotalAmount(String ShopID, LocalDate date){
        String sql = "SELECT o.*, p.*, od.quantity " +
                "FROM qldonhang.orders o " +
                "JOIN qldonhang.order_detail od ON o.orderID = od.orderID " +
                "JOIN qldonhang.product p ON p.productID = od.productID " +
                "WHERE p.shopID = ? and o.status = ?";

        if (date != null) {
            sql += " AND Month(o.orderDate) = ? and Year(o.orderDate) = ?";
        }

        try (Connection con = DBconnection.openConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ShopID);
            ps.setString(2, "DELIVERED");
            if (date != null) {
                ps.setInt(3, date.getMonthValue());
                ps.setInt(4, date.getYear());
            }

            ResultSet rs = ps.executeQuery();
            double totalAmount = 0;

            while (rs.next()) {
                double amount = rs.getDouble("amount");
                if (amount > 0) {
                    totalAmount += amount;
                }
            }

            return totalAmount;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;

        }
    }

    public LocalDate getMinOrderDate(String shopID) {
        String sql = "SELECT MIN(o.orderDate) AS minDate " +
                "FROM qldonhang.orders o " +
                "JOIN qldonhang.order_detail od ON o.orderID = od.orderID " +
                "JOIN qldonhang.product p ON p.productID = od.productID " +
                "WHERE p.shopID = ?";

        try (Connection con = DBconnection.openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, shopID);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                java.sql.Date minDateSql = rs.getDate("minDate");
                if (minDateSql != null) {
                    return minDateSql.toLocalDate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Double> getMonthlyFreightStats(String shipperID) {
        Map<String, Double> monthlyStats = new LinkedHashMap<>();
        String sql = "SELECT YEAR(o.shippedDate) AS year, MONTH(o.shippedDate) AS month, SUM(o.freight) AS totalFreight " +
                "FROM qldonhang.orders o " +
                "WHERE o.shipperID = ? AND o.status = 'DELIVERED' AND o.shippedDate IS NOT NULL " +
                "GROUP BY YEAR(o.shippedDate), MONTH(o.shippedDate) " +
                "ORDER BY year DESC, month DESC";

        try (Connection con = DBconnection.openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, shipperID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int year = rs.getInt("year");
                int month = rs.getInt("month");
                double totalFreight = rs.getDouble("totalFreight");
                String yearMonth = String.format("%d-%02d", year, month);
                monthlyStats.put(yearMonth, totalFreight);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return monthlyStats;
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
}

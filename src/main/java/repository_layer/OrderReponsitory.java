package repository_layer;

import config.DBconnection;
import model_layer.order;
import model_layer.order_detail;
import model_layer.products;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderReponsitory {
    public List<order> getOrdersWithProducts(String status, String shopID) {
        Map<String, order> map = new LinkedHashMap<>();

        String sql = "SELECT o.*, p.*, od.quantity " +
                "FROM qldonhang.orders o " +
                "JOIN qldonhang.order_detail od ON o.orderID = od.orderID " +
                "JOIN qldonhang.product p ON p.productID = od.productID " +
                "WHERE o.status = ? AND p.shopID = ?";

        try (Connection con = DBconnection.openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, shopID);

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
}

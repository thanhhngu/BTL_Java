package repository_layer;

import config.DBconnection;
import model_layer.OrderHistoryDetailItem;
import model_layer.OrderHistoryItem;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HistoryRepository {

    public List<OrderHistoryItem> getOrdersByCustomer(String customerID) {
        List<OrderHistoryItem> list = new ArrayList<>();

        String sql = "SELECT orderID, customerID, shipperID, orderDate, shippedDate, freight, " +
                "addressID, paymentID, status, shopID, amount " +
                "FROM Orders " +
                "WHERE customerID = ? " +
                "ORDER BY orderDate DESC";

        try (
                Connection con = DBconnection.openConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, customerID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderHistoryItem item = new OrderHistoryItem();

                    item.setOrderID(rs.getString("orderID"));
                    item.setCustomerID(rs.getString("customerID"));
                    item.setShipperID(rs.getString("shipperID"));

                    Date orderDate = rs.getDate("orderDate");
                    if (orderDate != null) {
                        item.setOrderDate(orderDate.toLocalDate());
                    }

                    Date shippedDate = rs.getDate("shippedDate");
                    if (shippedDate != null) {
                        item.setShippedDate(shippedDate.toLocalDate());
                    }

                    item.setFreight(rs.getDouble("freight"));
                    item.setAddressID(rs.getString("addressID"));
                    item.setPaymentID(rs.getString("paymentID"));
                    item.setStatus(rs.getString("status"));
                    item.setShopID(rs.getString("shopID"));
                    item.setAmount(rs.getDouble("amount"));

                    item.setDetails(getOrderDetails(item.getOrderID()));

                    list.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<OrderHistoryDetailItem> getOrderDetails(String orderID) {
        List<OrderHistoryDetailItem> list = new ArrayList<>();

        String sql = "SELECT od.orderID, od.productID, od.unitPrice, od.quantity, od.discount, p.name " +
                "FROM Order_Detail od " +
                "LEFT JOIN Product p ON od.productID = p.productID " +
                "WHERE od.orderID = ?";

        try (
                Connection con = DBconnection.openConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, orderID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderHistoryDetailItem item = new OrderHistoryDetailItem();
                    item.setOrderID(rs.getString("orderID"));
                    item.setProductID(rs.getString("productID"));
                    item.setUnitPrice(rs.getDouble("unitPrice"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setDiscount(rs.getDouble("discount"));
                    item.setProductName(rs.getString("name"));
                    list.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
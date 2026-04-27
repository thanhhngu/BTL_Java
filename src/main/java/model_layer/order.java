package model_layer;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class order {
    private String orderID;
    private String customerID;
    private String shipperID;
    private LocalDate orderDate;
    private LocalDate shippedDate;
    private double freight;
    private double amount;
    private String addressID;
    private String paymentID;
    private String status;

    private List<order_detail> items;

    public List<order_detail> getItems() {
        return items;
    }

    public void setItems(List<order_detail> items) {
        this.items = items;
    }

    public order() {
    }

    public order(String orderID, String customerID, String shipperID, LocalDate orderDate,
                 LocalDate shippedDate, double freight, double amount,String addressID, String paymentID, String status) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.shipperID = shipperID;
        this.orderDate = orderDate;
        this.shippedDate = shippedDate;
        this.amount = amount;
        this.freight = freight;
        this.addressID = addressID;
        this.paymentID = paymentID;
        this.status = status;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getShipperID() {
        return shipperID;
    }

    public void setShipperID(String shipperID) {
        this.shipperID = shipperID;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(LocalDate shippedDate) {
        this.shippedDate = shippedDate;
    }

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAddressID() {
        return addressID;
    }

    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public void setStatus(String status) {this.status = status;}

    public String getStatus() {return status;}

}
package model_layer;

import java.time.LocalDate;

public class order {
    private int orderID;
    private int customerID;
    private int shipperID;
    private LocalDate orderDate;
    private LocalDate shippedDate;
    private double freight;
    private int addressID;
    private int paymentID;

    public order() {
    }

    public order(int orderID, int customerID, int shipperID, LocalDate orderDate,
                 LocalDate shippedDate, double freight, int addressID, int paymentID) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.shipperID = shipperID;
        this.orderDate = orderDate;
        this.shippedDate = shippedDate;
        this.freight = freight;
        this.addressID = addressID;
        this.paymentID = paymentID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getShipperID() {
        return shipperID;
    }

    public void setShipperID(int shipperID) {
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

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", customerID=" + customerID +
                ", shipperID=" + shipperID +
                ", orderDate=" + orderDate +
                ", shippedDate=" + shippedDate +
                ", freight=" + freight +
                ", addressID=" + addressID +
                ", paymentID=" + paymentID +
                '}';
    }
}
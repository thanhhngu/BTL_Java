package model_layer;

import java.time.LocalDate;

public class order {
    private String orderID;
    private String customerID;
    private String shipperID;
    private LocalDate orderDate;
    private LocalDate shippedDate;
    private double freight;
    private String addressID;
    private String paymentID;
    private String status;

    
	public order() {
    }

    public order(String orderID, String customerID, String shipperID, LocalDate orderDate,
                 LocalDate shippedDate, double freight, String addressID, String paymentID) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.shipperID = shipperID;
        this.orderDate = orderDate;
        this.shippedDate = shippedDate;
        this.freight = freight;
        this.addressID = addressID;
        this.paymentID = paymentID;
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
    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
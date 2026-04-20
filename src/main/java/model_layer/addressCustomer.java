package model_layer;

public class addressCustomer {
    private String addressID;
    private String customerID;
    private String address;

    public addressCustomer() {
    }

    public addressCustomer(String addressID, String customerID, String address) {
        this.addressID = addressID;
        this.customerID = customerID;
        this.address = address;
    }

    public String getAddressID() {
        return addressID;
    }

    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return address;
    }
}
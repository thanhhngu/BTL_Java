package model_layer;

public class addressCustomer {
    private int addressID;
    private int customerID;
    private String address;

    public addressCustomer() {
    }

    public addressCustomer(int addressID, int customerID, String address) {
        this.addressID = addressID;
        this.customerID = customerID;
        this.address = address;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
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
        return "AddressCustomer{" +
                "addressID=" + addressID +
                ", customerID=" + customerID +
                ", address='" + address + '\'' +
                '}';
    }
}
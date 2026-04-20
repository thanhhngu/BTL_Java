package model_layer;

public class shipper {
    private int shipperID;
    private String name;
    private String phone;
    private String companyName;

    public shipper() {
    }

    public shipper(int shipperID, String name, String phone, String companyName) {
        this.shipperID = shipperID;
        this.name = name;
        this.phone = phone;
        this.companyName = companyName;
    }

    public int getShipperID() {
        return shipperID;
    }

    public void setShipperID(int shipperID) {
        this.shipperID = shipperID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "Shipper{" +
                "shipperID=" + shipperID +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}
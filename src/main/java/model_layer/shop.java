package model_layer;

public class shop {
    private int shopID;
    private String name;
    private String phone;
    private String address;
    private String email;

    public shop() {
    }

    public shop(int shopID, String name, String phone, String address, String email) {
        this.shopID = shopID;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "shopID=" + shopID +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
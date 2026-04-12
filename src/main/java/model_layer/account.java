package model_layer;

public class account {
    private int accountID;
    private static String username;
    private String password;
    private String role;

    private String customerID;
    private String shopID;
    private String shipperID;

    public account() {
    }

    public account(int accountID, String username, String password, String role,
                   String customerID, String shopID, String shipperID) {
        this.accountID = accountID;
        this.username = username;
        this.password = password;
        this.role = role;
        this.customerID = customerID;
        this.shopID = shopID;
        this.shipperID = shipperID;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public static String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getShipperID() {
        return shipperID;
    }

    public void setShipperID(String shipperID) {
        this.shipperID = shipperID;
    }
}
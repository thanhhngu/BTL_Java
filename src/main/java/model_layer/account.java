package model_layer;

public class account {
    private String accountID;
    private static String username;
    private String password;
    private String role;

    private String customerID;
    private String shopID;
    private String shipperID;

    private String roleID;

    public account() {
    }

    public account(String accountID, String username, String password, String role,
                   String customerID, String shopID, String shipperID) {
        this.accountID = accountID;
        this.username = username;
        this.password = password;
        this.role = role;
        this.customerID = customerID;
        this.shopID = shopID;
        this.shipperID = shipperID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
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

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }
}
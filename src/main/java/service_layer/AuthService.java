package service_layer;

import model_layer.account;
import repository_layer.CustomerRepository;
import repository_layer.ShipperRepository;
import repository_layer.ShopRepository;
import repository_layer.accountRepository;

public class AuthService {
    private accountRepository accountRepository;
    private CustomerRepository customerRepository;
    private ShipperRepository shipperRepository;
    private ShopRepository shopRepository;

    public AuthService() {
        accountRepository = new accountRepository();
        customerRepository = new CustomerRepository();
        shipperRepository = new ShipperRepository();
        shopRepository = new ShopRepository();
    }

    public account login(String username, String password, String role) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        if (password == null || password.trim().isEmpty()) {
            return null;
        }
        if (role == null || role.trim().isEmpty()) {
            return null;
        }

        return accountRepository.login(
                username.trim(),
                password.trim(),
                role.trim().toUpperCase()
        );
    }

    // registerCustomer
    public String registerCustomer(String name, String phone, int gender, String birthdate,
                                   String username, String password) {

        if (accountRepository.existsUsername(username)) {
            return "Username đã tồn tại";
        }

// Tạo customerID mới
        String customerID = customerRepository.generateNextCustomerID();

// insert vào bảng Customer
        boolean ok1 = customerRepository.insertCustomer(customerID, name, phone, gender, birthdate);
        if (!ok1) return "Lưu Customer thất bại";

// insert vào bảng Account
        account acc = new account();
        acc.setUsername(username);
        acc.setPassword(password);
        acc.setRole("CUSTOMER");
        acc.setCustomerID(customerID);

        boolean ok2 = accountRepository.insertAccount(acc);
        if (!ok2) return "Lưu Account thất bại";

        return "SUCCESS";
    }

    public String registerShop(String name, String phone, String address, String email,
                               String username, String password) {

        if (accountRepository.existsUsername(username)) {
            return "Username đã tồn tại";
        }

        String shopID = shopRepository.generateNextShopID();

        boolean ok1 = shopRepository.insertShop(shopID, name, phone, address, email);
        if (!ok1) {
            return "Lưu Shop thất bại";
        }

        account acc = new account();
        acc.setUsername(username);
        acc.setPassword(password);
        acc.setRole("SHOP");
        acc.setShopID(shopID);

        boolean ok2 = accountRepository.insertAccount(acc);
        if (!ok2) {
            return "Lưu Account thất bại";
        }

        return "SUCCESS";
    }

    public String registerShipper(String name, String phone, String companyName,
                                  String username, String password) {

        if (accountRepository.existsUsername(username)) {
            return "Username đã tồn tại";
        }

// tạo shipperID mới
        String shipperID = shipperRepository.generateNextShipperID();

// insert vào bảng Shipper
        boolean ok1 = shipperRepository.insertShipper(shipperID, name, phone, companyName);
        if (!ok1) return "Lưu Shipper thất bại";

// insert vào Account
        account acc = new account();
        acc.setUsername(username);
        acc.setPassword(password);
        acc.setRole("SHIPPER");
        acc.setShipperID(shipperID);

        boolean ok2 = accountRepository.insertAccount(acc);
        if (!ok2) return "Lưu Account thất bại";

        return "SUCCESS";
    }
}
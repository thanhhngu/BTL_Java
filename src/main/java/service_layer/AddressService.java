package service_layer;

import model_layer.addressCustomer;
import repository_layer.AddressRepository;

import java.util.List;

public class AddressService {
    private final AddressRepository addressRepository = new AddressRepository();

    public List<addressCustomer> getAddressesByCustomer(String customerID) {
        return addressRepository.findByCustomerID(customerID);
    }
}

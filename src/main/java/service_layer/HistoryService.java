package service_layer;

import model_layer.OrderHistoryItem;
import repository_layer.HistoryRepository;

import java.util.List;

public class HistoryService {
    private final HistoryRepository repository = new HistoryRepository();

    public List<OrderHistoryItem> getDeliveredOrdersByCustomer(String customerID) {
        return repository.getOrdersByCustomer(customerID);
    }
}
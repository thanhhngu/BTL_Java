package service_layer;

import model_layer.CartItem;
import repository_layer.OrderRepository;

import java.util.List;

public class OrderService {

    private final OrderRepository orderRepository = new OrderRepository();

    public boolean placeOrder(String customerID,
                              String shopID,
                              String addressID,
                              String payID,
                              double amount,
                              List<CartItem> items) {

        double freight = 30000;

        return orderRepository.insertOrder(
                customerID,
                shopID,
                addressID,
                payID,
                amount,
                freight,
                items
        );
    }
}
package service_layer;

import repository_layer.OrderReponsitory;

import java.util.Map;

public class ShipperService {
    private final OrderReponsitory orderRepository;

    public ShipperService() {
        this.orderRepository = new OrderReponsitory();
    }

    public Map<String, Double> getMonthlyFreightStats(String shipperID) {
        return orderRepository.getMonthlyFreightStats(shipperID);
    }
}
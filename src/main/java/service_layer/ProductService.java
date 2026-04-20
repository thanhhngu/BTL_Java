package service_layer;

import model_layer.products;
import repository_layer.ProductRepository;

import java.util.List;

public class ProductService {

    private final ProductRepository productRepository = new ProductRepository();

    public List<products> getAllAvailableProducts() {
        return productRepository.getAllAvailableProducts();
    }

    public List<products> searchProductsByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllAvailableProducts();
        }
        return productRepository.searchProductsByName(keyword.trim());
    }

    public List<products> getProductsByCategory(String catgID) {
        if (catgID == null || catgID.trim().isEmpty() || "ALL".equalsIgnoreCase(catgID)) {
            return getAllAvailableProducts();
        }
        return productRepository.getProductsByCategory(catgID);
    }
}

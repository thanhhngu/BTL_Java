package service_layer;

import model_layer.products;
import repository_layer.ProductRepository;

import java.util.List;

public class ProductService {
    private ProductRepository productRepository;

    public ProductService() {
        productRepository = new ProductRepository();
    }

    public List<products> getAllProducts() {
        return productRepository.findAll();
    }

    public List<products> searchProductsByName(String keyword) {
        return productRepository.findByName(keyword);
    }
}
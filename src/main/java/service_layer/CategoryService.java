package service_layer;

import model_layer.category;
import repository_layer.CategoryRepository;

import java.util.List;

public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService() {
        categoryRepository = new CategoryRepository();
    }

    public List<category> getAllCategories() {
        return categoryRepository.getAllCategories();
    }
}
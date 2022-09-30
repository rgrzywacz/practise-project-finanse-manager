package category;

import java.util.List;

import eception.ApplicationException;
import validation.ValidationMessage;

public class CategoryService {
    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<String> getAllCategoryNames() {
        return categoryRepository.findAllCategoryNames();
    }

    public void addCategory(String categoryName) {
        Category byName = categoryRepository.findByName(categoryName);
        if (byName == null) {
            Category category = new Category(categoryName);
            categoryRepository.insert(category);
        }
    }

    public void deleteCategory(String categoryName) throws ApplicationException {
        Category category = categoryRepository.findByName(categoryName);
        if (category != null && (category.getExpenses() == null || category.getExpenses().isEmpty())) {
            categoryRepository.delete(category);
        } else {
            ValidationMessage validationMessage = new ValidationMessage(ValidationMessage.CATEGORY_ASSOCIATED_WITH_ENTRIES, true);
            throw new ApplicationException(validationMessage.getMessage());
        }
    }

}

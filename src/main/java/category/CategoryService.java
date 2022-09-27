package category;

import java.util.List;

import eception.ApplicationException;
import validation.ValidationMessage;

public class CategoryService {
    private CategoryDao categoryDao;

    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public List<String> getAllCategoryNames() {
        return categoryDao.findAllCategoryNames();
    }

    public void addCategory(String categoryName) {
        Category byName = categoryDao.findByName(categoryName);
        if (byName == null) {
            Category category = new Category(categoryName);
            categoryDao.insert(category);
        }
    }

    public void deleteCategory(String categoryName) throws ApplicationException {
        Category category = categoryDao.findByName(categoryName);
        if (category != null && (category.getExpenses() == null || category.getExpenses().isEmpty())) {
            categoryDao.delete(category);
        } else {
            ValidationMessage validationMessage = new ValidationMessage(ValidationMessage.CATEGORY_ASSOCIATED_WITH_ENTRIES, true);
            throw new ApplicationException(validationMessage.getMessage());
        }
    }

}

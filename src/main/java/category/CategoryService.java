package category;

import java.util.List;

import eception.ApplicationException;

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
            throw new ApplicationException("Podana kategoria jest powiązana z wydatkami, usuń wydatki powiązane z kategorią aby usunąć kategorię");
        }
    }

}

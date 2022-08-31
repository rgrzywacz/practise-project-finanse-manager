package category;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import config.ConnectionManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class CategoryDao {
    Logger logger = Logger.getLogger(CategoryDao.class.getName());

    public Category findByName(String name) {
        EntityManager entityManager = ConnectionManager.getEntityManager();
        Category category = null;
        try {
            category = (Category) entityManager.createQuery("select c from Category c where c.name=?1").setParameter(1, name).getSingleResult();
        } catch (NoResultException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        entityManager.close();
        return category;
    }

    public List<String> findAllCategoryNames() {
        EntityManager entityManager = ConnectionManager.getEntityManager();
        List resultList = entityManager.createQuery("select c.name from Category c").getResultList();
        entityManager.close();
        return resultList;
    }

    public void insert(Category category) {
        EntityManager entityManager = ConnectionManager.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(category);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void delete(Category category) {
        EntityManager entityManager = ConnectionManager.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.remove(category);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}

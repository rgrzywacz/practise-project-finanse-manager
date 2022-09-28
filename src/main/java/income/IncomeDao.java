package income;

import java.util.List;

import config.ConnectionManager;
import expanse.Expense;
import jakarta.persistence.EntityManager;

public class IncomeDao {

    public void insert(Income income) {
        EntityManager entityManager = ConnectionManager.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(income);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public Income find(Long id) {
        EntityManager entityManager = ConnectionManager.getEntityManager();
        Income income = entityManager.find(Income.class, id);
        entityManager.close();
        return income;
    }

    public List<Income> findAllByAccountNumber(Long accountId) {
        EntityManager entityManager = ConnectionManager.getEntityManager();
        List incomes = entityManager.createQuery("select i from Income i where i.account.id = ?1").setParameter(1, accountId).getResultList();
        entityManager.close();
        return incomes;
    }

    public List<Income> findAll() {
        EntityManager entityManager = ConnectionManager.getEntityManager();
        List<Income> incomes = entityManager.createQuery("select i from Income i").getResultList();
        entityManager.close();
        return incomes;
    }
    public void delete(Income income) {
        EntityManager entityManager = ConnectionManager.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.remove(income);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}

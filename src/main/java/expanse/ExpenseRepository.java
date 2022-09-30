package expanse;

import java.time.LocalDate;
import java.util.List;

import config.ConnectionManager;
import jakarta.persistence.EntityManager;

public class ExpenseRepository {
    public void insert(Expense expense) {
        EntityManager entityManager = ConnectionManager.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(expense);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public List<Expense> findAllByAccountNumber(Long accountId) {
        EntityManager entityManager = ConnectionManager.getEntityManager();
        List expenses = entityManager.createQuery("select e from Expense e where e.account.id = ?1").setParameter(1, accountId).getResultList();
        entityManager.close();
        return expenses;
    }

    public List<Expense> findAll() {
        EntityManager entityManager = ConnectionManager.getEntityManager();
        List<Expense> expenses = entityManager.createQuery("select e from Expense e").getResultList();
        entityManager.close();
        return expenses;
    }

    public List<Expense> findBetweenDates(LocalDate start, LocalDate end) {
        EntityManager entityManager = ConnectionManager.getEntityManager();
        List<Expense> expenses = entityManager.createQuery("select e from Expense e where e.expanseAddDate>=?1 and e.expanseAddDate<=?2")
                                              .setParameter(1, start)
                                              .setParameter(2, end)
                                              .getResultList();
        entityManager.close();
        return expenses;
    }

    public void deleteById(Long id) {
        EntityManager entityManager = ConnectionManager.getEntityManager();
        entityManager.getTransaction().begin();
        Expense expense = entityManager.find(Expense.class, id);
        if (expense != null) {
            entityManager.remove(expense);
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public List<Object[]> findExpenseSummaryGroupByCategory() {
        EntityManager entityManager = ConnectionManager.getEntityManager();
        String sql = "select sum(e.amount), c.name, count(e.id) from Expense e " + "join Category c on e.category_id = c.id " + "group by c.name " + "order by sum(e.amount) desc";

        List<Object[]> resultList = entityManager.createNativeQuery(sql).getResultList();
        entityManager.close();
        return resultList;
    }

    public List<Object[]> findExpenseSummaryGroupByCategoryAndAccount(long accountId) {
        EntityManager entityManager = ConnectionManager.getEntityManager();
        String sql = "select sum(e.amount), c.name, count(e.id) from Expense e " +
                     "join Category c on e.category_id = c.id " +
                     "join Account a on e.account_id = a.id " +
                     "where a.id = ?1 " +
                     "group by c.name " +
                     "order by sum(e.amount) desc";

        List<Object[]> resultList = entityManager
                .createNativeQuery(sql)
                .setParameter(1, accountId)
                .getResultList();
        entityManager.close();
        return resultList;
    }

    public List<Expense> findBetweenDatesForAccount(LocalDate start, LocalDate end, long accountId) {
        EntityManager entityManager = ConnectionManager.getEntityManager();
        List<Expense> expenses = entityManager.createQuery("select e from Expense e where e.expanseAddDate>=?1 and e.expanseAddDate<=?2 and e.account.id=?3 ")
                                              .setParameter(1, start)
                                              .setParameter(2, end)
                                              .setParameter(3, accountId)
                                              .getResultList();
        entityManager.close();
        return expenses;
    }
}

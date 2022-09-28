package account;

import java.util.List;

import config.ConnectionManager;
import expanse.Expense;
import jakarta.persistence.EntityManager;

public class AccountDao {

    public Account findAccount(long id) {
        EntityManager entityManager = ConnectionManager.getEntityManager();
        Account account = (Account) entityManager.createQuery("select a from Account a join fetch a.expenses").getSingleResult();
        entityManager.close();
        return account;
    }

    public void insertAccount(Account account) {
        EntityManager entityManager = ConnectionManager.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(account);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public List<Account> findAll() {
        EntityManager entityManager = ConnectionManager.getEntityManager();
        List<Account> accounts = entityManager.createQuery("select a from Account a").getResultList();
        entityManager.close();
        return accounts;
    }

    public Account findByAccountNumber(String accountNumber) {
        EntityManager entityManager = ConnectionManager.getEntityManager();
        Account account = (Account) entityManager.createQuery("select a from Account a where a.accountNumber=?1").setParameter(1, accountNumber).getSingleResult();
        return account;
    }

    public void deleteAccount(long accountId) {
        EntityManager entityManager = ConnectionManager.getEntityManager();
        Account account = entityManager.find(Account.class, accountId);
        entityManager.getTransaction().begin();
        entityManager.remove(account);
        entityManager.getTransaction().commit();
    }
}

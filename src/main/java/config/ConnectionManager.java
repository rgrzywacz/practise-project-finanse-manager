package config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ConnectionManager {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("moneyTracker");

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}

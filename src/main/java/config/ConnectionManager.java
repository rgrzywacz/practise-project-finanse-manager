package config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class ConnectionManager {

    private static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    public static EntityManager getEntityManager() {
        return sessionFactory.createEntityManager();
    }
}

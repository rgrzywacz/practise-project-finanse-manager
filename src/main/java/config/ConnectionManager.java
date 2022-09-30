package config;

import java.util.Properties;

import category.Category;
import expanse.Expense;
import income.Income;
import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

public class ConnectionManager {

    private static SessionFactory sessionFactory;

    public static EntityManager getEntityManager() {
        return getSessionFactory().createEntityManager();
    }

    private static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
                settings.put(Environment.URL, "jdbc:mysql://localhost:3306/finance_manager");
                settings.put(Environment.USER, "root");
                settings.put(Environment.PASS, "rootroot");
                settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");

                settings.put(Environment.SHOW_SQL, "true");

                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

                settings.put(Environment.HBM2DDL_AUTO, "validate");

                configuration.setProperties(settings);

                configuration.addAnnotatedClass(Category.class);
                configuration.addAnnotatedClass(Expense.class);
                configuration.addAnnotatedClass(Income.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return sessionFactory;
    }
}

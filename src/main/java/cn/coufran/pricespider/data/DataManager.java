package cn.coufran.pricespider.data;

import cn.coufran.pricespider.bean.OriginDatum;
import cn.coufran.pricespider.bean.Price;
import cn.coufran.pricespider.bean.Batch;
import org.hibernate.Session;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @author Coufran
 * @version 1.0.0
 * @since 1.0.0
 */
public class DataManager {
    private static EntityManagerFactory entityManagerFactory;

    static {
        Configuration configuration = new Configuration();

        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/test");
        configuration.setProperty("hibernate.connection.username", "liuhm8");
        configuration.setProperty("hibernate.connection.password", "YLHH-5423");
        configuration.setProperty("hibernate.connection.pool_size", "20");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        configuration.setProperty("hibernate.current_session_context_class", "thread");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "validate");
        configuration.setPhysicalNamingStrategy(new PhysicalNamingStrategyStandardImpl() {
            private Identifier toXxxName(Identifier name, JdbcEnvironment context) {
                if(name == null || "DTYPE".equals(name.getText())) {
                    return name;
                }
                String text = name.getText();
                for(char c='A'; c<='Z'; c++) {
                    text = text.replaceAll(""+c, "_" + (char)(c-('A'-'a')));
                }
                if(text.startsWith("_")) {
                    text = text.substring(1);
                }
                text = text.replaceAll("__", "_");
                return Identifier.toIdentifier(text);
            }

            @Override
            public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
                return this.toXxxName(name, context);
            }

            @Override
            public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
                return this.toXxxName(name, context);
            }
        });

        configuration.addAnnotatedClass(Price.class);
        configuration.addAnnotatedClass(OriginDatum.class);
        configuration.addAnnotatedClass(Batch.class);

        entityManagerFactory = configuration.buildSessionFactory();
    }

    public static EntityManager createEntityManager() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        return entityManager;
    }

    public static void closeEntityManager(EntityManager entityManager) {
        entityManager.getTransaction().commit();
        entityManager.close();
    }

}

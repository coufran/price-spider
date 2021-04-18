package cn.coufran.pricespider.data;

import cn.coufran.pricespider.bean.OriginDatum;
import cn.coufran.pricespider.bean.Price;
import cn.coufran.pricespider.bean.Batch;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author Coufran
 * @version 1.0.0
 * @since 1.0.0
 */
public class DataManager {
    private static SessionFactory sessionFactory;

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
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");

        configuration.addAnnotatedClass(Price.class);
        configuration.addAnnotatedClass(OriginDatum.class);
        configuration.addAnnotatedClass(Batch.class);

        sessionFactory = configuration.buildSessionFactory();
    }

    public static Session openSession() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        return session;
    }

    public static void closeSession(Session session) {
        session.getTransaction().commit();
        session.close();
    }

}

package cn.coufran.pricespider;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.sql.DriverManager;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liuhm8
 * @version 1.0.0
 * @since 1.0.0
 */
public class PriceSpider {
    private static SessionFactory sessionFactory;

    static {
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/test");
        configuration.setProperty("hibernate.connection.username", "liuhm8");
        configuration.setProperty("hibernate.connection.password", "YLHH-5423");
        configuration.setProperty("hibernate.connection.pool_size", "20");
        configuration.setProperty("hibernate.current_session_context_class", "thread");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.addAnnotatedClass(Price.class);

        sessionFactory = configuration.buildSessionFactory();
    }

    public static void main(String[] args) throws IOException {
        new PriceSpider().start();
    }

    public void start(int start, int end) {
        List<Price> prices = new ArrayList<>();
        for (int i=start; i<=end; i++) {
            Connection connection = Jsoup.connect("http://www.xinfadi.com.cn/marketanalysis/0/list/" + i + ".shtml");
            connection.timeout(1000 * 60);
            Document document = null;
            try {
                document = connection.get();
            } catch (IOException e) {
//                e.printStackTrace();
                System.err.println(e.getMessage());
                i--;
                continue;
            }
            Element table = document.select("table.hq_table").get(0);
            Elements lines = table.select("tbody>tr:gt(0)");
            for(Element line : lines) {
                Price price = new Price();
                Elements columns = line.select("td");
                // 品名
                Element name = columns.get(0);
                price.setName(name.text());
                prices.add(price);
                // 最高价
                Element max = columns.get(1);
                price.setMax(price2Int(max.text()));
                // 平均价
                Element average = columns.get(2);
                price.setAverage(price2Int(average.text()));
                // 最低价
                Element min = columns.get(3);
                price.setMin(price2Int(min.text()));
                // 规格
                Element specification = columns.get(4);
                price.setSpecification(specification.text());
                // 单位
                Element unit = columns.get(5);
                price.setUnit(unit.text());
                // 发布日期
                Element issuedDate = columns.get(6);
                price.setIssuedDate(text2Date(issuedDate.text()));
            }
            System.out.println(i);
        }
        System.out.println(prices);

        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        for(Price price : prices) {
            session.save(price);
        }
        transaction.commit();
        session.close();
    }

    public void start() {
        for(int i=1; i<=15385; i+= 1000) {
            int finalI = i;
            new Thread(() -> {
                new PriceSpider().start(finalI, Math.min(finalI +999, 15385));
            }) {{
                this.setName("spider-" + finalI);
            }}.start();
        }

    }

    private Date text2Date(String text) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Integer price2Int(String price) {
        try {
            return (int) (new DecimalFormat("0.00").parse(price).doubleValue()*100);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}

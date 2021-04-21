package cn.coufran.pricespider;

import cn.coufran.pricespider.bean.Batch;
import cn.coufran.pricespider.bean.OriginDatum;
import cn.coufran.pricespider.bean.Price;
import cn.coufran.pricespider.data.DataManager;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 1.爬取数据
 * 2.解析数据
 * 3.分析汇总入库
 * @author Coufran
 * @version 1.0.0
 * @since 1.0.0
 */
public class Spider {
    private static final Logger LOGGER = LoggerFactory.getLogger(Spider.class);

    public static void main(String[] args) throws IOException {
        new Spider().start();
    }

    private Loader loader = new XinfadiRetryLoader();
    private Parser parser = new XinfadiParser();

    public void start() {
        int step = 100;
        int lastPage = 15441;
        for(int i=13001; i<=lastPage; i+=step) {
            this.start(i, Math.min(i+step-1, lastPage));
        }
    }

    public void start(int start, int end) {
        Batch batch = new Batch();
        batch.generateNo();
        batch.setStartTime(new Date());

        for(int i=start; i<=end; i++) {
            String url = String.format("http://www.xinfadi.com.cn/marketanalysis/0/list/%s.shtml", i);
            OriginDatum originDatum = loader.load(url);
            originDatum.setBatch(batch);
            batch.addOriginDatum(originDatum);
            if(!originDatum.getSuccess()) {
                LOGGER.warn("{} error: {}", i, originDatum.getErrorMessage());
                continue;
            }
            List<Price> prices = parser.parse(originDatum);
            for(Price price : prices) {
                price.setOriginDatum(originDatum);
                originDatum.addPrice(price);
            }
            LOGGER.info("{} success", i);
        }
        batch.setEndTime(new Date());

        EntityManager entityManager = DataManager.createEntityManager();
        entityManager.persist(batch);
        DataManager.closeEntityManager(entityManager);
    }

}

package cn.coufran.pricespider;

import cn.coufran.pricespider.bean.Batch;
import cn.coufran.pricespider.bean.OriginDatum;
import cn.coufran.pricespider.bean.Price;
import cn.coufran.pricespider.data.DataManager;
import org.hibernate.Session;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public static void main(String[] args) throws IOException {
        new Spider().start();
    }

    private Loader loader = new XinfadiLoader();
    private Parser parser = new XinfadiParser();

    public void start() {
        Session session = DataManager.openSession();
        Batch batch = new Batch();
        batch.generateNo();
        batch.setStartDate(new Date());

        for(int i=1; i<=10; i++) {
            String url = String.format("http://www.xinfadi.com.cn/marketanalysis/0/list/%s.shtml", i);
            OriginDatum originDatum = loader.load(url);
            originDatum.setBatchNo(batch.getNo());
            List<Price> prices = parser.parse(originDatum);
            for(Price price : prices) {
                session.save(originDatum);
                session.save(price);
            }
        }
        batch.setEndDate(new Date());
        session.save(batch);
        DataManager.closeSession(session);
    }

}

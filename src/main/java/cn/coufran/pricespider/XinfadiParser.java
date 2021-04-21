package cn.coufran.pricespider;

import cn.coufran.pricespider.bean.OriginDatum;
import cn.coufran.pricespider.bean.Price;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Coufran
 * @version 1.0.0
 * @since 1.0.0
 */
public class XinfadiParser implements Parser {
    @Override
    public List<Price> parse(OriginDatum originDatum) {
        Document document = originDatum.getHtmlContent();
        List<Price> prices = new ArrayList<>();
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
            price.setMaxPrice(price2Int(max.text()));
            // 平均价
            Element average = columns.get(2);
            price.setAveragePrice(price2Int(average.text()));
            // 最低价
            Element min = columns.get(3);
            price.setMinPrice(price2Int(min.text()));
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
        return prices;
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

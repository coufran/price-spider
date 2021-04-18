package cn.coufran.pricespider;

import cn.coufran.pricespider.bean.OriginDatum;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Date;

/**
 * @author Coufran
 * @version 1.0.0
 * @since 1.0.0
 */
public class XinfadiLoader implements Loader {
    @Override
    public OriginDatum load(String url) {
        OriginDatum originDatum = new OriginDatum();
        originDatum.setUrl(url);
        originDatum.setStartTime(new Date());

        Connection connect = Jsoup.connect(url);
        connect.timeout(1000 * 60); // 60s
        Document document = null;
        try {
            document = connect.get();
        } catch (IOException e) {
            originDatum.setSuccess(false);
            String message = e.getMessage();
            originDatum.setErrorMsg(message);
        }
        if(document != null) {
            originDatum.setSuccess(true);
            originDatum.setHtmlContent(document);
        }

        originDatum.setEndTime(new Date());
        return originDatum;
    }
}

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
public class XinfadiRetryLoader extends XinfadiLoader {
    @Override
    public OriginDatum load(String url) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        OriginDatum originDatum = super.load(url);
        if(originDatum.getSuccess()) {
            return originDatum;
        }
        // 重试3次
        for(int i=0; i<3; i++) {
            try {
                Thread.sleep(1000 * 10 * (i * 6 + 1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            originDatum = super.load(url);
            if(originDatum.getSuccess()) {
                break;
            }
        }
        return originDatum;
    }
}

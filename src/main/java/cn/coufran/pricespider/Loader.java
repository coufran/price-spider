package cn.coufran.pricespider;

import cn.coufran.pricespider.bean.OriginDatum;

/**
 * @author liuhm8
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Loader {
    OriginDatum load(String url);
}

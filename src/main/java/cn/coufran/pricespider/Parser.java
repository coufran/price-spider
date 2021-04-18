package cn.coufran.pricespider;

import cn.coufran.pricespider.bean.OriginDatum;
import cn.coufran.pricespider.bean.Price;

import java.util.List;

/**
 * @author Coufran
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Parser {
    List<Price> parse(OriginDatum originDatum);
}

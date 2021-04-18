package cn.coufran.pricespider;

import cn.coufran.pricespider.bean.Price;

import java.util.List;

/**
 * @author liuhm8
 * @version 1.0.0
 * @since 1.0.0
 */
public interface DataStore {
    void save(List<Price> prices);
}

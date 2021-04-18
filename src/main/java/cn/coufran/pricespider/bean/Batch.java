package cn.coufran.pricespider.bean;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 爬取批次
 * @author Coufran
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Entity
public class Batch {
    /** 批次号 */
    @Id
    private String no;
    /** 开始时间 */
    private Date startDate;
    /** 结束时间 */
    private Date endDate;
    /** 原始数据 */
//    private List<OriginDatum> originData;

    public void generateNo() {
        String no = new SimpleDateFormat("yyyyMMdd").format(new Date());
        no = no + "01";
        this.no = no;
    }
}

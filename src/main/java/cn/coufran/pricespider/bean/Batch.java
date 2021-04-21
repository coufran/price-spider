package cn.coufran.pricespider.bean;

import cn.coufran.pricespider.data.DataManager;
import lombok.Data;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    @Column(length = 10, columnDefinition = "char")
    private String no;
    /** 开始时间 */
    private Date startTime;
    /** 结束时间 */
    private Date endTime;
    /** 原始数据 */
    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    private List<OriginDatum> originData;

    private static int lastIndex = 0;
    private static String lastDatePrefix = null;

    public void generateNo() {
        String datePrefix = new SimpleDateFormat("yyMMdd").format(new Date());
        if(!datePrefix.equals(lastDatePrefix)) {
            synchronized (this.getClass()) {
                if(!datePrefix.equals(lastDatePrefix)) {
                    lastDatePrefix = datePrefix;
                    lastIndex = 0;
                }
            }
        }
        EntityManager entityManager = DataManager.createEntityManager();
        synchronized (this.getClass()) {
            while (true) {
                String indexSuffix = new DecimalFormat("0000").format(++lastIndex);
                String no = datePrefix + indexSuffix;
                Batch batch = entityManager.find(Batch.class, no);
                if (batch == null) {
                    this.no = no;
                    break;
                }
            }
        }
        DataManager.closeEntityManager(entityManager);
    }

    public void addOriginDatum(OriginDatum originDatum) {
        if(originData == null) {
            synchronized(this) {
                if(originData == null) {
                    originData = new ArrayList<>();
                }
            }
        }
        originData.add(originDatum);
    }
}

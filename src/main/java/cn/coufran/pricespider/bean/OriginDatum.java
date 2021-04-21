package cn.coufran.pricespider.bean;

import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Coufran
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Entity
public class OriginDatum {
    /** ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 爬取URL */
    private String url;
    /** 是否爬取成功 */
    private Boolean success;
    /** 失败原因 */
    private String errorMessage;
    /** 开始时间 */
    private Date startTime;
    /** 结束时间 */
    private Date endTime;
    /** 爬取的内容 */
    @Transient
    private Document htmlContent;
    /** 批次 */
    @ManyToOne
    private Batch batch;
    @OneToMany(mappedBy = "originDatum", cascade = CascadeType.ALL)
    private List<Price> prices;

    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "text")
    @Access(AccessType.PROPERTY)
    public String getContent() {
        if(htmlContent == null) {
            return null;
        }
        return htmlContent.toString();
    }

    public void setContent(String content) {
        if(content == null) {
            this.htmlContent = null;
        }
        this.htmlContent = Jsoup.parse(content);
    }

    public void addPrice(Price price) {
        if(this.prices == null) {
            synchronized (this) {
                if(this.prices == null) {
                    this.prices = new ArrayList<>();
                }
            }
        }
        this.prices.add(price);
    }
}

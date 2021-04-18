package cn.coufran.pricespider.bean;

import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Coufran
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Entity
public class OriginDatum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String url;
    private Boolean success;
    private String errorMsg;
    private Date startTime;
    private Date endTime;
    @Transient
    private Document htmlContent;
    private String batchNo;

    @Basic
    public String getContent() {
        return htmlContent.toString();
    }

    public void setContent(String content) {
        this.htmlContent = Jsoup.parse(content);
    }
}

package cn.coufran.pricespider.bean;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Coufran
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Entity
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer maxPrice;
    private Integer averagePrice;
    private Integer minPrice;
    private String specification;
    private String unit;
    private Date issuedDate;
    @ManyToOne
    private OriginDatum originDatum;
}

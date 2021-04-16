package cn.coufran.pricespider;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author liuhm8
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
    private Integer max;
    private Integer average;
    private Integer min;
    private String specification;
    private String unit;
    private Date issuedDate;
}

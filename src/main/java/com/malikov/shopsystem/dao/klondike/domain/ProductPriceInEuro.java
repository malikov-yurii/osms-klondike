package com.malikov.shopsystem.dao.klondike.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Table(name = "osms_product_price_in_euro")
@Getter
@Setter
public class ProductPriceInEuro {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", columnDefinition = "bigint(11)", unique = true, nullable = false)
    private BigInteger productId;

    @Column(name = "price")
    private BigDecimal price;

}

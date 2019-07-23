package com.malikov.shopsystem.dao.klondike.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Table(name = "catalog_product_entity_decimal")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entity_id", columnDefinition = "bigint(11)", unique = true, nullable = false)
    private BigInteger id;

    @Column(name = "value")
    private BigDecimal price;

}

package com.malikov.shopsystem.dao.klondike.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Table(name = "catalog_product_entity_decimal")
@Getter
@Setter
@IdClass(ProductAttributeId.class)
public class ProductAttribute {

    @Id
    @Column(name = "entity_id", columnDefinition = "bigint(11)", nullable = false)
    private BigInteger productId;

    @Id
    @Column(name = "attribute_id")
    private Integer attributeId;

    @Column(name = "value")
    private BigDecimal price;

}

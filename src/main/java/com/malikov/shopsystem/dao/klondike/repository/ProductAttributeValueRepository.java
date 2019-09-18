package com.malikov.shopsystem.dao.klondike.repository;

import com.malikov.shopsystem.dao.klondike.domain.ProductAttribute;
import com.malikov.shopsystem.dao.klondike.domain.ProductAttributeId;
import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface ProductAttributeValueRepository extends CrudRepository<ProductAttribute, ProductAttributeId> {

    Optional<ProductAttribute> findByProductIdAndAttributeId(BigInteger productId, Integer attributeId);

    List<ProductAttribute> findAllByAttributeId(Integer attributeId);

}

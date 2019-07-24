package com.malikov.shopsystem.dao.klondike.repository;

import com.malikov.shopsystem.dao.klondike.domain.ProductPriceInEuro;
import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;

public interface ProductPriceInEuroRepository extends CrudRepository<ProductPriceInEuro, BigInteger> {

}

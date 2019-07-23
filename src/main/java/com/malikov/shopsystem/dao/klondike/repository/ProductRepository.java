package com.malikov.shopsystem.dao.klondike.repository;

import com.malikov.shopsystem.dao.klondike.domain.Product;
import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;

public interface ProductRepository extends CrudRepository<Product, BigInteger> {

}

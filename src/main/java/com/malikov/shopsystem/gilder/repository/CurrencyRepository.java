package com.malikov.shopsystem.gilder.repository;

import com.malikov.shopsystem.gilder.domain.Currency;
import com.malikov.shopsystem.enumtype.CurrencyCode;
import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;

public interface CurrencyRepository extends CrudRepository<Currency, BigInteger> {

    Currency findByCurrencyCode(CurrencyCode currencyCode);

}

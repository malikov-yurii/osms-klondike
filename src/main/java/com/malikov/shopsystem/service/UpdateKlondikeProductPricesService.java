package com.malikov.shopsystem.service;

import com.malikov.shopsystem.dao.gilder.domain.Currency;
import com.malikov.shopsystem.dao.gilder.repository.CurrencyRepository;
import com.malikov.shopsystem.dao.klondike.domain.Product;
import com.malikov.shopsystem.dao.klondike.domain.ProductPriceInEuro;
import com.malikov.shopsystem.dao.klondike.repository.ProductPriceInEuroRepository;
import com.malikov.shopsystem.dao.klondike.repository.ProductRepository;
import com.malikov.shopsystem.enumtype.CurrencyCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Consumer;

import static java.math.BigDecimal.ROUND_UP;

@Service
@Slf4j
public class UpdateKlondikeProductPricesService {

    private static final int TWENTY_MINUTES = 20 * 60 * 1000;

    private final CurrencyRepository currencyRateRepository;
    private final ProductRepository productRepository;
    private final ProductPriceInEuroRepository productPriceInEuroRepository;

    public UpdateKlondikeProductPricesService(CurrencyRepository currencyRateRepository,
                                              ProductRepository productRepository,
                                              ProductPriceInEuroRepository productPriceInEuroRepository) {
        this.currencyRateRepository = currencyRateRepository;
        this.productRepository = productRepository;
        this.productPriceInEuroRepository = productPriceInEuroRepository;
    }

    @Scheduled(fixedDelay = TWENTY_MINUTES)
    protected void productTest() {
        Optional.ofNullable(currencyRateRepository.findByCurrencyCode(CurrencyCode.EUR))
                .map(Currency::getCurrencyRate)
                .ifPresent(updateWithNewCurrencyRateKlondikeProductsWithPriceInEuro());
    }

    private Consumer<BigDecimal> updateWithNewCurrencyRateKlondikeProductsWithPriceInEuro() {
        return currencyRate -> {
            log.info("New currency rate for products with prices in euro = " + currencyRate);
            productPriceInEuroRepository.findAll()
                    .forEach(updateProductPrice(currencyRate));
        };
    }

    private Consumer<ProductPriceInEuro> updateProductPrice(BigDecimal currencyRate) {
        return productPriceInEuro ->
                productRepository.findById(productPriceInEuro.getProductId())
                        .ifPresent(product -> {
                            BigDecimal updatedPrice = productPriceInEuro.getPrice().divide(currencyRate, 0, ROUND_UP);
                            product.setPrice(updatedPrice);
                            Product saved = productRepository.save(product);
                            log.info(saved.getId() + " new product price is " + saved.getPrice());
                        });

    }

}

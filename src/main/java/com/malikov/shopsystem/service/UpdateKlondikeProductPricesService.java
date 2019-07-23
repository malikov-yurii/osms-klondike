package com.malikov.shopsystem.service;

import com.malikov.shopsystem.dao.klondike.repository.ProductRepository;
import com.malikov.shopsystem.enumtype.CurrencyCode;
import com.malikov.shopsystem.dao.gilder.domain.Currency;
import com.malikov.shopsystem.dao.gilder.repository.CurrencyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Optional;

@Service
@Slf4j
public class UpdateKlondikeProductPricesService {

    private static final int TWENTY_MINUTES = 20 * 60 * 1000;
//    private static final int THREE_HOURS = 3 * 60 * 60 * 1000;

    private final CurrencyRepository currencyRateRepository;
    private final ProductRepository productRepository;

    public UpdateKlondikeProductPricesService(CurrencyRepository currencyRateRepository,
                                              ProductRepository productRepository) {
        this.currencyRateRepository = currencyRateRepository;
        this.productRepository = productRepository;
    }

//    @Scheduled(fixedDelay = TWENTY_MINUTES, initialDelay = THREE_HOURS)
    @Scheduled(fixedDelay = TWENTY_MINUTES)
    @Transactional("gilderTransactionManager")
    protected void update() {
        log.info("Scheduled update for klondike product prices.");
        Optional.ofNullable(currencyRateRepository.findByCurrencyCode(CurrencyCode.EUR))
                .map(Currency::getCurrencyRate)
                .ifPresent(currencyRate -> {
                    log.info("currencyRate=" + currencyRate);


                });
    }

    @Scheduled(fixedDelay = TWENTY_MINUTES)
    @Transactional("productTransactionManager")
    protected void productTest() {
        log.info("ProductTest");
        productRepository.findById(BigInteger.valueOf(559))
                .ifPresent(product -> {
                    log.info("559 product price = " + product.getPrice());
                });

    }

}

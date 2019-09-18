package com.malikov.shopsystem.service;

import com.malikov.shopsystem.dao.gilder.domain.Currency;
import com.malikov.shopsystem.dao.gilder.repository.CurrencyRepository;
import com.malikov.shopsystem.dao.klondike.repository.ProductAttributeValueRepository;
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

    private static final int MINUTE = 60 * 1000;
    private static final int TWENTY_MINUTES = 20 * MINUTE;
    private static final Integer PRICE_IN_EURO_ID = 233;
    private static final Integer PRICE_IN_HRN = 77;

    private final CurrencyRepository currencyRateRepository;
    private final ProductAttributeValueRepository productAttributeRepository;

    public UpdateKlondikeProductPricesService(CurrencyRepository currencyRateRepository,
                                              ProductAttributeValueRepository productAttributeRepository) {
        this.currencyRateRepository = currencyRateRepository;
        this.productAttributeRepository = productAttributeRepository;
    }

    @Scheduled(fixedDelay = TWENTY_MINUTES, initialDelay = MINUTE)
    protected void scheduledUpdateProductPriceByCurrency() {
        Optional.ofNullable(currencyRateRepository.findByCurrencyCode(CurrencyCode.EUR))
                .map(Currency::getCurrencyRate)
                .ifPresent(updateWithNewCurrencyRateKlondikeProductsWithPriceInEuro());
    }

    private Consumer<BigDecimal> updateWithNewCurrencyRateKlondikeProductsWithPriceInEuro() {
        return currencyRate -> {
            productAttributeRepository.findAllByAttributeId(PRICE_IN_EURO_ID)
                    .stream()
                    .filter(productPriceInEuro ->
                            BigDecimal.ZERO.compareTo(productPriceInEuro.getPrice()) != 0)
                    .forEach(productPriceInEuro ->
                            productAttributeRepository.findByProductIdAndAttributeId(productPriceInEuro.getProductId(), PRICE_IN_HRN)
                                    .ifPresent(productPriceInHrn -> {
                                        BigDecimal updatedPrice = productPriceInEuro.getPrice().divide(currencyRate, 0, ROUND_UP);
                                        productPriceInHrn.setPrice(updatedPrice);
                                        productAttributeRepository.save(productPriceInHrn);
                                    })
                    );
        };
    }

}

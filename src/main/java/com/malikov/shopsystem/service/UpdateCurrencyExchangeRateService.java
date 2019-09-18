package com.malikov.shopsystem.service;

import com.malikov.shopsystem.dao.gilder.domain.Currency;
import com.malikov.shopsystem.dao.gilder.repository.CurrencyRepository;
import com.malikov.shopsystem.dto.minfin.MinFinResponseDto;
import com.malikov.shopsystem.enumtype.CurrencyCode;
import com.malikov.shopsystem.exception.NotSupportedCurrencyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class UpdateCurrencyExchangeRateService {

    private static final String AUCTION_EXCHANGE_RATE_URL =
            "http://api.minfin.com.ua/auction/info/58ec1c89d7ea9221853cf7b777a02c686c455a03/";
    private static final int FOUR_HOURS = 4 * 60 * 60 * 1000;

    private final CurrencyRepository currencyRateRepository;
    private final RestTemplate restTemplate;

    public UpdateCurrencyExchangeRateService(CurrencyRepository currencyRepository,
                                             RestTemplate restTemplate) {
        this.currencyRateRepository = currencyRepository;
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedDelay = FOUR_HOURS)
    @Transactional
    protected void refreshEuroExchangeRate() {
        try {
            Currency euro = Optional.ofNullable(currencyRateRepository.findByCurrencyCode(CurrencyCode.EUR))
                    .orElseThrow(NotSupportedCurrencyException::new);

            Optional.ofNullable(requestEuroExchangeRate())
                    .ifPresent(newEuroExchangeRate -> {
                        BigDecimal currencyRate = BigDecimal.ONE.divide(newEuroExchangeRate, 6, RoundingMode.HALF_UP);
                        euro.setCurrencyRate(currencyRate);
                        euro.setLastUpdated(LocalDateTime.now());
                    });

            LocalDateTime now = LocalDateTime.now();
            euro.setLastUpdated(now);
            euro.setLastAutoUpdateAttempt(now);
            currencyRateRepository.save(euro);
        } catch (Exception e) {
            log.error("Scheduled exchange rate update attempt.");
        }
    }

    private BigDecimal requestEuroExchangeRate() {
        try {
            MinFinResponseDto responseBody = restTemplate.getForObject(URI.create(AUCTION_EXCHANGE_RATE_URL),
                    MinFinResponseDto.class);
            log.info("Received Minfin response. New EUR rate = {}", responseBody.getEur().getBid());
            return responseBody.getEur().getBid();

        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Refreshing currency rate failed.");
            return null;
        }
    }

}

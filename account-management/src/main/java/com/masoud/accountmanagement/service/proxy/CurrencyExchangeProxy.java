package com.masoud.accountmanagement.service.proxy;

import com.masoud.accountmanagement.service.dto.currencyexchange.ConversionRequestDTO;
import com.masoud.accountmanagement.service.dto.currencyexchange.ConversionResponseDTO;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "gateway", fallbackFactory = CurrencyExchangeFallbackFactory.class)
@RibbonClient(name = "currency-exchange")
public interface CurrencyExchangeProxy {
    @PostMapping("/currency-exchange/api/currency-conversion")
    public ResponseEntity<ConversionResponseDTO> currencyConversion(@Valid @RequestBody ConversionRequestDTO conversionRequestDTO);
}




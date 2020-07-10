package com.masoud.currencyexchange.rest;

import com.masoud.currencyexchange.service.CurrencyConversionService;
import com.masoud.currencyexchange.service.dto.ConversionRequestDTO;
import com.masoud.currencyexchange.service.dto.ConversionResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class CurrencyExchangeResource {

    private final Logger log = LoggerFactory.getLogger(CurrencyExchangeResource.class);

    @Autowired
    private CurrencyConversionService currencyConversionService;

    @PostMapping("/currency-conversion")
    public ResponseEntity<ConversionResponseDTO> currencyConversion(@Valid @RequestBody ConversionRequestDTO conversionRequestDTO) throws URISyntaxException {
        log.debug("REST request to get currencyConversion :{}, ", conversionRequestDTO);
        ConversionResponseDTO result = currencyConversionService.convertFromToAmount(conversionRequestDTO);
        return ResponseEntity.ok()
                .body(result);
    }
}

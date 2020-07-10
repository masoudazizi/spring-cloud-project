package com.masoud.currencyexchange.service;

import com.masoud.currencyexchange.service.dto.ConversionRequestDTO;
import com.masoud.currencyexchange.service.dto.ConversionResponseDTO;

import java.net.URISyntaxException;

public interface CurrencyConversionService {

    /**
     * convert an amount from a specific currency to specific currency.
     *
     * @param conversionRequestDTO
     * @return bigDecimal amount
     */
    ConversionResponseDTO convertFromToAmount(ConversionRequestDTO conversionRequestDTO) throws URISyntaxException;
}

package com.masoud.currencyexchange.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masoud.currencyexchange.service.dto.ExchangeRatesDTO;
import com.masoud.currencyexchange.rest.errors.ThirdPartyError;
import com.masoud.currencyexchange.service.CurrencyConversionService;
import com.masoud.currencyexchange.service.dto.ConversionRequestDTO;
import com.masoud.currencyexchange.service.dto.ConversionResponseDTO;
import com.masoud.currencyexchange.service.enumeration.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class CurrencyConversionImpl implements CurrencyConversionService {

    @Value("${application.currency-exchange.url}")
    private String currencyExchangeUrlService;

    @Override
    public ConversionResponseDTO convertFromToAmount(ConversionRequestDTO conversionRequestDTO) throws URISyntaxException {
        URI uri = new URI(currencyExchangeUrlService.replace("{from}", conversionRequestDTO.getFrom()));
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ExchangeRatesDTO exchangeRatesDto;
        ThirdPartyError thirdPartyError;
        ResponseEntity<String> result = null;
        try {
            result = restTemplate.getForEntity(uri, String.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new ConversionResponseDTO(Status.UNSUCCESSFUL, null, "from.currency.not.valid", "from currency is not valid");
            } else {
                return new ConversionResponseDTO(Status.UNSUCCESSFUL, null, "third.party.service.has.problem", "third party web service call has problem");
            }
        }
        try {
            if (result.getBody().contains("success")) {
                exchangeRatesDto = mapper.readValue(result.getBody(), ExchangeRatesDTO.class);
            } else {
                thirdPartyError = mapper.readValue(result.getBody(), ThirdPartyError.class);
                if (thirdPartyError.getError().contains("unknown-code")) {
                    return new ConversionResponseDTO(Status.UNSUCCESSFUL, null, "from.currency.not.valid", "from currency is not valid");
                } else {
                    return new ConversionResponseDTO(Status.UNSUCCESSFUL, null, "third.party.service.has.problem", "third party web service call has problem");
                }
            }
        } catch (JsonProcessingException e) {
            return new ConversionResponseDTO(Status.UNSUCCESSFUL, null, "third.party.service.has.problem", "third party web service call has problem");
        }
        if (exchangeRatesDto.getConversion_rates().get(conversionRequestDTO.getTo()) == null)
            return new ConversionResponseDTO(Status.UNSUCCESSFUL, null, "to.currency.not.valid", "to currency is not valid");

        ConversionResponseDTO conversionResponseDTO = new ConversionResponseDTO();
        conversionResponseDTO.setStatus(Status.SUCCESSFUL);
        conversionResponseDTO.setConversionResult(conversionRequestDTO.getAmount()
                .multiply(new BigDecimal(exchangeRatesDto.getConversion_rates().get(conversionRequestDTO.getTo()))));
        return conversionResponseDTO;
    }
}

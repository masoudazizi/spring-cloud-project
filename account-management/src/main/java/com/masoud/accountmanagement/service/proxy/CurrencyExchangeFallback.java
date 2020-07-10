
package com.masoud.accountmanagement.service.proxy;

import com.masoud.accountmanagement.service.dto.currencyexchange.ConversionRequestDTO;
import com.masoud.accountmanagement.service.dto.currencyexchange.ConversionResponseDTO;
import com.masoud.accountmanagement.service.enumeration.Status;
import feign.FeignException;
import org.mariadb.jdbc.internal.logging.Logger;
import org.mariadb.jdbc.internal.logging.LoggerFactory;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;

public class CurrencyExchangeFallback implements CurrencyExchangeProxy {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Throwable cause;

    public CurrencyExchangeFallback(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public ResponseEntity<ConversionResponseDTO> currencyConversion(@Valid ConversionRequestDTO conversionRequestDTO) {
        if (cause instanceof FeignException) {
            logger.error(cause.getLocalizedMessage());
            ConversionResponseDTO conversionResponseDTO = new ConversionResponseDTO();
            conversionResponseDTO.setErrorCode("currency.conversion.service.has.problem");
            conversionResponseDTO.setErrorCode("currency conversion service has problem");
            conversionResponseDTO.setStatus(Status.UNSUCCESSFUL);
            return ResponseEntity.ok().body(conversionResponseDTO);
        } else {
            ConversionResponseDTO conversionResponseDTO = new ConversionResponseDTO();
            conversionResponseDTO.setErrorCode("unknown.problem");
            conversionResponseDTO.setErrorCode("unknown problem");
            conversionResponseDTO.setStatus(Status.UNSUCCESSFUL);
            return ResponseEntity.ok().body(conversionResponseDTO);
        }
    }
}

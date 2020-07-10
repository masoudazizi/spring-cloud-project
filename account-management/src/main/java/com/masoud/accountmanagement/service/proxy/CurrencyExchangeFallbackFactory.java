package com.masoud.accountmanagement.service.proxy;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
class CurrencyExchangeFallbackFactory implements FallbackFactory<CurrencyExchangeProxy> {

    @Override
    public CurrencyExchangeProxy create(Throwable throwable) {
        return new CurrencyExchangeFallback(throwable);
    }


}
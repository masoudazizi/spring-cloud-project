package com.masoud.currencyexchange.service.dto;

import java.util.HashMap;
import java.util.Map;

public class ExchangeRatesDTO {
    private String result;
    private String documentation;
    private String terms_of_use;
    private String time_zone;
    private String time_last_update;
    private String time_next_update;
    private String base;
    private Map<String, Float> conversion_rates = new HashMap<>();

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public String getTerms_of_use() {
        return terms_of_use;
    }

    public void setTerms_of_use(String terms_of_use) {
        this.terms_of_use = terms_of_use;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public String getTime_last_update() {
        return time_last_update;
    }

    public void setTime_last_update(String time_last_update) {
        this.time_last_update = time_last_update;
    }

    public String getTime_next_update() {
        return time_next_update;
    }

    public void setTime_next_update(String time_next_update) {
        this.time_next_update = time_next_update;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Map<String, Float> getConversion_rates() {
        return conversion_rates;
    }

    public void setConversion_rates(Map<String, Float> conversion_rates) {
        this.conversion_rates = conversion_rates;
    }
}

package com.serdarberk.transferservice.VO;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "exchangeratesapi",contextId ="exchangerates")
public interface ExchangeRatesClient {
}

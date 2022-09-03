package dev.skaringa.qupa.configuration.nasdaq;

import dev.skaringa.qupa.api.nasdaq.client.NasdaqStockMarketDataClient;
import dev.skaringa.qupa.service.StockMarketDataClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;


@Configuration
public class NasdaqStockMarketDataClientConfig {
    @Bean
    public RestTemplate restTemplate(ApiData apiData, NasdaqAuthorizationRequestInterceptor authInterceptor) {
        return new RestTemplateBuilder()
                .rootUri(apiData.getRootUri())
                .interceptors(authInterceptor)
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofMinutes(1))
                .build();
    }

    @Bean
    public StockMarketDataClient nasdaqStockMarketDataClient(RestTemplate restTemplate) {
        return new NasdaqStockMarketDataClient(restTemplate);
    }
}

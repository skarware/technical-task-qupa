package dev.skaringa.qupa.api.nasdaq.client;

import dev.skaringa.qupa.api.nasdaq.dto.Response;
import dev.skaringa.qupa.exception.SystemException;
import dev.skaringa.qupa.service.StockMarketDataClient;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Objects;


@Log4j2
@RequiredArgsConstructor
public class NasdaqStockMarketDataClient implements StockMarketDataClient {
    private static final String STOCK_PRICES_DATA_URI = "/datasets/WIKI";

    private final RestTemplate restTemplate;

    @Override
    public Response.Dataset getStockData(String ticker, LocalDate from, LocalDate to) {
        Try<Response.Dataset> result = Try.of(() -> getDataset(ticker, from, to));
        if (result.isFailure()) {
            Throwable cause = result.getCause();
            String message = "Failed to get stock market data, due to: " + cause.getMessage();
            log.error(message, cause);
            throw SystemException.unexpected(message, cause);
        }

        Response.Dataset dataset = result.get();
        log.info(dataset);
        return dataset;
    }

    private Response.Dataset getDataset(String ticker, LocalDate from, LocalDate to) {
        String uri = uri(ticker, from, to);
        ResponseEntity<Response> response = restTemplate.getForEntity(uri, Response.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw SystemException.unexpected("Failed to get dataset. Response: " + response);
        }

        return Objects.requireNonNull(response.getBody()).getDataset();
    }

    private String uri(String ticker, LocalDate from, LocalDate to) {
        return UriComponentsBuilder.fromUriString(STOCK_PRICES_DATA_URI)
                .pathSegment(ticker)
                .queryParam("start_date", from)
                .queryParam("end_date", to)
                .toUriString();
    }
}

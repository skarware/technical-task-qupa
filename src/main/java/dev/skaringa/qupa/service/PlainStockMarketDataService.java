package dev.skaringa.qupa.service;

import dev.skaringa.qupa.api.nasdaq.dto.Response;
import dev.skaringa.qupa.factory.StockDatasetFactory;
import dev.skaringa.qupa.model.StockDataset;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Log4j2
@Service
@RequiredArgsConstructor
public class PlainStockMarketDataService implements StockMarketDataService {
    private final StockMarketDataClient stockMarketDataClient;
    private final StockDatasetFactory stockDatasetFactory;

    @Override
    public StockDataset getDataset(String symbol, LocalDate from, LocalDate to) {
        Response.Dataset rawDataset = stockMarketDataClient.getStockData(symbol, from, to);
        return stockDatasetFactory.toModel(rawDataset);
    }
}

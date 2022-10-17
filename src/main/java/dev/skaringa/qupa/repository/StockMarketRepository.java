package dev.skaringa.qupa.repository;

import dev.skaringa.qupa.model.StockDataset;

import java.time.LocalDate;
import java.util.Optional;

public interface StockMarketRepository {
    Optional<StockDataset> findByTickerAndDateRange(String ticker, LocalDate from, LocalDate to);

    Optional<StockDataset> findByTickerAndDate(String ticker, LocalDate date);

    Optional<StockDataset> findByTicker(String ticker);

    StockDataset save(StockDataset dataset);
}

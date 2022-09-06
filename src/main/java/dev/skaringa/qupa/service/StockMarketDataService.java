package dev.skaringa.qupa.service;

import dev.skaringa.qupa.model.StockDataset;

import java.time.LocalDate;

public interface StockMarketDataService {
    StockDataset getDataset(String symbol, LocalDate from, LocalDate to);
}

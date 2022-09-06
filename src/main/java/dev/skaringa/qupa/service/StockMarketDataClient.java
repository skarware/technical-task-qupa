package dev.skaringa.qupa.service;

import dev.skaringa.qupa.api.nasdaq.dto.Response;

import java.time.LocalDate;

public interface StockMarketDataClient {
    Response.Dataset getDataset(String ticker, LocalDate from, LocalDate to);
}

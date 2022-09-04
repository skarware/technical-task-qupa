package dev.skaringa.qupa.service;

import dev.skaringa.qupa.factory.ChartFactory;
import dev.skaringa.qupa.model.Chart;
import dev.skaringa.qupa.model.ChartDataEntry;
import dev.skaringa.qupa.model.ChartRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChartService {
    private final StockMarketDataClient stockMarketDataClient;
    private final ChartFactory chartFactory;

    public Chart<ChartDataEntry> getCandlestickChart(ChartRequest request) {
        String ticker = request.getTicker();
        LocalDate from = request.getFrom();
        LocalDate to = request.getTo();
        return chartFactory.toCandlestickChartModel(stockMarketDataClient.getStockData(ticker, from, to));
    }

    public Chart<ChartDataEntry> getVolumeChart(ChartRequest request) {
        String ticker = request.getTicker();
        LocalDate from = request.getFrom();
        LocalDate to = request.getTo();
        return chartFactory.toVolumeChartModel(stockMarketDataClient.getStockData(ticker, from, to));
    }
}

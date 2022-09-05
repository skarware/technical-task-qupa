package dev.skaringa.qupa.service;

import dev.skaringa.qupa.api.nasdaq.dto.Response;
import dev.skaringa.qupa.factory.ChartFactory;
import dev.skaringa.qupa.factory.StockDatasetFactory;
import dev.skaringa.qupa.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChartService {
    private final StockMarketDataClient stockMarketDataClient;
    private final SimpleMovingAverageCalculator simpleMovingAverageCalculator;
    private final ChartFactory chartFactory;
    private final StockDatasetFactory stockDatasetFactory;

    public Chart<ChartDataEntry> getCandlestickChart(ChartRequest request) {
        String ticker = request.getTicker();
        LocalDate from = request.getFrom();
        LocalDate to = request.getTo();
        return chartFactory.toDailyCandlestickChartModel(stockMarketDataClient.getStockData(ticker, from, to));
    }

    public Chart<ChartDataEntry> getVolumeChart(ChartRequest request) {
        String ticker = request.getTicker();
        LocalDate from = request.getFrom();
        LocalDate to = request.getTo();
        return chartFactory.toDailyVolumeChartModel(stockMarketDataClient.getStockData(ticker, from, to));
    }

    public Chart<ChartDataEntry> getSMAChart(ChartRequest request, int period) {
        String ticker = request.getTicker();
        LocalDate from = request.getFrom();
        LocalDate to = request.getTo();
        LocalDate fromWithLossMarketCloseDaysOffset = from.minusDays(period * 2L);
        Response.Dataset rawDataset = stockMarketDataClient.getStockData(ticker, fromWithLossMarketCloseDaysOffset, to);
        StockDataset stockDataset = stockDatasetFactory.toModel(rawDataset);
        List<ChartDataEntry> movingAverages = simpleMovingAverageCalculator.calculate(stockDataset, from, period);
        return new Chart<>(stockDataset.getSymbol(), from, to, ChartType.SMA, movingAverages);
    }
}

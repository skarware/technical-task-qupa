package dev.skaringa.qupa.service;

import dev.skaringa.qupa.factory.ChartFactory;
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
    private final StockMarketDataService stockMarketDataService;
    private final SimpleMovingAverageCalculator simpleMovingAverageCalculator;
    private final ChartFactory chartFactory;

    public Chart<ChartDataEntry> getCandlestickChart(ChartRequest request) {
        String ticker = request.getTicker();
        LocalDate from = request.getFrom();
        LocalDate to = request.getTo();
        return chartFactory.toDailyCandlestickChartModel(stockMarketDataService.getDataset(ticker, from, to));
    }

    public Chart<ChartDataEntry> getVolumeChart(ChartRequest request) {
        String ticker = request.getTicker();
        LocalDate from = request.getFrom();
        LocalDate to = request.getTo();
        return chartFactory.toDailyVolumeChartModel(stockMarketDataService.getDataset(ticker, from, to));
    }

    public Chart<ChartDataEntry> getSMAChart(ChartRequest request) {
        String ticker = request.getTicker();
        LocalDate from = request.getFrom();
        LocalDate to = request.getTo();
        int period = request.getPeriod();

        LocalDate fromWithLossMarketCloseDaysOffset = from.minusDays(period * 2L);
        StockDataset dataset = stockMarketDataService.getDataset(ticker, fromWithLossMarketCloseDaysOffset, to);
        List<ChartDataEntry> movingAverages = simpleMovingAverageCalculator.calculate(dataset, from, period);
        return new Chart<>(dataset.getSymbol(), from, to, ChartType.SMA, period, movingAverages);
    }
}

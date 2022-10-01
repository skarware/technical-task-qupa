package dev.skaringa.qupa.service;

import dev.skaringa.qupa.factory.ChartFactory;
import dev.skaringa.qupa.model.Chart;
import dev.skaringa.qupa.model.ChartDataEntry;
import dev.skaringa.qupa.model.ChartRequest;
import dev.skaringa.qupa.service.simplemovingaverage.SimpleMovingAverageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChartService {
    private final StockMarketDataService stockMarketDataService;
    private final ChartFactory chartFactory;
    private final SimpleMovingAverageService simpleMovingAverageService;

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
        return simpleMovingAverageService.getChart(ticker, from, to, period);
    }
}

package dev.skaringa.qupa.service;

import dev.skaringa.qupa.factory.CandlestickChartFactory;
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
    private final ChartDataClient chartDataClient;
    private final CandlestickChartFactory candlestickChartFactory;

    public Chart<ChartDataEntry> getCandlestickChart(ChartRequest request) {
        log.info("Got getCandlestickChart request: {}", request);
        String ticker = request.getTicker();
        LocalDate from = request.getFrom();
        LocalDate to = request.getTo();
        Object candlestickChart = chartDataClient.getCandlestickChart(ticker, from, to);
        return candlestickChartFactory.toModel(candlestickChart);
    }

    public Chart<ChartDataEntry> getVolumeChart(ChartRequest request) {
        log.info("Got getVolumeChart request: {}", request);
        String ticker = request.getTicker();
        LocalDate from = request.getFrom();
        LocalDate to = request.getTo();
        Object candlestickChart = chartDataClient.getVolumeChart(ticker, from, to);
        return candlestickChartFactory.toModel(candlestickChart);
    }
}

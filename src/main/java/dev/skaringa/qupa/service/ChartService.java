package dev.skaringa.qupa.service;

import dev.skaringa.qupa.api.nasdaq.dto.Response;
import dev.skaringa.qupa.factory.CandlestickChartFactory;
import dev.skaringa.qupa.factory.VolumeChartFactory;
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
    private final CandlestickChartFactory candlestickChartFactory;
    private final VolumeChartFactory volumeChartFactory;

    public Chart<ChartDataEntry> getCandlestickChart(ChartRequest request) {
        String ticker = request.getTicker();
        LocalDate from = request.getFrom();
        LocalDate to = request.getTo();
        Response.Dataset dataset = stockMarketDataClient.getStockData(ticker, from, to);
        return candlestickChartFactory.toModel(dataset);
    }

    public Chart<ChartDataEntry> getVolumeChart(ChartRequest request) {
        String ticker = request.getTicker();
        LocalDate from = request.getFrom();
        LocalDate to = request.getTo();
        Response.Dataset dataset = stockMarketDataClient.getStockData(ticker, from, to);
        return volumeChartFactory.toModel(dataset);
    }
}

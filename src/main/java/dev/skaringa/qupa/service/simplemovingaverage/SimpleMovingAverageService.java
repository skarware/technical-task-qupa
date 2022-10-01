package dev.skaringa.qupa.service.simplemovingaverage;

import dev.skaringa.qupa.model.Chart;
import dev.skaringa.qupa.model.ChartDataEntry;
import dev.skaringa.qupa.model.ChartType;
import dev.skaringa.qupa.model.StockDataset;
import dev.skaringa.qupa.service.StockMarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SimpleMovingAverageService {
    private final StockMarketDataService stockMarketDataService;
    private final DatasetAdjusterForMarketClosedDays datasetAdjusterForMarketClosedDays;
    private final SimpleMovingAverageCalculator simpleMovingAverageCalculator;

    public Chart<ChartDataEntry> getChart(String ticker, LocalDate from, LocalDate to, int period) {
        LocalDate fromWithOffset = from.minusDays(period);
        StockDataset dataset = stockMarketDataService.getDataset(ticker, fromWithOffset, to);
        StockDataset adjustedDataset = datasetAdjusterForMarketClosedDays.adjust(dataset, from, period);
        List<ChartDataEntry> entries = simpleMovingAverageCalculator.calculate(adjustedDataset, from, period);
        return new Chart<>(ticker, from, to, ChartType.SMA, period, entries);
    }
}
package dev.skaringa.qupa.service.simplemovingaverage;

import dev.skaringa.qupa.model.StockDataset;
import dev.skaringa.qupa.service.StockMarketDataService;
import dev.skaringa.qupa.service.util.StockDatasetMerger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class DatasetAdjusterForMarketClosedDays {
    private final StockMarketDataService stockMarketDataService;

    public StockDataset adjust(StockDataset dataset, LocalDate from, int period) {
        boolean datasetIsNotEmpty = !dataset.getData().isEmpty();
        if (datasetIsNotEmpty) {
            String ticker = dataset.getSymbol();
            LocalDate dateToProbe = dataset.getFrom();
            while (countOffsetDays(dataset, from) < period) {
                dateToProbe = dateToProbe.minusDays(1);
                StockDataset dayDataset = stockMarketDataService.getDataset(ticker, dateToProbe);
                dataset = StockDatasetMerger.merge(dataset, dayDataset);
            }
        }

        return dataset;
    }

    private long countOffsetDays(StockDataset dataset, LocalDate date) {
        return Stream.of(dataset.getData())
                .flatMap(Collection::stream)
                .filter(entry -> entry.getDate().isBefore(date))
                .count();
    }
}

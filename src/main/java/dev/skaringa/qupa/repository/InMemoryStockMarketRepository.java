package dev.skaringa.qupa.repository;

import dev.skaringa.qupa.model.StockDataset;
import dev.skaringa.qupa.util.StockDatasetMerger;
import dev.skaringa.qupa.util.StockDatasetSorter;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryStockMarketRepository implements StockMarketRepository {
    private final Map<String, StockDataset> tickerToStockDatasets = new HashMap<>();

    @Override
    public Optional<StockDataset> findByTickerAndDateRange(String ticker, LocalDate from, LocalDate to) {
        Optional<StockDataset> dataset = findByTicker(ticker);
        if (dataset.isPresent()) {
            List<StockDataset.DataEntry> filteredEntries = filter(dataset.get().getData(), from, to);
            boolean filteredEntriesIsNotEmpty = !filteredEntries.isEmpty();
            if (filteredEntriesIsNotEmpty) {
                return Optional.of(new StockDataset(ticker, from, to, filteredEntries));
            }
        }

        return Optional.empty();
    }

    private List<StockDataset.DataEntry> filter(List<StockDataset.DataEntry> data, LocalDate from, LocalDate to) {
        return data.stream()
                .filter(entry -> entry.getDate().isAfter(from.minusDays(1)))
                .filter(entry -> entry.getDate().isBefore(to.plusDays(1)))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<StockDataset> findByTickerAndDate(String ticker, LocalDate date) {
        return findByTickerAndDateRange(ticker, date, date);
    }

    @Override
    public Optional<StockDataset> findByTicker(String ticker) {
        return Optional.ofNullable(tickerToStockDatasets.get(ticker));
    }

    public Set<StockDataset> findAll() {
        return tickerToStockDatasets.values().parallelStream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public StockDataset save(StockDataset dataset) {
        Optional<StockDataset> storedDataset = findByTicker(dataset.getSymbol());
        if (storedDataset.isPresent()) {
            dataset = StockDatasetMerger.merge(storedDataset.get(), dataset);
        }
        StockDataset sortedDataset = StockDatasetSorter.sortDataEntriesByDate(dataset);
        tickerToStockDatasets.put(sortedDataset.getSymbol(), sortedDataset);
        return sortedDataset;
    }
}

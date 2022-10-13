package dev.skaringa.qupa.util;

import dev.skaringa.qupa.model.StockDataset;
import lombok.experimental.UtilityClass;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class StockDatasetSorter {
    public StockDataset sortDataEntriesByDate(StockDataset dataset) {
        List<StockDataset.DataEntry> sortedData = sortByDate(dataset.getData());

        return new StockDataset(dataset.getSymbol(), dataset.getFrom(), dataset.getTo(), sortedData);
    }

    public StockDataset sortDataEntriesByDateReverse(StockDataset dataset) {
        List<StockDataset.DataEntry> sortedData = sortByDateReversed(dataset.getData());

        return new StockDataset(dataset.getSymbol(), dataset.getFrom(), dataset.getTo(), sortedData);
    }

    private List<StockDataset.DataEntry> sortByDate(List<StockDataset.DataEntry> dataEntries) {
        return dataEntries.stream()
                .sorted(Comparator.comparing(StockDataset.DataEntry::getDate))
                .collect(Collectors.toUnmodifiableList());
    }

    private List<StockDataset.DataEntry> sortByDateReversed(List<StockDataset.DataEntry> dataEntries) {
        return dataEntries.stream()
                .sorted(Comparator.comparing(StockDataset.DataEntry::getDate).reversed())
                .collect(Collectors.toUnmodifiableList());
    }
}

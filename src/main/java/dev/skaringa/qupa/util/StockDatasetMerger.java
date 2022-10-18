package dev.skaringa.qupa.util;

import dev.skaringa.qupa.model.StockDataset;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class StockDatasetMerger {
    public StockDataset merge(StockDataset... datasets) {
        LocalDate from = Stream.of(datasets).map(StockDataset::getFrom).min(LocalDate::compareTo).orElseThrow();
        LocalDate to = Stream.of(datasets).map(StockDataset::getTo).max(LocalDate::compareTo).orElseThrow();
        String symbol = Arrays.stream(datasets).findFirst().orElseThrow().getSymbol();

        List<StockDataset.DataEntry> mergedData = Stream.of(datasets)
                .map(StockDataset::getData)
                .flatMap(Collection::stream)
                .collect(
                        Collectors.toMap(
                                StockDataset.DataEntry::getDate, Function.identity(), (left, right) -> right))
                .values().stream()
                .sorted(Comparator.comparing(StockDataset.DataEntry::getDate))
                .collect(Collectors.toUnmodifiableList());

        return new StockDataset(symbol, from, to, mergedData);
    }
}

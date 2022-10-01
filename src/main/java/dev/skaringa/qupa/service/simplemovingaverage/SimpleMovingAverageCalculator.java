package dev.skaringa.qupa.service.simplemovingaverage;

import dev.skaringa.qupa.exception.SystemException;
import dev.skaringa.qupa.model.ChartDataEntry;
import dev.skaringa.qupa.model.DailySingleValueChartDataEntry;
import dev.skaringa.qupa.model.StockDataset;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SimpleMovingAverageCalculator {
    public List<ChartDataEntry> calculate(StockDataset stockDataset, LocalDate from, int period) {
        Assert.isTrue(period > 0, "SMA period value must be greater than zero");
        List<StockDataset.DataEntry> filteredEntries = filterCalculableEntries(stockDataset.getData(), from);
        return Stream.of(filteredEntries)
                .flatMap(Collection::stream)
                .map(createSMAChartDataEntry(stockDataset, period))
                .collect(Collectors.toUnmodifiableList());
    }

    private Function<StockDataset.DataEntry, ChartDataEntry> createSMAChartDataEntry(
            StockDataset stockDataset, int period) {
        return entry -> new DailySingleValueChartDataEntry<>(
                entry.getDate(), calculateSMAOnDate(stockDataset.getData(), entry.getDate(), period));
    }

    private List<StockDataset.DataEntry> filterCalculableEntries(
            List<StockDataset.DataEntry> dataEntries, LocalDate from) {
        return Stream.of(dataEntries)
                .flatMap(Collection::stream)
                .filter(entry -> entry.getDate().isAfter(from.minusDays(1)))
                .collect(Collectors.toUnmodifiableList());
    }

    private BigDecimal calculateSMAOnDate(List<StockDataset.DataEntry> dataEntries, LocalDate date, int period) {
        List<StockDataset.DataEntry> reversedPeriodEntries = splitPeriodDataEntries(dataEntries, date, period);
        if (reversedPeriodEntries.size() != period) {
            throw SystemException.unexpected("Not enough stock data entries to calculate SMA");
        }

        return Stream.of(reversedPeriodEntries)
                .flatMap(Collection::stream)
                .map(entry -> extractClosingPrice(dataEntries, entry.getDate()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(period), MathContext.DECIMAL128);
    }

    private List<StockDataset.DataEntry> splitPeriodDataEntries(
            List<StockDataset.DataEntry> dataEntries, LocalDate date, int period) {
        return Stream.of(dataEntries)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(StockDataset.DataEntry::getDate).reversed())
                .filter(entry -> entry.getDate().isBefore(date))
                .limit(period)
                .collect(Collectors.toUnmodifiableList());
    }

    private BigDecimal extractClosingPrice(List<StockDataset.DataEntry> dataEntries, LocalDate date) {
        return Stream.of(dataEntries)
                .flatMap(Collection::stream)
                .filter(entry -> entry.getDate().equals(date))
                .findFirst()
                .orElseThrow()
                .getClose();
    }
}

package dev.skaringa.qupa.factory;

import dev.skaringa.qupa.model.ChartDataEntry;
import dev.skaringa.qupa.model.DailyCandlestickChartDataEntry;
import dev.skaringa.qupa.model.StockDataset;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class DailyCandlestickChartDataEntryFactory {
    public List<ChartDataEntry> toModels(List<StockDataset.DataEntry> source) {
        Assert.notNull(source, "Daily candlestick chart data entries 'source' must not be null");
        return source.stream()
                .map(this::toModel)
                .collect(Collectors.toUnmodifiableList());
    }

    private ChartDataEntry toModel(StockDataset.DataEntry source) {
        return new DailyCandlestickChartDataEntry(
                source.getDate(), source.getOpen(), source.getHigh(), source.getLow(), source.getClose());
    }
}

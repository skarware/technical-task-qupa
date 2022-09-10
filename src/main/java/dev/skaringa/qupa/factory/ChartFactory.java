package dev.skaringa.qupa.factory;

import dev.skaringa.qupa.model.Chart;
import dev.skaringa.qupa.model.ChartDataEntry;
import dev.skaringa.qupa.model.ChartType;
import dev.skaringa.qupa.model.StockDataset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChartFactory {
    private final DailyCandlestickChartDataEntryFactory dailyCandlestickChartDataEntryFactory;
    private final DailySingleValueChartDataEntryFactory dailySingleValueChartDataEntryFactory;

    public Chart<ChartDataEntry> toDailyCandlestickChartModel(StockDataset source) {
        return toModel(source, ChartType.CANDLESTICK);
    }

    public Chart<ChartDataEntry> toDailyVolumeChartModel(StockDataset source) {
        return toModel(source, ChartType.VOLUME);
    }

    private Chart<ChartDataEntry> toModel(StockDataset source, ChartType chartType) {
        Assert.notNull(source, "Chart 'source' must not be null");
        return new Chart<>(
                source.getSymbol(),
                source.getFrom(),
                source.getTo(),
                chartType,
                null,
                toChartDataEntryModels(source.getData(), chartType));
    }

    private List<ChartDataEntry> toChartDataEntryModels(List<StockDataset.DataEntry> source, ChartType chartType) {
        Assert.notNull(source, "Chart data entry 'source' must not be null");
        if (chartType == ChartType.CANDLESTICK) {
            return dailyCandlestickChartDataEntryFactory.toModels(source);
        } else if (chartType == ChartType.VOLUME) {
            return dailySingleValueChartDataEntryFactory.toModels(source);
        }

        throw new IllegalArgumentException("Unexpected type of chart data: " + chartType);
    }
}

package dev.skaringa.qupa.factory;

import dev.skaringa.qupa.api.nasdaq.dto.Response;
import dev.skaringa.qupa.model.Chart;
import dev.skaringa.qupa.model.ChartDataEntry;
import dev.skaringa.qupa.model.ChartType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChartFactory {
    private final DailyCandlestickChartDataEntryFactory dailyCandlestickChartDataEntryFactory;
    private final DailySingleValueChartDataEntryFactory dailySingleValueChartDataEntryFactory;

    public Chart<ChartDataEntry> toDailyCandlestickChartModel(Response.Dataset source) {
        return toModel(source, ChartType.CANDLESTICK);
    }

    public Chart<ChartDataEntry> toDailyVolumeChartModel(Response.Dataset source) {
        return toModel(source, ChartType.VOLUME);
    }

    private Chart<ChartDataEntry> toModel(Response.Dataset source, ChartType chartType) {
        Assert.notNull(source, "Chart 'source' must not be null");
        return new Chart<>(
                source.getDatasetCode(),
                source.getStartDate(),
                source.getEndDate(),
                chartType,
                toChartDataEntryModels(source.getData(), chartType));
    }

    private List<ChartDataEntry> toChartDataEntryModels(List<Response.Dataset.DataEntry> source, ChartType chartType) {
        Assert.notNull(source, "Chart data entry 'source' must not be null");
        if (chartType == ChartType.CANDLESTICK) {
            return dailyCandlestickChartDataEntryFactory.toModels(source);
        } else if (chartType == ChartType.VOLUME) {
            return dailySingleValueChartDataEntryFactory.toModels(source);
        }

        throw new IllegalArgumentException("Unexpected type of chart data: " + chartType);
    }
}

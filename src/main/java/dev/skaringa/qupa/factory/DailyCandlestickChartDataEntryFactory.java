package dev.skaringa.qupa.factory;

import dev.skaringa.qupa.api.nasdaq.dto.Response;
import dev.skaringa.qupa.model.ChartDataEntry;
import dev.skaringa.qupa.model.DailyCandlestickChartDataEntry;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class DailyCandlestickChartDataEntryFactory {
    public List<ChartDataEntry> toModels(List<Response.Dataset.DataEntry> source) {
        Assert.notNull(source, "Daily candlestick chart data entries 'source' must not be null");
        return source.stream()
                .map(this::toModel)
                .collect(Collectors.toUnmodifiableList());
    }

    private ChartDataEntry toModel(Response.Dataset.DataEntry source) {
        return new DailyCandlestickChartDataEntry(
                source.getDate(), source.getOpen(), source.getHigh(), source.getLow(), source.getClose());
    }
}

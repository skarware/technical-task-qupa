package dev.skaringa.qupa.factory;

import dev.skaringa.qupa.api.nasdaq.dto.Response;
import dev.skaringa.qupa.model.StockDataset;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StockDatasetDataEntryFactory {
    public List<StockDataset.DataEntry> toModels(List<Response.Dataset.DataEntry> source) {
        Assert.notNull(source, "Stock dataset data entries 'source' must not be null");
        return source.stream()
                .map(this::toModel)
                .collect(Collectors.toUnmodifiableList());
    }

    private StockDataset.DataEntry toModel(Response.Dataset.DataEntry source) {
        return new StockDataset.DataEntry(
                source.getDate(),
                source.getOpen(),
                source.getHigh(),
                source.getLow(),
                source.getClose(),
                source.getVolume());
    }
}

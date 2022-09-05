package dev.skaringa.qupa.factory;

import dev.skaringa.qupa.api.nasdaq.dto.Response;
import dev.skaringa.qupa.model.StockDataset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class StockDatasetFactory {
    private final StockDatasetDataEntryFactory stockDatasetDataEntryFactory;

    public StockDataset toModel(Response.Dataset source) {
        Assert.notNull(source, "Stock dataset 'source' must not be null");
        return new StockDataset(
                source.getDatasetCode(),
                source.getStartDate(),
                source.getEndDate(),
                stockDatasetDataEntryFactory.toModels(source.getData()));
    }
}

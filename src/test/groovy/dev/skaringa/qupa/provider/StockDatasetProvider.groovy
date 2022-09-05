package dev.skaringa.qupa.provider


import dev.skaringa.qupa.model.StockDataset

import java.time.LocalDate

class StockDatasetProvider {
    private static final def SYMBOL = "symbol"
    private static final def FROM = LocalDate.of(2010, 1, 1)
    private static final def TO = LocalDate.of(2010, 1, 5)
    private static final Map SAMPLE = [
            symbol: SYMBOL,
            from  : FROM,
            to    : TO]

    static def model(Map<String, Object> map = [:]) {
        def props = SAMPLE + [data: [StockDatasetDataEntryProvider.model()]] + map
        return new StockDataset(
                props.symbol as String,
                props.from as LocalDate,
                props.to as LocalDate,
                props.data as List<StockDataset.DataEntry>)
    }
}

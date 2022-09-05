package dev.skaringa.qupa.provider


import dev.skaringa.qupa.model.StockDataset

import java.time.LocalDate

class StockDatasetDataEntryDtoProvider {
    private static final def DATE = LocalDate.of(2010, 1, 5)
    private static final def OPEN = new BigDecimal("214.6")
    private static final def HIGH = new BigDecimal("215.59")
    private static final def LOW = new BigDecimal("213.25")
    private static final def CLOSE = new BigDecimal("214.38")
    private static final def VOLUME = 21496600L
    private static final Map SAMPLE = [
            date  : DATE,
            open  : OPEN,
            high  : HIGH,
            low   : LOW,
            close : CLOSE,
            volume: VOLUME]

    static def dto(Map<String, Object> map = [:]) {
        def props = SAMPLE + map
        return new StockDataset.DataEntry(
                props.date as LocalDate,
                props.open as BigDecimal,
                props.high as BigDecimal,
                props.low as BigDecimal,
                props.close as BigDecimal,
                props.volume as Long)
    }
}

package dev.skaringa.qupa.provider

import dev.skaringa.qupa.api.nasdaq.dto.Response

import java.time.LocalDate

class NasdaqDatasetDataEntryDtoProvider {
    private static final def DATE = LocalDate.of(2010, 1, 5)
    private static final def OPEN = new BigDecimal("214.6")
    private static final def HIGH = new BigDecimal("215.59")
    private static final def LOW = new BigDecimal("213.25")
    private static final def CLOSE = new BigDecimal("214.38")
    private static final def VOLUME = 21496600L
    private static final def EX_DIVIDEND = 0
    private static final def SPLIT_RATIO = 1
    private static final def ADJ_OPEN = new BigDecimal("27.579091019809")
    private static final def ADJ_HIGH = new BigDecimal("27.706319818083")
    private static final def ADJ_LOW = new BigDecimal("27.405597203981")
    private static final def ADJ_CLOSE = new BigDecimal("27.550817953526")
    private static final def ADJ_VOLUME = 21496600L
    private static final Map SAMPLE = [
            date      : DATE,
            open      : OPEN,
            high      : HIGH,
            low       : LOW,
            close     : CLOSE,
            volume    : VOLUME,
            exDividend: EX_DIVIDEND,
            splitRatio: SPLIT_RATIO,
            adjOpen   : ADJ_OPEN,
            adjHigh   : ADJ_HIGH,
            adjLow    : ADJ_LOW,
            adjClose  : ADJ_CLOSE,
            adjVolume : ADJ_VOLUME]

    static def dto(Map<String, Object> map = [:]) {
        def props = SAMPLE + map
        return new Response.Dataset.DataEntry(
                props.date as LocalDate,
                props.open as BigDecimal,
                props.high as BigDecimal,
                props.low as BigDecimal,
                props.close as BigDecimal,
                props.volume as Long,
                props.exDividend as Integer,
                props.splitRatio as Integer,
                props.adjOpen as BigDecimal,
                props.adjHigh as BigDecimal,
                props.adjLow as BigDecimal,
                props.adjClose as BigDecimal,
                props.adjVolume as Long)
    }
}

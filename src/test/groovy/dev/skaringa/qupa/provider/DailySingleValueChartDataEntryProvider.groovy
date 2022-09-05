package dev.skaringa.qupa.provider

import dev.skaringa.qupa.model.DailySingleValueChartDataEntry

import java.time.LocalDate

class DailySingleValueChartDataEntryProvider {
    private static final def DATE = LocalDate.of(2010, 1, 5)
    private static final def VALUE = new BigDecimal("123.456")
    private static final Map SAMPLE = [
            date : DATE,
            value: VALUE]

    static def model(Map<String, Object> map = [:]) {
        def props = SAMPLE + map
        return new DailySingleValueChartDataEntry(props.date as LocalDate, props.value as BigDecimal)
    }
}

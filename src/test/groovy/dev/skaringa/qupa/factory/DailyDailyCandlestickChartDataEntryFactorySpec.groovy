package dev.skaringa.qupa.factory


import dev.skaringa.qupa.provider.StockDatasetDataEntryProvider
import spock.lang.Specification

import java.time.LocalDate

class DailyDailyCandlestickChartDataEntryFactorySpec extends Specification {
    private def factory = new DailyCandlestickChartDataEntryFactory()

    def "creates daily candlestick chart data entry models correctly"() {
        given: "stock dataset models"
        def entry1 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 3, 4)])
        def entry2 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2023, 4, 5)])

        when: "toModels is called"
        def result = factory.toModels([entry1, entry2])

        then: "created chart entry models have correct values"
        result.size() == 2
        def resultEntry1 = result[0]
        resultEntry1.date == entry1.date
        resultEntry1.open == entry1.open
        resultEntry1.high == entry1.high
        resultEntry1.low == entry1.low
        resultEntry1.close == entry1.close
        def resultEntry2 = result[1]
        resultEntry2.date == entry2.date
        resultEntry2.open == entry2.open
        resultEntry2.high == entry2.high
        resultEntry2.low == entry2.low
        resultEntry2.close == entry2.close
    }
}

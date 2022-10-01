package dev.skaringa.qupa.service.simplemovingaverage

import dev.skaringa.qupa.SpecBaseIT
import dev.skaringa.qupa.factory.StockDatasetDataEntryFactory
import dev.skaringa.qupa.provider.NasdaqDatasetDataEntryDtoProvider
import dev.skaringa.qupa.provider.StockDatasetDataEntryProvider
import dev.skaringa.qupa.service.StockMarketDataClient
import org.springframework.beans.factory.annotation.Autowired

import java.time.LocalDate

class DatasetAdjusterForMarketClosedDaysSpec extends SpecBaseIT {
    @Autowired
    private DatasetAdjusterForMarketClosedDays datasetAdjusterForMarketClosedDays
    @Autowired
    private StockMarketDataClient stockMarketDataClient
    @Autowired
    private StockDatasetDataEntryFactory entryFactory

    def "does not adjusts dataset when data is empty"() {
        given: "Dataset, SMA period and date range"
        def period = 1
        def from = LocalDate.of(2012, 1, 1)
        def to = LocalDate.of(2012, 1, 3)
        def fromWithOffset = from.minusDays(period)
        def dataset = stockDataset([symbol: TICKER, from: fromWithOffset, to: to, data: []])

        when: "adjust is called"
        def result = datasetAdjusterForMarketClosedDays.adjust(dataset, from, period)

        then: "correct result is returned"
        result.symbol == TICKER
        result.from == fromWithOffset
        result.to == to
        result.data == []

        and: "stock market data client stub is not called - dataset is not adjusted"
        0 * stockMarketDataClient.getDataset(_)
    }

    def "does not adjust dataset when data size is correct"() {
        given: "Dataset, SMA period and date range"
        def period = 2
        def from = LocalDate.of(2012, 1, 1)
        def to = LocalDate.of(2012, 1, 3)
        def fromWithOffset = from.minusDays(period)
        def entry0 = StockDatasetDataEntryProvider.model([date: from])
        def entry1 = StockDatasetDataEntryProvider.model([date: from.minusDays(1)])
        def entry2 = StockDatasetDataEntryProvider.model([date: from.minusDays(2)])
        def dataset = stockDataset([symbol: TICKER, from: fromWithOffset, to: to, data: [entry0, entry1, entry2]])

        when: "adjust is called"
        def result = datasetAdjusterForMarketClosedDays.adjust(dataset, from, period)

        then: "correct result is returned"
        result.symbol == TICKER
        result.from == fromWithOffset
        result.to == to
        result.data == [entry0, entry1, entry2]

        and: "stock market data client stub is not called - dataset is not adjusted"
        0 * stockMarketDataClient.getDataset(_)
    }

    def "does not adjust dataset when data size is correct and data entry dates are not consecutive"() {
        given:
        def period = 2
        def from = LocalDate.of(2012, 1, 1)
        def to = LocalDate.of(2012, 1, 3)
        def fromWithOffset = from.minusDays(period)
        def entry0 = StockDatasetDataEntryProvider.model([date: from])
        def entry1 = StockDatasetDataEntryProvider.model([date: from.minusDays(3)])
        def entry2 = StockDatasetDataEntryProvider.model([date: from.minusDays(9)])
        def dataset = stockDataset([symbol: TICKER, from: fromWithOffset, to: to, data: [entry0, entry1, entry2]])

        when:
        def result = datasetAdjusterForMarketClosedDays.adjust(dataset, from, period)

        then:
        result.symbol == TICKER
        result.from == fromWithOffset
        result.to == to
        result.data == [entry0, entry1, entry2]

        and: "stock market data client stub is not called - dataset is not adjusted"
        0 * stockMarketDataClient.getDataset(_)
    }

    def "adjusts dataset when data is missing one offset entry out of one"() {
        given:
        def period = 1
        def from = LocalDate.of(2012, 1, 1)
        def to = LocalDate.of(2012, 1, 3)
        def fromWithOffset = from.minusDays(period)
        def entry0 = StockDatasetDataEntryProvider.model([date: from])
        def dataset = stockDataset([symbol: TICKER, from: fromWithOffset, to: to, data: [entry0]])

        and: "stub returns stock market adjusted data"
        def adjustedDateToProbe = fromWithOffset.minusDays(1)
        def adjustedEntry = NasdaqDatasetDataEntryDtoProvider.dto([date: adjustedDateToProbe])
        def adjustedDataset = nasdaqDataset([datasetCode: TICKER, startDate: adjustedDateToProbe, endDate: adjustedDateToProbe, data: [adjustedEntry]])
        1 * stockMarketDataClient.getDataset(TICKER, adjustedDateToProbe, adjustedDateToProbe) >> adjustedDataset

        when:
        def result = datasetAdjusterForMarketClosedDays.adjust(dataset, from, period)

        then:
        result.symbol == TICKER
        result.from == adjustedDateToProbe
        result.to == to
        def adjustedEntryModels = entryFactory.toModels([adjustedEntry])
        result.data == [entry0, *adjustedEntryModels]
    }

    def "adjusts dataset when data is missing one offset entry out of three"() {
        given:
        def period = 3
        def from = LocalDate.of(2012, 1, 1)
        def to = LocalDate.of(2012, 1, 3)
        def fromWithOffset = from.minusDays(period)
        def entry0 = StockDatasetDataEntryProvider.model([date: from])
        def entry1 = StockDatasetDataEntryProvider.model([date: from.minusDays(1)])
        def entry2 = StockDatasetDataEntryProvider.model([date: from.minusDays(2)])
        def dataset = stockDataset([symbol: TICKER, from: fromWithOffset, to: to, data: [entry0, entry1, entry2]])

        and: "stub returns stock market adjusted data"
        def adjustedDateToProbe = fromWithOffset.minusDays(1)
        def adjustedEntry = NasdaqDatasetDataEntryDtoProvider.dto([date: adjustedDateToProbe])
        def adjustedDataset = nasdaqDataset([datasetCode: TICKER, startDate: adjustedDateToProbe, endDate: adjustedDateToProbe, data: [adjustedEntry]])
        1 * stockMarketDataClient.getDataset(TICKER, adjustedDateToProbe, adjustedDateToProbe) >> adjustedDataset

        when:
        def result = datasetAdjusterForMarketClosedDays.adjust(dataset, from, period)

        then:
        result.symbol == TICKER
        result.from == adjustedDateToProbe
        result.to == to
        def adjustedEntryModels = entryFactory.toModels([adjustedEntry])
        result.data == [entry0, entry1, entry2, *adjustedEntryModels]
    }

    def "adjusts dataset when data is missing two offset entries out of three"() {
        given:
        def period = 3
        def from = LocalDate.of(2012, 1, 1)
        def to = LocalDate.of(2012, 1, 3)
        def fromWithOffset = from.minusDays(period)
        def entry0 = StockDatasetDataEntryProvider.model([date: from])
        def entry1 = StockDatasetDataEntryProvider.model([date: from.minusDays(1)])
        def dataset = stockDataset([symbol: TICKER, from: fromWithOffset, to: to, data: [entry0, entry1]])

        and: "stub returns stock market adjusted data on first call"
        def adjustedDateToProbe1 = fromWithOffset.minusDays(1)
        def adjustedEntry1 = NasdaqDatasetDataEntryDtoProvider.dto([date: adjustedDateToProbe1])
        def adjustedDataset1 = nasdaqDataset([datasetCode: TICKER, startDate: adjustedDateToProbe1, endDate: adjustedDateToProbe1, data: [adjustedEntry1]])
        1 * stockMarketDataClient.getDataset(TICKER, adjustedDateToProbe1, adjustedDateToProbe1) >> adjustedDataset1

        and: "stub returns stock market adjusted data on second call"
        def adjustedDateToProbe2 = fromWithOffset.minusDays(2)
        def adjustedEntry2 = NasdaqDatasetDataEntryDtoProvider.dto([date: adjustedDateToProbe2])
        def adjustedDataset2 = nasdaqDataset([datasetCode: TICKER, startDate: adjustedDateToProbe2, endDate: adjustedDateToProbe2, data: [adjustedEntry2]])
        1 * stockMarketDataClient.getDataset(TICKER, adjustedDateToProbe2, adjustedDateToProbe2) >> adjustedDataset2

        when:
        def result = datasetAdjusterForMarketClosedDays.adjust(dataset, from, period)

        then:
        result.symbol == TICKER
        result.from == adjustedDateToProbe2
        result.to == to
        def adjustedEntryModels = entryFactory.toModels([adjustedEntry1, adjustedEntry2])
        result.data == [entry0, entry1, *adjustedEntryModels]
    }

    def "adjusts dataset when data is missing three offset entries out of three"() {
        given:
        def period = 3
        def from = LocalDate.of(2012, 1, 1)
        def to = LocalDate.of(2012, 1, 3)
        def fromWithOffset = from.minusDays(period)
        def entry0 = StockDatasetDataEntryProvider.model([date: from])
        def dataset = stockDataset([symbol: TICKER, from: fromWithOffset, to: to, data: [entry0]])

        and: "stub returns stock market adjusted data on first call"
        def adjustedDateToProbe1 = fromWithOffset.minusDays(1)
        def adjustedEntry1 = NasdaqDatasetDataEntryDtoProvider.dto([date: adjustedDateToProbe1])
        def adjustedDataset1 = nasdaqDataset([datasetCode: TICKER, startDate: adjustedDateToProbe1, endDate: adjustedDateToProbe1, data: [adjustedEntry1]])
        1 * stockMarketDataClient.getDataset(TICKER, adjustedDateToProbe1, adjustedDateToProbe1) >> adjustedDataset1

        and: "stub returns stock market adjusted data on second call"
        def adjustedDateToProbe2 = fromWithOffset.minusDays(2)
        def adjustedEntry2 = NasdaqDatasetDataEntryDtoProvider.dto([date: adjustedDateToProbe2])
        def adjustedDataset2 = nasdaqDataset([datasetCode: TICKER, startDate: adjustedDateToProbe2, endDate: adjustedDateToProbe2, data: [adjustedEntry2]])
        1 * stockMarketDataClient.getDataset(TICKER, adjustedDateToProbe2, adjustedDateToProbe2) >> adjustedDataset2

        and: "stub returns stock market adjusted data on second call"
        def adjustedDateToProbe3 = fromWithOffset.minusDays(3)
        def adjustedEntry3 = NasdaqDatasetDataEntryDtoProvider.dto([date: adjustedDateToProbe3])
        def adjustedDataset3 = nasdaqDataset([datasetCode: TICKER, startDate: adjustedDateToProbe3, endDate: adjustedDateToProbe3, data: [adjustedEntry3]])
        1 * stockMarketDataClient.getDataset(TICKER, adjustedDateToProbe3, adjustedDateToProbe3) >> adjustedDataset3

        when:
        def result = datasetAdjusterForMarketClosedDays.adjust(dataset, from, period)

        then:
        result.symbol == TICKER
        result.from == adjustedDateToProbe3
        result.to == to
        def adjustedEntryModels = entryFactory.toModels([adjustedEntry1, adjustedEntry2, adjustedEntry3])
        result.data == [entry0, *adjustedEntryModels]
    }
}

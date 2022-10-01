package dev.skaringa.qupa.service.simplemovingaverage

import dev.skaringa.qupa.SpecBaseIT
import dev.skaringa.qupa.model.ChartType
import dev.skaringa.qupa.provider.DailySingleValueChartDataEntryProvider
import dev.skaringa.qupa.provider.NasdaqDatasetDataEntryDtoProvider
import dev.skaringa.qupa.service.StockMarketDataClient
import org.springframework.beans.factory.annotation.Autowired

import java.time.LocalDate

class SimpleMovingAverageServiceSpec extends SpecBaseIT {
    @Autowired
    private SimpleMovingAverageService simpleMovingAverageService
    @Autowired
    private StockMarketDataClient stockMarketDataClient

    def "returns correct chart for date"() {
        given:
        def period = 1
        def date = LocalDate.of(2012, 1, 1)

        and: "stub returns stock market data"
        def entry1 = NasdaqDatasetDataEntryDtoProvider.dto([date: date.minusDays(1), close: 1])
        def entry2 = NasdaqDatasetDataEntryDtoProvider.dto([date: date, close: 2])
        def dataset = nasdaqDataset([datasetCode: TICKER, startDate: date, endDate: date, data: [entry1, entry2]])
        1 * stockMarketDataClient.getDataset(TICKER, date.minusDays(period), date) >> dataset

        when:
        def result = simpleMovingAverageService.getChart(TICKER, date, date, period)

        then:
        result.ticker == TICKER
        result.from == date
        result.to == date
        result.type == ChartType.SMA
        result.period == period
        result.data == [DailySingleValueChartDataEntryProvider.model([date: date, value: 1])]
    }

    def "returns correct chart for date range"() {
        given:
        def period = 1
        def from = LocalDate.of(2012, 1, 1)
        def to = LocalDate.of(2012, 1, 2)

        and: "stub returns stock market data"
        def entry1 = NasdaqDatasetDataEntryDtoProvider.dto([date: from.minusDays(1), close: 1])
        def entry2 = NasdaqDatasetDataEntryDtoProvider.dto([date: from, close: 2])
        def entry3 = NasdaqDatasetDataEntryDtoProvider.dto([date: from.plusDays(1), close: 3])
        def dataset = nasdaqDataset([datasetCode: TICKER, startDate: from, endDate: to, data: [entry1, entry2, entry3]])
        1 * stockMarketDataClient.getDataset(TICKER, from.minusDays(period), to) >> dataset

        when:
        def result = simpleMovingAverageService.getChart(TICKER, from, to, period)

        then:
        result.ticker == TICKER
        result.from == from
        result.to == to
        result.type == ChartType.SMA
        result.period == period
        result.data == [
                DailySingleValueChartDataEntryProvider.model([date: from, value: 1]),
                DailySingleValueChartDataEntryProvider.model([date: from.plusDays(1), value: 2]),
        ]
    }

    def "returns correct chart for date when dataset adjustment needed"() {
        given:
        def period = 2
        def date = LocalDate.of(2012, 1, 1)

        and: "stub returns stock market data"
        def entry1 = NasdaqDatasetDataEntryDtoProvider.dto([date: date.minusDays(1), close: 1])
        def entry2 = NasdaqDatasetDataEntryDtoProvider.dto([date: date, close: 2])
        def dateWithOffset = date.minusDays(period)
        def dataset = nasdaqDataset([datasetCode: TICKER, startDate: dateWithOffset, endDate: date, data: [entry1, entry2]])
        1 * stockMarketDataClient.getDataset(TICKER, dateWithOffset, date) >> dataset

        and: "stub returns stock market adjusted data"
        def adjustedDate = dateWithOffset.minusDays(1)
        def entry0 = NasdaqDatasetDataEntryDtoProvider.dto([date: adjustedDate, close: 3])
        def adjustedDataset = nasdaqDataset([datasetCode: TICKER, startDate: adjustedDate, endDate: adjustedDate, data: [entry0]])
        1 * stockMarketDataClient.getDataset(TICKER, adjustedDate, adjustedDate) >> adjustedDataset

        when:
        def result = simpleMovingAverageService.getChart(TICKER, date, date, period)

        then:
        result.ticker == TICKER
        result.from == date
        result.to == date
        result.type == ChartType.SMA
        result.period == period
        result.data == [DailySingleValueChartDataEntryProvider.model([date: date, value: 2])]
    }

    def "returns correct chart for date range when dataset adjustment needed"() {
        given:
        def period = 2
        def from = LocalDate.of(2012, 1, 1)
        def to = LocalDate.of(2012, 1, 2)

        and: "stub returns stock market data"
        def entry1 = NasdaqDatasetDataEntryDtoProvider.dto([date: from.minusDays(1), close: 1])
        def entry2 = NasdaqDatasetDataEntryDtoProvider.dto([date: from, close: 2])
        def entry3 = NasdaqDatasetDataEntryDtoProvider.dto([date: from.plusDays(1), close: 3])
        def fromWithOffset = from.minusDays(period)
        def dataset = nasdaqDataset([datasetCode: TICKER, startDate: fromWithOffset, endDate: to, data: [entry1, entry2, entry3]])
        1 * stockMarketDataClient.getDataset(TICKER, from.minusDays(period), to) >> dataset

        and: "stub returns stock market adjusted data"
        def adjustedDate = fromWithOffset.minusDays(1)
        def entry0 = NasdaqDatasetDataEntryDtoProvider.dto([date: adjustedDate, close: 3])
        def adjustedDataset = nasdaqDataset([datasetCode: TICKER, startDate: adjustedDate, endDate: adjustedDate, data: [entry0]])
        1 * stockMarketDataClient.getDataset(TICKER, adjustedDate, adjustedDate) >> adjustedDataset

        when:
        def result = simpleMovingAverageService.getChart(TICKER, from, to, period)

        then:
        result.ticker == TICKER
        result.from == from
        result.to == to
        result.type == ChartType.SMA
        result.period == period
        result.data == [
                DailySingleValueChartDataEntryProvider.model([date: from, value: 2]),
                DailySingleValueChartDataEntryProvider.model([date: from.plusDays(1), value: 1.5]),
        ]
    }

    def "returns empty chart when stock dataset data is empty"() {
        given:
        def period = 2
        def from = LocalDate.of(2012, 1, 1)
        def to = LocalDate.of(2012, 1, 2)

        and: "stub called just once and returns stock market data"
        def fromWithOffset = from.minusDays(period)
        def dataset = nasdaqDataset([datasetCode: TICKER, startDate: fromWithOffset, endDate: to, data: []])
        1 * stockMarketDataClient.getDataset(_, _, _) >> dataset

        when:
        def result = simpleMovingAverageService.getChart(TICKER, from, to, period)

        then:
        result.ticker == TICKER
        result.from == from
        result.to == to
        result.type == ChartType.SMA
        result.period == period
        result.data == []
    }
}

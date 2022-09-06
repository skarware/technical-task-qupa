package dev.skaringa.qupa.service

import dev.skaringa.qupa.SpecBase
import dev.skaringa.qupa.api.ErrorCode
import dev.skaringa.qupa.exception.SystemException
import dev.skaringa.qupa.provider.DailySingleValueChartDataEntryProvider
import dev.skaringa.qupa.provider.StockDatasetDataEntryProvider

import java.time.LocalDate

class SimpleMovingAverageCalculatorSpec extends SpecBase {
    private def calculator = new SimpleMovingAverageCalculator()

    def "returns empty list when no data entries given"() {
        given: "from date and period"
        def from = LocalDate.of(2022, 1, 1)
        def period = 3

        and: "stock dataset"
        def stockDataset = stockDataset([
                from: from,
                to  : from.plusDays(2),
                data: [],
        ])

        when: "calculate is called"
        def result = calculator.calculate(stockDataset, from, period)

        then: "result is correct"
        result == []
    }

    def "returns correct SMA result for period of 1-day"() {
        given: "from date and period"
        def from = LocalDate.of(2022, 1, 1)
        def period = 1

        and: "stock dataset"
        def entry1 = StockDatasetDataEntryProvider.model([date: from.minusDays(1), close: 1])
        def entry2 = StockDatasetDataEntryProvider.model([date: from, close: 2])
        def entry3 = StockDatasetDataEntryProvider.model([date: from.plusDays(1), close: 3])
        def entry4 = StockDatasetDataEntryProvider.model([date: from.plusDays(2), close: 4])
        def stockDataset = stockDataset([
                from: from,
                to  : from,
                data: [entry1, entry2, entry3, entry4],
        ])

        when: "calculate is called"
        def result = calculator.calculate(stockDataset, from, period)

        then: "result is correct"
        result == [
                DailySingleValueChartDataEntryProvider.model([date: from, value: 1]),
                DailySingleValueChartDataEntryProvider.model([date: from.plusDays(1), value: 2]),
                DailySingleValueChartDataEntryProvider.model([date: from.plusDays(2), value: 3]),
        ]
    }

    def "returns correct SMA result for period of 2-day"() {
        given: "from date and period"
        def from = LocalDate.of(2022, 1, 1)
        def period = 2

        and: "stock dataset"
        def entry1 = StockDatasetDataEntryProvider.model([date: from.minusDays(2), close: 1])
        def entry2 = StockDatasetDataEntryProvider.model([date: from.minusDays(1), close: 2])
        def entry3 = StockDatasetDataEntryProvider.model([date: from, close: 3])
        def entry4 = StockDatasetDataEntryProvider.model([date: from.plusDays(1), close: 4])
        def entry5 = StockDatasetDataEntryProvider.model([date: from.plusDays(2), close: 5])
        def stockDataset = stockDataset([
                from: from,
                to  : from,
                data: [entry1, entry2, entry3, entry4, entry5],
        ])

        when: "calculate is called"
        def result = calculator.calculate(stockDataset, from, period)

        then: "result is correct"
        result == [
                DailySingleValueChartDataEntryProvider.model([date: from, value: 1.5]),
                DailySingleValueChartDataEntryProvider.model([date: from.plusDays(1), value: 2.5]),
                DailySingleValueChartDataEntryProvider.model([date: from.plusDays(2), value: 3.5]),
        ]
    }

    def "returns correct SMA result for period of 3-day"() {
        given: "from date and period"
        def from = LocalDate.of(2022, 1, 1)
        def period = 3

        and: "stock dataset"
        def entry1 = StockDatasetDataEntryProvider.model([date: from.minusDays(3), close: 1])
        def entry2 = StockDatasetDataEntryProvider.model([date: from.minusDays(2), close: 2])
        def entry3 = StockDatasetDataEntryProvider.model([date: from.minusDays(1), close: 3])
        def entry4 = StockDatasetDataEntryProvider.model([date: from, close: 4])
        def entry5 = StockDatasetDataEntryProvider.model([date: from.plusDays(1), close: 5])
        def entry6 = StockDatasetDataEntryProvider.model([date: from.plusDays(2), close: 6])
        def stockDataset = stockDataset([
                from: from,
                to  : from,
                data: [entry1, entry2, entry3, entry4, entry5, entry6],
        ])

        when: "calculate is called"
        def result = calculator.calculate(stockDataset, from, period)

        then: "result is correct"
        result == [
                DailySingleValueChartDataEntryProvider.model([date: from, value: 2]),
                DailySingleValueChartDataEntryProvider.model([date: from.plusDays(1), value: 3]),
                DailySingleValueChartDataEntryProvider.model([date: from.plusDays(2), value: 4]),
        ]
    }

    def "returns correct SMA result for period of 4-day"() {
        given: "from date and period"
        def from = LocalDate.of(2022, 1, 1)
        def period = 4

        and: "stock dataset"
        def entry1 = StockDatasetDataEntryProvider.model([date: from.minusDays(4), close: 30.5])
        def entry2 = StockDatasetDataEntryProvider.model([date: from.minusDays(3), close: 30.6])
        def entry3 = StockDatasetDataEntryProvider.model([date: from.minusDays(2), close: 30.35])
        def entry4 = StockDatasetDataEntryProvider.model([date: from.minusDays(1), close: 29.7])
        def entry5 = StockDatasetDataEntryProvider.model([date: from, close: 28.1])
        def entry6 = StockDatasetDataEntryProvider.model([date: from.plusDays(1), close: 29.25])
        def entry7 = StockDatasetDataEntryProvider.model([date: from.plusDays(2), close: 30.25])
        def stockDataset = stockDataset([
                from: from,
                to  : from,
                data: [entry1, entry2, entry3, entry4, entry5, entry6, entry7],
        ])

        when: "calculate is called"
        def result = calculator.calculate(stockDataset, from, period)

        then: "result is correct"
        result == [
                DailySingleValueChartDataEntryProvider.model([date: from, value: 30.2875]),
                DailySingleValueChartDataEntryProvider.model([date: from.plusDays(1), value: 29.6875]),
                DailySingleValueChartDataEntryProvider.model([date: from.plusDays(2), value: 29.35]),
        ]
    }

    def "returns correct SMA result for period of 5-day"() {
        given: "from date and period"
        def from = LocalDate.of(2022, 1, 1)
        def period = 5

        and: "stock dataset"
        def entry1 = StockDatasetDataEntryProvider.model([date: from.minusDays(5), close: 30.5])
        def entry2 = StockDatasetDataEntryProvider.model([date: from.minusDays(4), close: 30.6])
        def entry3 = StockDatasetDataEntryProvider.model([date: from.minusDays(3), close: 30.35])
        def entry4 = StockDatasetDataEntryProvider.model([date: from.minusDays(2), close: 29.7])
        def entry5 = StockDatasetDataEntryProvider.model([date: from.minusDays(1), close: 28.1])
        def entry6 = StockDatasetDataEntryProvider.model([date: from, close: 29.25])
        def entry7 = StockDatasetDataEntryProvider.model([date: from.plusDays(1), close: 30.25])
        def stockDataset = stockDataset([
                from: from,
                to  : from,
                data: [entry1, entry2, entry3, entry4, entry5, entry6, entry7],
        ])

        when: "calculate is called"
        def result = calculator.calculate(stockDataset, from, period)

        then: "result is correct"
        result == [
                DailySingleValueChartDataEntryProvider.model([date: from, value: 29.85]),
                DailySingleValueChartDataEntryProvider.model([date: from.plusDays(1), value: 29.60]),
        ]
    }

    def "returns correct SMA result when data entry dates are not consecutive"() {
        given: "from date and period"
        def from = LocalDate.of(2022, 1, 1)
        def period = 3

        and: "stock dataset"
        def entry1 = StockDatasetDataEntryProvider.model([date: from.minusDays(6), close: 1])
        def entry2 = StockDatasetDataEntryProvider.model([date: from.minusDays(4), close: 2])
        def entry3 = StockDatasetDataEntryProvider.model([date: from.minusDays(2), close: 3])
        def entry4 = StockDatasetDataEntryProvider.model([date: from, close: 4])
        def entry5 = StockDatasetDataEntryProvider.model([date: from.plusDays(2), close: 5])
        def entry6 = StockDatasetDataEntryProvider.model([date: from.plusDays(4), close: 6])
        def stockDataset = stockDataset([
                from: from,
                to  : from,
                data: [entry1, entry2, entry3, entry4, entry5, entry6],
        ])

        when: "calculate is called"
        def result = calculator.calculate(stockDataset, from, period)

        then: "result is correct"
        result == [
                DailySingleValueChartDataEntryProvider.model([date: from, value: 2]),
                DailySingleValueChartDataEntryProvider.model([date: from.plusDays(2), value: 3]),
                DailySingleValueChartDataEntryProvider.model([date: from.plusDays(4), value: 4]),
        ]
    }

    def "throws exception when data entries offset not enough for given period"() {
        given: "from date and period"
        def from = LocalDate.of(2022, 1, 1)
        def period = 3

        and: "stock dataset"
        def entry1 = StockDatasetDataEntryProvider.model([date: from.minusDays(2), close: 1])
        def entry2 = StockDatasetDataEntryProvider.model([date: from.minusDays(1), close: 2])
        def entry3 = StockDatasetDataEntryProvider.model([date: from, close: 3.3])
        def entry4 = StockDatasetDataEntryProvider.model([date: from.plusDays(1), close: 4])
        def entry5 = StockDatasetDataEntryProvider.model([date: from.plusDays(2), close: 5])
        def stockDataset = stockDataset([
                from: from,
                to  : from,
                data: [entry1, entry2, entry3, entry4, entry5],
        ])

        when: "calculate is called"
        calculator.calculate(stockDataset, from, period)

        then: "Exception is thrown"
        def ex = thrown(SystemException)
        ex.code == ErrorCode.UNEXPECTED
        ex.message == "Not enough stock data entries to calculate SMA"
    }

    def "throws exception when given period is zero"() {
        given: "from date and period"
        def from = LocalDate.of(2022, 1, 1)
        def period = 0

        and: "stock dataset"
        def entry1 = StockDatasetDataEntryProvider.model([date: from.minusDays(2), close: 1])
        def entry2 = StockDatasetDataEntryProvider.model([date: from.minusDays(1), close: 2])
        def entry3 = StockDatasetDataEntryProvider.model([date: from, close: 3.3])
        def entry4 = StockDatasetDataEntryProvider.model([date: from.plusDays(1), close: 4])
        def entry5 = StockDatasetDataEntryProvider.model([date: from.plusDays(2), close: 5])
        def stockDataset = stockDataset([
                from: from,
                to  : from,
                data: [entry1, entry2, entry3, entry4, entry5],
        ])

        when: "calculate is called"
        calculator.calculate(stockDataset, from, period)

        then: "Exception is thrown"
        def ex = thrown(IllegalArgumentException)
        ex.message == "SMA period value must be greater than zero"
    }

    def "throws exception when given period is less than zero"() {
        given: "from date and period"
        def from = LocalDate.of(2022, 1, 1)
        def period = -1

        and: "stock dataset"
        def entry1 = StockDatasetDataEntryProvider.model([date: from.minusDays(2), close: 1])
        def entry2 = StockDatasetDataEntryProvider.model([date: from.minusDays(1), close: 2])
        def entry3 = StockDatasetDataEntryProvider.model([date: from, close: 3.3])
        def entry4 = StockDatasetDataEntryProvider.model([date: from.plusDays(1), close: 4])
        def entry5 = StockDatasetDataEntryProvider.model([date: from.plusDays(2), close: 5])
        def stockDataset = stockDataset([
                from: from,
                to  : from,
                data: [entry1, entry2, entry3, entry4, entry5],
        ])

        when: "calculate is called"
        calculator.calculate(stockDataset, from, period)

        then: "Exception is thrown"
        def ex = thrown(IllegalArgumentException)
        ex.message == "SMA period value must be greater than zero"
    }
}

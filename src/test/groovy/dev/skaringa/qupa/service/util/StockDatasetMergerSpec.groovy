package dev.skaringa.qupa.service.util

import dev.skaringa.qupa.SpecBase
import dev.skaringa.qupa.provider.StockDatasetDataEntryProvider

import java.time.LocalDate

class StockDatasetMergerSpec extends SpecBase {
    def "merges two datasets correctly when data entries consecutive"() {
        given: "base date"
        def date = LocalDate.of(2022, 1, 1)

        and: "first dataset"
        def entry1 = StockDatasetDataEntryProvider.model([date: date.plusDays(1)])
        def entry2 = StockDatasetDataEntryProvider.model([date: date.plusDays(2)])
        def dataset1 = stockDataset([
                from: date,
                to  : date.plusDays(2),
                data: [entry1, entry2],
        ])

        and: "second dataset"
        def entry3 = StockDatasetDataEntryProvider.model([date: date.plusDays(3)])
        def entry4 = StockDatasetDataEntryProvider.model([date: date.plusDays(4)])
        def dataset2 = stockDataset([
                from: date.plusDays(3),
                to  : date.plusDays(4),
                data: [entry3, entry4]
        ])

        when:
        def result = StockDatasetMerger.merge(dataset1, dataset2)

        then:
        def expectedFrom = date
        def expectedTo = date.plusDays(4)
        result.symbol == dataset1.symbol && result.symbol == dataset2.symbol
        result.from == expectedFrom
        result.to == expectedTo
        result.data.size() == 4
        result.data == [entry1, entry2, entry3, entry4]
    }

    def "merges two datasets correctly when data entries overlap"() {
        given: "base date"
        def date = LocalDate.of(2022, 1, 1)

        and: "first dataset"
        def entry1 = StockDatasetDataEntryProvider.model([date: date.plusDays(1)])
        def entry2 = StockDatasetDataEntryProvider.model([date: date.plusDays(2)])
        def dataset1 = stockDataset([
                from: date,
                to  : date.plusDays(2),
                data: [entry1, entry2],
        ])

        and: "second dataset with overlapping entry"
        def entry3 = entry2
        def entry4 = StockDatasetDataEntryProvider.model([date: date.plusDays(3)])
        def dataset2 = stockDataset([
                from: date.plusDays(2),
                to  : date.plusDays(3),
                data: [entry3, entry4]
        ])

        when:
        def result = StockDatasetMerger.merge(dataset1, dataset2)

        then:
        def expectedFrom = date
        def expectedTo = date.plusDays(3)
        result.symbol == dataset1.symbol && result.symbol == dataset2.symbol
        result.from == expectedFrom
        result.to == expectedTo
        result.data.size() == 3
        result.data == [entry1, entry2, entry4]
    }

    def "merges three datasets correctly when data entries consecutive"() {
        given: "base date"
        def date = LocalDate.of(2022, 1, 1)

        and: "first dataset"
        def entry1 = StockDatasetDataEntryProvider.model([date: date.plusDays(1)])
        def entry2 = StockDatasetDataEntryProvider.model([date: date.plusDays(2)])
        def dataset1 = stockDataset([
                from: date,
                to  : date.plusDays(2),
                data: [entry1, entry2],
        ])

        and: "second dataset"
        def entry3 = StockDatasetDataEntryProvider.model([date: date.plusDays(3)])
        def entry4 = StockDatasetDataEntryProvider.model([date: date.plusDays(4)])
        def dataset2 = stockDataset([
                from: date.plusDays(3),
                to  : date.plusDays(4),
                data: [entry3, entry4]
        ])

        and: "third dataset"
        def entry5 = StockDatasetDataEntryProvider.model([date: date.plusDays(5)])
        def entry6 = StockDatasetDataEntryProvider.model([date: date.plusDays(7)])
        def dataset3 = stockDataset([
                from: date.plusDays(5),
                to  : date.plusDays(9),
                data: [entry5, entry6]
        ])

        when:
        def result = StockDatasetMerger.merge(dataset1, dataset2, dataset3)

        then:
        def expectedFrom = date
        def expectedTo = date.plusDays(9)
        result.symbol == dataset1.symbol && result.symbol == dataset2.symbol
        result.from == expectedFrom
        result.to == expectedTo
        result.data.size() == 6
        result.data == [entry1, entry2, entry3, entry4, entry5, entry6]
    }

    def "merges three datasets correctly when data entries overlap"() {
        given: "base date"
        def date = LocalDate.of(2022, 1, 1)

        and: "first dataset"
        def entry1 = StockDatasetDataEntryProvider.model([date: date.plusDays(1)])
        def entry2 = StockDatasetDataEntryProvider.model([date: date.plusDays(2)])
        def dataset1 = stockDataset([
                from: date,
                to  : date.plusDays(2),
                data: [entry1, entry2],
        ])

        and: "second dataset with overlapping entry"
        def entry3 = entry2
        def entry4 = StockDatasetDataEntryProvider.model([date: date.plusDays(3)])
        def dataset2 = stockDataset([
                from: date.plusDays(2),
                to  : date.plusDays(3),
                data: [entry3, entry4]
        ])

        and: "second dataset with overlapping entry"
        def entry5 = entry2
        def entry6 = entry4
        def entry7 = StockDatasetDataEntryProvider.model([date: date.plusDays(5)])
        def entry8 = StockDatasetDataEntryProvider.model([date: date.plusDays(7)])
        def dataset3 = stockDataset([
                from: date.plusDays(2),
                to  : date.plusDays(9),
                data: [entry5, entry6, entry7, entry8]
        ])

        when:
        def result = StockDatasetMerger.merge(dataset1, dataset2, dataset3)

        then:
        def expectedFrom = date
        def expectedTo = date.plusDays(9)
        result.symbol == dataset1.symbol && result.symbol == dataset2.symbol
        result.from == expectedFrom
        result.to == expectedTo
        result.data.size() == 5
        result.data == [entry1, entry2, entry4, entry7, entry8]
    }
}

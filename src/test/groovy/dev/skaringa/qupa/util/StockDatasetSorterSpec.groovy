package dev.skaringa.qupa.util

import dev.skaringa.qupa.SpecBase
import dev.skaringa.qupa.provider.StockDatasetDataEntryProvider

import java.time.LocalDate

class StockDatasetSorterSpec extends SpecBase {
    def "sorts data entries by date"() {
        given: "base date"
        def date = LocalDate.of(2022, 1, 1)

        and: "dataset"
        def entry1 = StockDatasetDataEntryProvider.model([date: date.plusDays(1)])
        def entry2 = StockDatasetDataEntryProvider.model([date: date.plusDays(2)])
        def entry3 = StockDatasetDataEntryProvider.model([date: date.plusDays(3)])
        def entry4 = StockDatasetDataEntryProvider.model([date: date.plusDays(4)])
        def entry5 = StockDatasetDataEntryProvider.model([date: date.plusDays(5)])
        def entry6 = StockDatasetDataEntryProvider.model([date: date.plusDays(6)])
        def dataset = stockDataset([
                from: date,
                to  : date.plusDays(6),
                data: [entry5, entry4, entry2, entry6, entry1, entry3]
        ])

        when:
        def result = StockDatasetSorter.sortDataEntriesByDate(dataset)

        then: "data entries are sorted"
        result.data == [entry1, entry2, entry3, entry4, entry5, entry6]
    }

    def "sorts data entries by date reverse"() {
        given: "base date"
        def date = LocalDate.of(2022, 1, 1)

        and: "dataset"
        def entry1 = StockDatasetDataEntryProvider.model([date: date.plusDays(1)])
        def entry2 = StockDatasetDataEntryProvider.model([date: date.plusDays(2)])
        def entry3 = StockDatasetDataEntryProvider.model([date: date.plusDays(3)])
        def entry4 = StockDatasetDataEntryProvider.model([date: date.plusDays(4)])
        def entry5 = StockDatasetDataEntryProvider.model([date: date.plusDays(5)])
        def entry6 = StockDatasetDataEntryProvider.model([date: date.plusDays(6)])
        def dataset = stockDataset([
                from: date,
                to  : date.plusDays(6),
                data: [entry5, entry4, entry2, entry6, entry1, entry3]
        ])

        when:
        def result = StockDatasetSorter.sortDataEntriesByDateReverse(dataset)

        then: "data entries are sorted"
        result.data == [entry1, entry2, entry3, entry4, entry5, entry6].reverse()
    }
}

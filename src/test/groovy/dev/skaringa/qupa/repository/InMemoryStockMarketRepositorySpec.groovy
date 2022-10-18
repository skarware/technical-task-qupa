package dev.skaringa.qupa.repository

import dev.skaringa.qupa.SpecBase
import dev.skaringa.qupa.provider.StockDatasetDataEntryProvider

import java.time.LocalDate

class InMemoryStockMarketRepositorySpec extends SpecBase {
    private def inMemoryStockMarketDataRepository = new InMemoryStockMarketRepository()

    def "findAll returns all stored datasets"() {
        given:
        def dataset1 = stockDataset([symbol: UUID.randomUUID().toString()])
        def dataset2 = stockDataset([symbol: UUID.randomUUID().toString()])
        def dataset3 = stockDataset([symbol: UUID.randomUUID().toString()])

        when:
        inMemoryStockMarketDataRepository.tickerToStockDatasets.putAll(
                Map.of(
                        dataset1.getSymbol(), dataset1,
                        dataset2.getSymbol(), dataset2,
                        dataset3.getSymbol(), dataset3
                ))

        then:
        inMemoryStockMarketDataRepository.findAll() == [dataset1, dataset2, dataset3] as Set
    }

    def "save returns saved dataset"() {
        given:
        def dataset = stockDataset()

        when:
        def result = inMemoryStockMarketDataRepository.save(dataset)

        then:
        result == dataset
    }

    def "save stores dataset"() {
        given:
        def dataset = stockDataset()

        when:
        inMemoryStockMarketDataRepository.save(dataset)

        then:
        inMemoryStockMarketDataRepository.findAll() == [dataset] as Set
    }

    def "save stores datasets"() {
        given:
        def dataset1 = stockDataset([symbol: UUID.randomUUID().toString()])
        def dataset2 = stockDataset([symbol: UUID.randomUUID().toString()])
        def dataset3 = stockDataset([symbol: UUID.randomUUID().toString()])

        when:
        inMemoryStockMarketDataRepository.save(dataset1)
        inMemoryStockMarketDataRepository.save(dataset2)
        inMemoryStockMarketDataRepository.save(dataset3)

        then:
        inMemoryStockMarketDataRepository.findAll() == [dataset1, dataset2, dataset3] as Set
    }

    def "save merges dataset with already stored and sorts entries by date"() {
        given: "dataset is stored"
        def entry1 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 1)])
        def entry2 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 2)])
        def entry3 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 3)])
        def dataset = stockDataset([
                from: LocalDate.of(2022, 1, 1),
                to  : LocalDate.of(2022, 1, 3),
                data: [entry3, entry1, entry2]])
        inMemoryStockMarketDataRepository.save(dataset)

        and: "dataset update"
        def entry4 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 4)])
        def entry5 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 5)])
        def entry6 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 6)])
        def newDataset = stockDataset([
                from: LocalDate.of(2022, 1, 4),
                to  : LocalDate.of(2022, 1, 6),
                data: [entry5, entry6, entry4]])

        when: "dataset update saved"
        inMemoryStockMarketDataRepository.save(newDataset)

        then: "dataset data is merged"
        def expectedDataset = stockDataset([
                from: LocalDate.of(2022, 1, 1),
                to  : LocalDate.of(2022, 1, 6),
                data: [entry1, entry2, entry3, entry4, entry5, entry6]])
        inMemoryStockMarketDataRepository.findAll() == [expectedDataset] as Set
    }

    def "save merges and overrides dataset with already stored"() {
        given: "dataset is stored"
        def entry1 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 1)])
        def entry2 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 2), volume: 1L])
        def entry3 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 3), volume: 1L])
        def dataset = stockDataset([
                from: LocalDate.of(2022, 1, 1),
                to  : LocalDate.of(2022, 1, 3),
                data: [entry3, entry1, entry2]])
        inMemoryStockMarketDataRepository.save(dataset)

        and: "dataset update"
        def entry4 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 2), volume: 100L])
        def entry5 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 3), volume: 100L])
        def entry6 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 4)])
        def newDataset = stockDataset([
                from: LocalDate.of(2022, 1, 2),
                to  : LocalDate.of(2022, 1, 4),
                data: [entry5, entry6, entry4]])

        when: "dataset update saved"
        inMemoryStockMarketDataRepository.save(newDataset)

        then: "dataset data is merged and overridden"
        def expectedDataset = stockDataset([
                from: LocalDate.of(2022, 1, 1),
                to  : LocalDate.of(2022, 1, 4),
                data: [entry1, entry4, entry5, entry6]])
        inMemoryStockMarketDataRepository.findAll() == [expectedDataset] as Set
    }

    def "save returns saved and merged dataset update with already stored"() {
        given: "dataset is stored"
        def entry1 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 1)])
        def entry2 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 2)])
        def entry3 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 3)])
        def dataset = stockDataset([
                from: LocalDate.of(2022, 1, 1),
                to  : LocalDate.of(2022, 1, 3),
                data: [entry1, entry2, entry3]])
        inMemoryStockMarketDataRepository.save(dataset)

        and: "dataset update"
        def entry4 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 4)])
        def entry5 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 5)])
        def entry6 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 6)])
        def newDataset = stockDataset([
                from: LocalDate.of(2022, 1, 4),
                to  : LocalDate.of(2022, 1, 6),
                data: [entry4, entry5, entry6]])

        when: "dataset update saved"
        def result = inMemoryStockMarketDataRepository.save(newDataset)

        then: "result is merged dataset"
        def expectedDataset = stockDataset([
                from: LocalDate.of(2022, 1, 1),
                to  : LocalDate.of(2022, 1, 6),
                data: [entry1, entry2, entry3, entry4, entry5, entry6]])
        result == expectedDataset

    }

    def "findByTicker returns empty optional when no dataset stored"() {
        given:
        def ticker = "NON_EXISTING_TICKER"

        when:
        def result = inMemoryStockMarketDataRepository.findByTicker(ticker)

        then:
        result == Optional.empty()
    }

    def "findByTicker returns empty optional when no dataset by given ticker stored"() {
        given:
        def dataset = stockDataset()
        inMemoryStockMarketDataRepository.save(dataset)
        def ticker = "NON_EXISTING_TICKER"

        when:
        def result = inMemoryStockMarketDataRepository.findByTicker(ticker)

        then:
        result == Optional.empty()
    }

    def "findByTicker returns stored dataset"() {
        given:
        def dataset = stockDataset()
        inMemoryStockMarketDataRepository.save(dataset)

        when:
        def result = inMemoryStockMarketDataRepository.findByTicker(dataset.symbol)

        then:
        result.isPresent()
        result.get() == dataset
    }

    def "findByTicker returns correct dataset when multiple datasets stored"() {
        given:
        def dataset1 = stockDataset([symbol: UUID.randomUUID().toString()])
        def dataset2 = stockDataset([symbol: UUID.randomUUID().toString()])
        def dataset3 = stockDataset([symbol: UUID.randomUUID().toString()])
        inMemoryStockMarketDataRepository.save(dataset1)
        inMemoryStockMarketDataRepository.save(dataset2)
        inMemoryStockMarketDataRepository.save(dataset3)

        when:
        def result = inMemoryStockMarketDataRepository.findByTicker(dataset2.symbol)

        then:
        result.isPresent()
        result.get() == dataset2
    }

    def "findByTickerAndDate returns empty optional when no dataset by given ticker stored"() {
        given: "dataset is stored"
        def dataset = stockDataset()
        inMemoryStockMarketDataRepository.save(dataset)
        def ticker = "NON_EXISTING_TICKER"
        def date = LocalDate.of(2012, 1, 1)

        when:
        def result = inMemoryStockMarketDataRepository.findByTickerAndDate(ticker, date)

        then:
        result == Optional.empty()
    }


    def "findByTickerAndDate returns empty optional when no dataset entry by given date stored"() {
        given: "dataset is stored"
        def entry1 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 1)])
        def entry2 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 2)])
        def entry3 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 3)])
        def dataset = stockDataset([
                from: LocalDate.of(2022, 1, 1),
                to  : LocalDate.of(2022, 1, 3),
                data: [entry1, entry2, entry3]])
        inMemoryStockMarketDataRepository.save(dataset)

        and: "date for dataset entry to find"
        def date = LocalDate.of(2012, 1, 1)


        when:
        def result = inMemoryStockMarketDataRepository.findByTickerAndDate(dataset.symbol, date)

        then:
        result == Optional.empty()
    }

    def "findByTickerAndDate returns stored and filtered dataset with single entry"() {
        given: "dataset is stored"
        def entry1 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 1)])
        def entry2 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 2)])
        def entry3 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 3)])
        def dataset = stockDataset([
                from: LocalDate.of(2022, 1, 1),
                to  : LocalDate.of(2022, 1, 3),
                data: [entry1, entry2, entry3]])
        inMemoryStockMarketDataRepository.save(dataset)

        and: "date for dataset entry to find"
        def date = entry2.date

        when:
        def result = inMemoryStockMarketDataRepository.findByTickerAndDate(dataset.symbol, date)

        then:
        result.isPresent()
        def resultDataset = result.get()
        resultDataset.symbol == dataset.symbol
        resultDataset.from == date
        resultDataset.to == date
        resultDataset.data == [entry2]
    }


    def "findByTickerAndDateRange returns empty optional when no dataset by given ticker stored"() {
        given: "dataset is stored"
        def dataset = stockDataset()
        inMemoryStockMarketDataRepository.save(dataset)
        def ticker = "NON_EXISTING_TICKER"
        def from = LocalDate.of(2012, 1, 1)
        def to = LocalDate.of(2012, 1, 3)

        when:
        def result = inMemoryStockMarketDataRepository.findByTickerAndDateRange(ticker, from, to)

        then:
        result == Optional.empty()
    }


    def "findByTickerAndDateRange returns empty optional when no dataset entry by given date range stored"() {
        given: "dataset is stored"
        def entry1 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 1)])
        def entry2 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 2)])
        def entry3 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 3)])
        def dataset = stockDataset([
                from: LocalDate.of(2022, 1, 1),
                to  : LocalDate.of(2022, 1, 3),
                data: [entry1, entry2, entry3]])
        inMemoryStockMarketDataRepository.save(dataset)

        and: "date range for dataset entries to find"
        def from = LocalDate.of(2012, 1, 1)
        def to = LocalDate.of(2012, 1, 3)


        when:
        def result = inMemoryStockMarketDataRepository.findByTickerAndDateRange(dataset.symbol, from, to)

        then:
        result == Optional.empty()
    }

    def "findByTickerAndDateRange returns stored and filtered dataset with multiple entries"() {
        given: "dataset is stored"
        def entry1 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 1)])
        def entry2 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 2)])
        def entry3 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 3)])
        def entry4 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 4)])
        def entry5 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 5)])
        def entry6 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 6)])
        def dataset = stockDataset([
                from: LocalDate.of(2022, 1, 1),
                to  : LocalDate.of(2022, 1, 6),
                data: [entry1, entry2, entry3, entry4, entry5, entry6]])
        inMemoryStockMarketDataRepository.save(dataset)

        and: "date range for dataset entries to find"
        def from = entry3.date
        def to = entry5.date

        when:
        def result = inMemoryStockMarketDataRepository.findByTickerAndDateRange(dataset.symbol, from, to)

        then:
        result.isPresent()
        def resultDataset = result.get()
        resultDataset.symbol == dataset.symbol
        resultDataset.from == from
        resultDataset.to == to
        resultDataset.data == [entry3, entry4, entry5]
    }
}
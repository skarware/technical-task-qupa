package dev.skaringa.qupa

import dev.skaringa.qupa.provider.NasdaqDatasetDataEntryDtoProvider
import dev.skaringa.qupa.provider.NasdaqDatasetDtoProvider
import dev.skaringa.qupa.provider.StockDatasetDataEntryProvider
import dev.skaringa.qupa.provider.StockDatasetProvider
import spock.lang.Specification

import java.time.LocalDate

abstract class SpecBase extends Specification {
    protected static final def TICKER = "ticker"

    private static final def RANDOM = new Random()

    protected static def randomId() {
        return RANDOM.nextLong()
    }

    protected static def nasdaqDataset(Map<String, Object> props = [:]) {
        return NasdaqDatasetDtoProvider.dto([
                id       : randomId(),
                startDate: LocalDate.of(2022, 1, 1),
                endDate  : LocalDate.of(2022, 1, 3),
                data     : nasdaqDatasetDataEntries(),
        ] + props)
    }

    protected static def nasdaqDatasetDataEntries() {
        def entry1 = NasdaqDatasetDataEntryDtoProvider.dto([date: LocalDate.of(2022, 1, 1)])
        def entry2 = NasdaqDatasetDataEntryDtoProvider.dto([date: LocalDate.of(2022, 1, 2)])
        def entry3 = NasdaqDatasetDataEntryDtoProvider.dto([date: LocalDate.of(2022, 1, 3)])
        return [entry1, entry2, entry3]
    }

    protected static def stockDataset(Map<String, Object> props = [:]) {
        return StockDatasetProvider.model([
                from: LocalDate.of(2022, 1, 1),
                to  : LocalDate.of(2022, 1, 3),
                data: stockDatasetDataEntries(),
        ] + props)
    }

    protected static def stockDatasetDataEntries() {
        def entry1 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 1)])
        def entry2 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 2)])
        def entry3 = StockDatasetDataEntryProvider.model([date: LocalDate.of(2022, 1, 3)])
        return [entry1, entry2, entry3]
    }
}

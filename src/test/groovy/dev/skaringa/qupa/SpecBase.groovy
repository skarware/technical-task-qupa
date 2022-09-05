package dev.skaringa.qupa

import dev.skaringa.qupa.provider.NasdaqDatasetDataEntryDtoProvider
import dev.skaringa.qupa.provider.NasdaqDatasetDtoProvider
import dev.skaringa.qupa.provider.StockDatasetDataEntryDtoProvider
import dev.skaringa.qupa.provider.StockDatasetDtoProvider
import spock.lang.Specification

import java.time.LocalDate

abstract class SpecBase extends Specification {
    protected static final def TICKER = "ticker"

    private static final def RANDOM = new Random()

    protected static def randomId() {
        return RANDOM.nextLong()
    }

    protected static def nasdaqDataset() {
        return NasdaqDatasetDtoProvider.dto([
                id       : randomId(),
                startDate: LocalDate.of(2022, 1, 1),
                endDate  : LocalDate.of(2022, 1, 3),
                data     : nasdaqDatasetDataEntries(),
        ])
    }

    protected static def nasdaqDatasetDataEntries() {
        def entry1 = NasdaqDatasetDataEntryDtoProvider.dto([date: LocalDate.of(2022, 1, 1)])
        def entry2 = NasdaqDatasetDataEntryDtoProvider.dto([date: LocalDate.of(2022, 1, 2)])
        def entry3 = NasdaqDatasetDataEntryDtoProvider.dto([date: LocalDate.of(2022, 1, 3)])
        return [entry1, entry2, entry3]
    }

    protected static def stockDataset() {
        return StockDatasetDtoProvider.dto([
                id  : randomId(),
                from: LocalDate.of(2022, 1, 1),
                to  : LocalDate.of(2022, 1, 3),
                data: stockDatasetDataEntries(),
        ])
    }

    protected static def stockDatasetDataEntries() {
        def entry1 = StockDatasetDataEntryDtoProvider.dto([date: LocalDate.of(2022, 1, 1)])
        def entry2 = StockDatasetDataEntryDtoProvider.dto([date: LocalDate.of(2022, 1, 2)])
        def entry3 = StockDatasetDataEntryDtoProvider.dto([date: LocalDate.of(2022, 1, 3)])
        return [entry1, entry2, entry3]
    }
}

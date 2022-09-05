package dev.skaringa.qupa.factory

import dev.skaringa.qupa.provider.NasdaqDatasetDataEntryDtoProvider
import spock.lang.Specification

import java.time.LocalDate

class StockDatasetDataEntryFactorySpec extends Specification {
    private def factory = new StockDatasetDataEntryFactory()

    def "creates stock data entry models correctly"() {
        given: "valid dtos"
        def entryDto1 = NasdaqDatasetDataEntryDtoProvider.dto([date: LocalDate.of(2022, 3, 4)])
        def entryDto2 = NasdaqDatasetDataEntryDtoProvider.dto([date: LocalDate.of(2023, 4, 5)])

        when: "toModels is called"
        def models = factory.toModels([entryDto1, entryDto2])

        then: "models have correct values"
        models.size() == 2
        def entryModel1 = models[0]
        entryModel1.date == entryDto1.date
        entryModel1.open == entryDto1.open
        entryModel1.high == entryDto1.high
        entryModel1.low == entryDto1.low
        entryModel1.close == entryDto1.close
        def entryModel2 = models[1]
        entryModel2.date == entryDto2.date
        entryModel2.open == entryDto2.open
        entryModel2.high == entryDto2.high
        entryModel2.low == entryDto2.low
        entryModel2.close == entryDto2.close
    }
}

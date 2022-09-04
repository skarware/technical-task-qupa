package dev.skaringa.qupa.factory


import dev.skaringa.qupa.provider.NasdaqDatasetDataEntryDtoProvider
import spock.lang.Specification

import java.time.LocalDate

class DailySingleValueChartDataEntryFactorySpec extends Specification {
    private def factory = new DailySingleValueChartDataEntryFactory()

    def "creates daily single value chart data entry models correctly"() {
        given: "valid dtos"
        def entryDto1 = NasdaqDatasetDataEntryDtoProvider.dto([date: LocalDate.of(2022, 3, 4)])
        def entryDto2 = NasdaqDatasetDataEntryDtoProvider.dto([date: LocalDate.of(2023, 4, 5)])

        when: "toModels is called"
        def models = factory.toModels([entryDto1, entryDto2])

        then: "models have correct values"
        models.size() == 2
        def entryModel1 = models[0]
        entryModel1.date == entryDto1.date
        entryModel1.value == entryDto1.volume
        def entryModel2 = models[1]
        entryModel2.date == entryDto2.date
        entryModel2.value == entryDto2.volume
    }
}

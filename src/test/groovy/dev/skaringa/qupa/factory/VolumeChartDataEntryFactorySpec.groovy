package dev.skaringa.qupa.factory

import dev.skaringa.qupa.api.nasdaq.dto.Response
import spock.lang.Specification

import java.time.LocalDate

class VolumeChartDataEntryFactorySpec extends Specification {
    private def factory = new VolumeChartDataEntryFactory()

    def "creates volume chart data entry models correctly"() {
        given: "valid dtos"
        def entryDto1 = new Response.Dataset.DataEntry(LocalDate.now(), 0.1, 0.2, 0.3, 0.4, 5, 6, 7, 0.8, 0.9, 0.11, 0.12, 13)
        def entryDto2 = new Response.Dataset.DataEntry(LocalDate.now().plusDays(1), 1.1, 1.2, 1.3, 1.4, 15, 16, 17, 1.8, 1.9, 1.11, 1.12, 113)

        when: "toModels is called"
        def models = factory.toModels([entryDto1, entryDto2])

        then: "models have correct values"
        models.size() == 2
        def entryModel1 = models[0]
        entryModel1.date == entryDto1.date
        entryModel1.volume == entryDto1.volume
        def entryModel2 = models[1]
        entryModel2.date == entryDto2.date
        entryModel2.volume == entryDto2.volume
    }
}

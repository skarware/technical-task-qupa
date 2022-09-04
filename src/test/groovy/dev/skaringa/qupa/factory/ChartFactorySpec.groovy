package dev.skaringa.qupa.factory

import dev.skaringa.qupa.api.nasdaq.dto.Response
import dev.skaringa.qupa.model.ChartType
import spock.lang.Specification

import java.time.LocalDate
import java.time.ZonedDateTime

class ChartFactorySpec extends Specification {
    private def factory = new ChartFactory(new CandlestickChartDataEntryFactory(), new VolumeChartDataEntryFactory())

    def "creates candlestick chart model correctly"() {
        given: "valid dto"
        def dataEntries = [new Response.Dataset.DataEntry(LocalDate.now(), 0.1, 0.2, 0.3, 0.4, 5, 6, 7, 0.8, 0.9, 0.11, 0.12, 13)]
        def dto = new Response.Dataset(1, "datasetCode", ZonedDateTime.now(), LocalDate.now(), LocalDate.now(), LocalDate.now(), LocalDate.now(), dataEntries)

        when: "toCandlestickChartModel is called"
        def model = factory.toCandlestickChartModel(dto)

        then: "model has correct values"
        model.ticker == dto.datasetCode
        model.from == dto.startDate
        model.to == dto.endDate
        model.type == ChartType.CANDLESTICK
        model.data.collect({ it.date }).containsAll(dto.data.collect({ it.date }))
    }

    def "creates volume chart model correctly"() {
        given: "valid dto"
        def dataEntries = [new Response.Dataset.DataEntry(LocalDate.now(), 0.1, 0.2, 0.3, 0.4, 5, 6, 7, 0.8, 0.9, 0.11, 0.12, 13)]
        def dto = new Response.Dataset(1, "datasetCode", ZonedDateTime.now(), LocalDate.now(), LocalDate.now(), LocalDate.now(), LocalDate.now(), dataEntries)

        when: "toVolumeChartModel is called"
        def model = factory.toVolumeChartModel(dto)

        then: "model has correct values"
        model.ticker == dto.datasetCode
        model.from == dto.startDate
        model.to == dto.endDate
        model.type == ChartType.VOLUME
        model.data.collect({ it.date }).containsAll(dto.data.collect({ it.date }))
    }
}

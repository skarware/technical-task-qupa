package dev.skaringa.qupa.factory

import dev.skaringa.qupa.SpecBase
import dev.skaringa.qupa.model.ChartType

class ChartFactorySpec extends SpecBase {
    private def factory = new ChartFactory(new CandlestickChartDataEntryFactory(), new VolumeChartDataEntryFactory())

    def "creates candlestick chart model correctly"() {
        given: "valid dto"
        def dto = nasdaqDataset()

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
        def dto = nasdaqDataset()

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
package dev.skaringa.qupa.factory

import dev.skaringa.qupa.SpecBase
import dev.skaringa.qupa.model.ChartType

class ChartFactorySpec extends SpecBase {
    private def factory = new ChartFactory(new DailyCandlestickChartDataEntryFactory(), new DailySingleValueChartDataEntryFactory())

    def "creates daily candlestick chart model correctly"() {
        given: "stock dataset model"
        def model = stockDataset()

        when: "toCandlestickChartModel is called"
        def result = factory.toDailyCandlestickChartModel(model)

        then: "created chart model has correct values"
        result.ticker == model.symbol
        result.from == model.from
        result.to == model.to
        result.type == ChartType.CANDLESTICK
        result.data.collect({ it.date }).containsAll(model.data.collect({ it.date }))
    }

    def "creates volume chart model correctly"() {
        given: "stock dataset model"
        def model = stockDataset()

        when: "toVolumeChartModel is called"
        def result = factory.toDailyVolumeChartModel(model)

        then: "created chart model has correct values"
        result.ticker == model.symbol
        result.from == model.from
        result.to == model.to
        result.type == ChartType.VOLUME
        result.data.collect({ it.date }).containsAll(model.data.collect({ it.date }))
    }
}

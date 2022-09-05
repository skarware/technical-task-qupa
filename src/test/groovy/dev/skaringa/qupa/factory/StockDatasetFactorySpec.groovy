package dev.skaringa.qupa.factory

import dev.skaringa.qupa.SpecBase

class StockDatasetFactorySpec extends SpecBase {
    private def factory = new StockDatasetFactory(new StockDatasetDataEntryFactory())

    def "creates stock model correctly"() {
        given: "valid dto"
        def dto = nasdaqDataset()

        when: "toModel is called"
        def model = factory.toModel(dto)

        then: "model has correct values"
        model.symbol == dto.datasetCode
        model.from == dto.startDate
        model.to == dto.endDate
        model.data.collect({ it.date }).containsAll(dto.data.collect({ it.date }))
    }
}

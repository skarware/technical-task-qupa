package dev.skaringa.qupa.controller

import dev.skaringa.qupa.SpecBaseIT
import dev.skaringa.qupa.model.CandlestickChartDataEntry
import dev.skaringa.qupa.model.Chart
import dev.skaringa.qupa.model.ChartType
import dev.skaringa.qupa.model.VolumeChartDataEntry
import dev.skaringa.qupa.service.ChartDataClient
import org.springframework.beans.factory.annotation.Autowired

import java.time.LocalDate

import static org.hamcrest.Matchers.hasSize
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ChartControllerSpec extends SpecBaseIT {
    @Autowired
    private ChartDataClient chartDataClient

    def "GET /api/chart/candlestick/{ticker} returns candlestick chart"() {
        given: "date range"
        def from = LocalDate.of(2012, 1, 1)
        def to = LocalDate.of(2022, 1, 1)

        and: "stub returns candlestick chart"
        def entry = new CandlestickChartDataEntry(LocalDate.now(), new BigDecimal("123.456"), new BigDecimal("123.456"), new BigDecimal("123.456"), new BigDecimal("123.456"))
        def toReturn = new Chart<>(TICKER, from, to, ChartType.CANDLESTICK, [entry])
        1 * chartDataClient.getCandlestickChart(TICKER, from, to) >> toReturn

        when: "GET /api/chart/candlestick/{ticker} is called"
        def response = mockMvc.perform(
                get("/api/chart/candlestick/{ticker}", TICKER)
                        .param("from", from.toString())
                        .param("to", to.toString()))

        then: "correct result is returned"
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.ticker').value(toReturn.ticker.toString()))
                .andExpect(jsonPath('$.from').value(toReturn.from.toString()))
                .andExpect(jsonPath('$.to').value(toReturn.to.toString()))
                .andExpect(jsonPath('$.type').value(toReturn.type.toString()))
                .andExpect(jsonPath('$.data', hasSize(toReturn.data.size())))
                .andExpect(jsonPath('$.data[0].date').value(toReturn.data[0].date.toString()))
                .andExpect(jsonPath('$.data[0].open').value(toReturn.data[0].open.toString()))
                .andExpect(jsonPath('$.data[0].high').value(toReturn.data[0].high.toString()))
                .andExpect(jsonPath('$.data[0].low').value(toReturn.data[0].low.toString()))
                .andExpect(jsonPath('$.data[0].close').value(toReturn.data[0].close.toString()))
    }

    def "GET /api/chart/volume/{ticker} returns volume chart"() {
        given: "date range"
        def from = LocalDate.of(2012, 1, 1)
        def to = LocalDate.of(2022, 1, 1)

        and: "stub returns volume chart"
        def entry1 = new VolumeChartDataEntry(LocalDate.now(), 123)
        def entry2 = new VolumeChartDataEntry(LocalDate.now(), 456)
        def entry3 = new VolumeChartDataEntry(LocalDate.now(), 789)
        def toReturn = new Chart<>(TICKER, from, to, ChartType.VOLUME, [entry1, entry2, entry3])
        1 * chartDataClient.getVolumeChart(TICKER, from, to) >> toReturn

        when: "GET /api/chart/volume/{ticker} is called"
        def response = mockMvc.perform(
                get("/api/chart/volume/{ticker}", TICKER)
                        .param("from", from.toString())
                        .param("to", to.toString()))

        then: "correct result is returned"
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.ticker').value(toReturn.ticker.toString()))
                .andExpect(jsonPath('$.from').value(toReturn.from.toString()))
                .andExpect(jsonPath('$.to').value(toReturn.to.toString()))
                .andExpect(jsonPath('$.type').value(toReturn.type.toString()))
                .andExpect(jsonPath('$.data', hasSize(toReturn.data.size())))
                .andExpect(jsonPath('$.data[0].date').value(toReturn.data[0].date.toString()))
                .andExpect(jsonPath('$.data[0].volume').value(toReturn.data[0].volume.toString()))
                .andExpect(jsonPath('$.data[1].date').value(toReturn.data[1].date.toString()))
                .andExpect(jsonPath('$.data[1].volume').value(toReturn.data[1].volume.toString()))
                .andExpect(jsonPath('$.data[2].date').value(toReturn.data[2].date.toString()))
                .andExpect(jsonPath('$.data[2].volume').value(toReturn.data[2].volume.toString()))
    }
}

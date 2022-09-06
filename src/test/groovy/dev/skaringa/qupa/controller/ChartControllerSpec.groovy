package dev.skaringa.qupa.controller

import dev.skaringa.qupa.SpecBaseIT
import dev.skaringa.qupa.api.ErrorCode
import dev.skaringa.qupa.model.ChartType
import dev.skaringa.qupa.provider.NasdaqDatasetDataEntryDtoProvider
import dev.skaringa.qupa.service.StockMarketDataClient
import org.springframework.beans.factory.annotation.Autowired

import java.time.LocalDate

import static org.hamcrest.Matchers.hasSize
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ChartControllerSpec extends SpecBaseIT {
    @Autowired
    private StockMarketDataClient stockMarketDataClient

    def "GET /api/chart/candlestick/{ticker} returns candlestick chart"() {
        given: "date range"
        def from = LocalDate.of(2012, 1, 1)
        def to = LocalDate.of(2022, 1, 1)

        and: "stub returns stock market data"
        def toReturn = nasdaqDataset()
        1 * stockMarketDataClient.getStockData(TICKER, from, to) >> toReturn

        when: "GET /api/chart/candlestick/{ticker} is called"
        def response = mockMvc.perform(
                get("/api/chart/candlestick/{ticker}", TICKER)
                        .param("from", from.toString())
                        .param("to", to.toString()))

        then: "correct result is returned"
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.ticker').value(toReturn.datasetCode.toString()))
                .andExpect(jsonPath('$.from').value(toReturn.startDate.toString()))
                .andExpect(jsonPath('$.to').value(toReturn.endDate.toString()))
                .andExpect(jsonPath('$.type').value(ChartType.CANDLESTICK.name()))
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

        and: "stub returns stock market data"
        def toReturn = nasdaqDataset()
        1 * stockMarketDataClient.getStockData(TICKER, from, to) >> toReturn

        when: "GET /api/chart/volume/{ticker} is called"
        def response = mockMvc.perform(
                get("/api/chart/volume/{ticker}", TICKER)
                        .param("from", from.toString())
                        .param("to", to.toString()))

        then: "correct result is returned"
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.ticker').value(toReturn.datasetCode.toString()))
                .andExpect(jsonPath('$.from').value(toReturn.startDate.toString()))
                .andExpect(jsonPath('$.to').value(toReturn.endDate.toString()))
                .andExpect(jsonPath('$.type').value(ChartType.VOLUME.name()))
                .andExpect(jsonPath('$.data', hasSize(toReturn.data.size())))
                .andExpect(jsonPath('$.data[0].date').value(toReturn.data[0].date.toString()))
                .andExpect(jsonPath('$.data[0].value').value(toReturn.data[0].volume.toString()))
                .andExpect(jsonPath('$.data[1].date').value(toReturn.data[1].date.toString()))
                .andExpect(jsonPath('$.data[1].value').value(toReturn.data[1].volume.toString()))
                .andExpect(jsonPath('$.data[2].date').value(toReturn.data[2].date.toString()))
                .andExpect(jsonPath('$.data[2].value').value(toReturn.data[2].volume.toString()))
    }

    def "GET /api/chart/sma/{ticker} returns SMA chart"() {
        given: "period and date range"
        def period = 1
        def from = LocalDate.of(2012, 1, 1)
        def to = LocalDate.of(2022, 1, 1)

        and: "stub returns stock market data"
        def entry1 = NasdaqDatasetDataEntryDtoProvider.dto([date: from.minusDays(1), close: 1])
        def entry2 = NasdaqDatasetDataEntryDtoProvider.dto([date: from, close: 2])
        def entry3 = NasdaqDatasetDataEntryDtoProvider.dto([date: from.plusDays(1), close: 3])
        def toReturn = nasdaqDataset([startDate: from, endDate: to, data: [entry1, entry2, entry3]])
        def fromWithLossMarketCloseDaysOffset = from.minusDays(period * 2L)
        1 * stockMarketDataClient.getStockData(TICKER, fromWithLossMarketCloseDaysOffset, to) >> toReturn

        when: "GET /api/chart/sma/{ticker} is called"
        def response = mockMvc.perform(
                get("/api/chart/sma/{ticker}", TICKER)
                        .param("from", from.toString())
                        .param("to", to.toString())
                        .param("period", period.toString()))

        then: "correct result is returned"
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.ticker').value(toReturn.datasetCode.toString()))
                .andExpect(jsonPath('$.from').value(toReturn.startDate.toString()))
                .andExpect(jsonPath('$.to').value(toReturn.endDate.toString()))
                .andExpect(jsonPath('$.type').value(ChartType.SMA.name()))
                .andExpect(jsonPath('$.data', hasSize(2)))
                .andExpect(jsonPath('$.data[0].date').value(toReturn.data[1].date.toString()))
                .andExpect(jsonPath('$.data[0].value').value(1))
                .andExpect(jsonPath('$.data[1].date').value(toReturn.data[2].date.toString()))
                .andExpect(jsonPath('$.data[1].value').value(2))
    }

    def "GET /api/chart/sma/{ticker} returns 400 when request param of period of zero given"() {
        given: "period and date range"
        def period = 0
        def from = LocalDate.of(2012, 1, 1)
        def to = LocalDate.of(2022, 1, 1)

        when: "GET /api/chart/sma/{ticker} is called"
        def response = mockMvc.perform(
                get("/api/chart/sma/{ticker}", TICKER)
                        .param("from", from.toString())
                        .param("to", to.toString())
                        .param("period", period.toString()))

        then: "response status is 400"
        response
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.[0].code').value(ErrorCode.ILLEGAL_ARGUMENT.name()))
                .andExpect(jsonPath('$.[0].message').value("SMA period should be greater than 0 and less than 500"))

        and: "stock market data client stub is not called"
        0 * stockMarketDataClient.getStockData(_)
    }

    def "GET /api/chart/sma/{ticker} returns 400 when request param of period less than 0 given"() {
        given: "period and date range"
        def period = -1
        def from = LocalDate.of(2012, 1, 1)
        def to = LocalDate.of(2022, 1, 1)

        when: "GET /api/chart/sma/{ticker} is called"
        def response = mockMvc.perform(
                get("/api/chart/sma/{ticker}", TICKER)
                        .param("from", from.toString())
                        .param("to", to.toString())
                        .param("period", period.toString()))

        then: "response status is 400"
        response
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.[0].code').value(ErrorCode.ILLEGAL_ARGUMENT.name()))
                .andExpect(jsonPath('$.[0].message').value("SMA period should be greater than 0 and less than 500"))

        and: "stock market data client stub is not called"
        0 * stockMarketDataClient.getStockData(_)
    }

    def "GET /api/chart/sma/{ticker} returns 400 when request param of period greater than 500 given"() {
        given: "period and date range"
        def period = 501
        def from = LocalDate.of(2012, 1, 1)
        def to = LocalDate.of(2022, 1, 1)

        when: "GET /api/chart/sma/{ticker} is called"
        def response = mockMvc.perform(
                get("/api/chart/sma/{ticker}", TICKER)
                        .param("from", from.toString())
                        .param("to", to.toString())
                        .param("period", period.toString()))

        then: "response status is 400"
        response
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.[0].code').value(ErrorCode.ILLEGAL_ARGUMENT.name()))
                .andExpect(jsonPath('$.[0].message').value("SMA period should be greater than 0 and less than 500"))

        and: "stock market data client stub is not called"
        0 * stockMarketDataClient.getStockData(_)
    }

    def "GET /api/chart/volume/{ticker} returns 400 when invalid request date range params given"() {
        given: "invalid request"
        def request = get("/api/chart/volume/{ticker}", TICKER)
                .param("from", "01-01-2012")
                .param("to", "01-01-2022")

        when: "GET /api/chart/volume/{ticker} is called"
        def response = mockMvc.perform(request)

        then: "response status is 400"
        response
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.[0].code').value(ErrorCode.INVALID_ARGUMENT.name()))

        and: "stock market data client stub is not called"
        0 * stockMarketDataClient.getStockData(_)
    }

    def "GET /api/chart/volume/{ticker} returns 400 when invalid request without date range params given"() {
        given: "invalid request"
        def request = get("/api/chart/volume/{ticker}", TICKER)

        when: "GET /api/chart/volume/{ticker} is called"
        def response = mockMvc.perform(request)

        then: "response status is 400"
        response
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.[0].code').value(ErrorCode.UNEXPECTED.name()))

        and: "stock market data client stub is not called"
        0 * stockMarketDataClient.getStockData(_)
    }
}

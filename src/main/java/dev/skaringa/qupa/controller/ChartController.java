package dev.skaringa.qupa.controller;

import dev.skaringa.qupa.model.Chart;
import dev.skaringa.qupa.model.ChartDataEntry;
import dev.skaringa.qupa.model.ChartRequest;
import dev.skaringa.qupa.model.ChartType;
import dev.skaringa.qupa.service.ChartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@CrossOrigin("*")
@Api(tags = "Chart Controller")
@RequiredArgsConstructor
@RequestMapping("api/chart")
public class ChartController {
    private final ChartService chartService;

    @GetMapping("{ticker}")
    public Chart<ChartDataEntry> getChart(
            @ApiParam(value = "Ticker", example = "AAPL")
            @PathVariable String ticker,
            @ApiParam(value = "Date From", example = "2012-01-01")
            @RequestParam(value = "from") LocalDate from,
            @ApiParam(value = "Date To", example = "2022-01-01")
            @RequestParam(value = "to") LocalDate to,
            @ApiParam(value = "ChartType", example = "VOLUME")
            @RequestParam(value = "chartType") ChartType chartType,
            @ApiParam(value = "SMA period in Days", example = "5")
            @RequestParam(value = "period", required = false) Integer period) {
        if (chartType == ChartType.SMA && period == null) {
            throw new IllegalArgumentException("SMA chart requires 'period' value");
        }
        if (period != null) {
            Assert.isTrue(period > 0 && period <= 500, "SMA period should be greater than 0 and less than 500");
        }

        ChartRequest request = new ChartRequest(ticker, from, to, chartType, period);

        if (request.getChartType() == ChartType.CANDLESTICK) return chartService.getCandlestickChart(request);
        if (request.getChartType() == ChartType.VOLUME) return chartService.getVolumeChart(request);
        if (request.getChartType() == ChartType.SMA) return chartService.getSMAChart(request);

        throw new IllegalArgumentException("Unexpected type of chart: " + chartType.getClass());
    }
}

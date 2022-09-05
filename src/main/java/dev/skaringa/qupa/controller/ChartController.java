package dev.skaringa.qupa.controller;

import dev.skaringa.qupa.model.Chart;
import dev.skaringa.qupa.model.ChartDataEntry;
import dev.skaringa.qupa.model.ChartRequest;
import dev.skaringa.qupa.service.ChartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@CrossOrigin("*")
@Api(tags = "Chart Controller")
@RequiredArgsConstructor
@RequestMapping("api/chart")
public class ChartController {
    private final ChartService chartService;

    @GetMapping("candlestick/{ticker}")
    public Chart<ChartDataEntry> getCandlestickChart(
            @ApiParam(value = "Ticker", example = "AAPL")
            @PathVariable String ticker,
            @ApiParam(value = "Date From", example = "2012-01-01")
            @RequestParam(value = "from") LocalDate from,
            @ApiParam(value = "Date To", example = "2022-01-01")
            @RequestParam(value = "to") LocalDate to) {
        return chartService.getCandlestickChart(new ChartRequest(ticker, from, to));
    }

    @GetMapping("volume/{ticker}")
    public Chart<ChartDataEntry> getVolumeChart(
            @ApiParam(value = "Ticker", example = "AAPL")
            @PathVariable String ticker,
            @ApiParam(value = "Date From", example = "2012-01-01")
            @RequestParam(value = "from") LocalDate from,
            @ApiParam(value = "Date To", example = "2022-01-01")
            @RequestParam(value = "to") LocalDate to) {
        return chartService.getVolumeChart(new ChartRequest(ticker, from, to));
    }

    @GetMapping("sma/{ticker}")
    public Chart<ChartDataEntry> getSMAChart(
            @ApiParam(value = "Ticker", example = "AAPL")
            @PathVariable String ticker,
            @ApiParam(value = "Date From", example = "2012-01-01")
            @RequestParam(value = "from") LocalDate from,
            @ApiParam(value = "Date To", example = "2022-01-01")
            @RequestParam(value = "to") LocalDate to,
            @ApiParam(value = "SMA period in Days", example = "5")
            @RequestParam(value = "period") int period) {
        return chartService.getSMAChart(new ChartRequest(ticker, from, to), period);
    }
}

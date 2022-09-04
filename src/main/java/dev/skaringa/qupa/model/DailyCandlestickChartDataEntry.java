package dev.skaringa.qupa.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@ApiModel("Daily Candlestick Chart Data Entry")
public class DailyCandlestickChartDataEntry implements ChartDataEntry {
    @NonNull
    @ApiModelProperty(value = "Date", required = true, example = "2022-01-01")
    LocalDate date;

    @NonNull
    @ApiModelProperty(value = "Open", required = true, example = "123.456")
    BigDecimal open;

    @NonNull
    @ApiModelProperty(value = "High", required = true, example = "123.456")
    BigDecimal high;

    @NonNull
    @ApiModelProperty(value = "Low", required = true, example = "123.456")
    BigDecimal low;

    @NonNull
    @ApiModelProperty(value = "Close", required = true, example = "123.456")
    BigDecimal close;
}

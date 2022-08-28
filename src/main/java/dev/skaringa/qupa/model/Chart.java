package dev.skaringa.qupa.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Value
@ApiModel("Chart")
public class Chart<T extends ChartDataEntry> {
    @NonNull
    @ApiModelProperty(value = "Ticker", required = true, example = "AAPL")
    String ticker;

    @NonNull
    @ApiModelProperty(value = "From", required = true, example = "2012-01-01")
    LocalDate from;

    @NonNull
    @ApiModelProperty(value = "To", required = true, example = "2022-01-01")
    LocalDate to;

    @NonNull
    @ApiModelProperty(value = "Chart Type", example = "CANDLESTICK", required = true)
    ChartType type;

    @NonNull
    @ApiModelProperty(value = "List of Chart Data Entries", required = true)
    List<T> data;
}

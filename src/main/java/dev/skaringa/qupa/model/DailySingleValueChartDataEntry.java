package dev.skaringa.qupa.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;

@Value
@ApiModel("Daily Single Value Chart Data Entry")
public class DailySingleValueChartDataEntry<T extends Number> implements ChartDataEntry {
    @NonNull
    @ApiModelProperty(value = "Date", required = true, example = "2022-01-01")
    LocalDate date;

    @NonNull
    @ApiModelProperty(value = "Number", example = "123", required = true)
    T value;
}

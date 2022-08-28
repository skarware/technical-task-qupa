package dev.skaringa.qupa.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;

@Value
@ApiModel("Volume Chart Data Entry")
public class VolumeChartDataEntry implements ChartDataEntry {
    @NonNull
    @ApiModelProperty(value = "Date", required = true, example = "2022-01-01")
    LocalDate date;

    @NonNull
    @ApiModelProperty(value = "Volume", example = "123", required = true)
    Long volume;
}

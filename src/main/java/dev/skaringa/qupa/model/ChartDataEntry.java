package dev.skaringa.qupa.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CandlestickChartDataEntry.class, name = "candlestick"),
        @JsonSubTypes.Type(value = VolumeChartDataEntry.class, name = "volume")
})
public interface ChartDataEntry {
}

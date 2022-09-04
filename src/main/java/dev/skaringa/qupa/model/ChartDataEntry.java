package dev.skaringa.qupa.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DailyCandlestickChartDataEntry.class, name = "candlestick"),
        @JsonSubTypes.Type(value = DailySingleValueChartDataEntry.class, name = "single-value")
})
public interface ChartDataEntry {
}

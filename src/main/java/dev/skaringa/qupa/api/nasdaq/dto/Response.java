package dev.skaringa.qupa.api.nasdaq.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Response {
    Dataset dataset;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Dataset {
        Integer id;
        String datasetCode;
        ZonedDateTime refreshedAt;
        LocalDate newestAvailableDate;
        LocalDate oldestAvailableDate;
        LocalDate startDate;
        LocalDate endDate;
        List<DataEntry> data;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @ToString
        @JsonFormat(shape = JsonFormat.Shape.ARRAY)
        public static class DataEntry {
            LocalDate date;
            BigDecimal open;
            BigDecimal high;
            BigDecimal low;
            BigDecimal close;
            Long volume;
            Integer exDividend;
            Integer splitRatio;
            BigDecimal adjOpen;
            BigDecimal adjHigh;
            BigDecimal adjLow;
            BigDecimal adjClose;
            Long adjVolume;
        }
    }
}

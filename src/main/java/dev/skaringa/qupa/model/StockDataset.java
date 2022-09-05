package dev.skaringa.qupa.model;

import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Value
public class StockDataset {
    @NonNull
    String symbol;
    @NonNull
    LocalDate from;
    @NonNull
    LocalDate to;
    @NonNull
    List<DataEntry> data;

    @Value
    public static class DataEntry {
        @NonNull
        LocalDate date;
        @NonNull
        BigDecimal open;
        @NonNull
        BigDecimal high;
        @NonNull
        BigDecimal low;
        @NonNull
        BigDecimal close;
        @NonNull
        Long volume;
    }
}

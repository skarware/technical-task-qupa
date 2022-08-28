package dev.skaringa.qupa.model;

import lombok.Value;

import java.time.LocalDate;

@Value
public class ChartRequest {
    String ticker;
    LocalDate from;
    LocalDate to;
}

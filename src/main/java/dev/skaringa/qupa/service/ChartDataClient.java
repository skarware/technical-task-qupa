package dev.skaringa.qupa.service;

import java.time.LocalDate;

public interface ChartDataClient {
    Object getCandlestickChart(String ticker, LocalDate from, LocalDate to);

    Object getVolumeChart(String ticker, LocalDate from, LocalDate to);
}

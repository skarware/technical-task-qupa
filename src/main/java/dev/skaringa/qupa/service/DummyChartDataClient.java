package dev.skaringa.qupa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DummyChartDataClient implements ChartDataClient {
    @Override
    public Object getCandlestickChart(String ticker, LocalDate from, LocalDate to) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Object getVolumeChart(String ticker, LocalDate from, LocalDate to) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

package dev.skaringa.qupa.factory;

import dev.skaringa.qupa.model.Chart;
import dev.skaringa.qupa.model.ChartDataEntry;
import org.springframework.stereotype.Component;

@Component
public class CandlestickChartFactory {
    public Chart<ChartDataEntry> toModel(Object source) {
//        TODO: refactor once source type is not known
        return (Chart<ChartDataEntry>) source;
    }
}

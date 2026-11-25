package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import browser.Plotter;

import java.util.ArrayList;
import java.util.List;

public class HistoryHandler extends NgordnetQueryHandler {
    private NGramMap map;

    public HistoryHandler(NGramMap map) {
        this.map = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> labels = q.words();
        List<TimeSeries> series = new ArrayList<>();

        for (String word : labels) {
            series.add(map.weightHistory(word, q.startYear(), q.endYear()));
        }

        return Plotter.encodeChartAsString(Plotter.generateTimeSeriesChart(labels, series));
    }
}

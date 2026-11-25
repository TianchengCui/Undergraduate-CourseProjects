package main;

import browser.NgordnetQueryHandler;
import browser.NgordnetQuery;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {
    private NGramMap ngm;

    public HistoryTextHandler(NGramMap map) {
        ngm = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        String result = "";
        for (String word : words) {
            TimeSeries ts = ngm.weightHistory(word, startYear, endYear);
            result += word + ": " + ts + "\n";
        }
        return result;
    }
}

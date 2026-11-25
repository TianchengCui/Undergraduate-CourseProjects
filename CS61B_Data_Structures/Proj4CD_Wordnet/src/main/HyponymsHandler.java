package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;

public class HyponymsHandler extends NgordnetQueryHandler {
    private WordNet wn;
    private NGramMap ngm;

    public HyponymsHandler(WordNet wn, NGramMap ngm) {
        this.wn = wn;
        this.ngm = ngm;
    }

    @Override
    public String handle(NgordnetQuery q) {
        if (q.k() == 0) {
            return wn.hyponymsList(q.words()).toString();
        }
        return wn.hyponymsList(q.words(), q.startYear(), q.endYear(), q.k(), ngm).toString();
    }
}

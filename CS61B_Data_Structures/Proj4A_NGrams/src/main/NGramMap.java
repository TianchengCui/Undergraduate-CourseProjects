package main;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.HashMap;

import static main.TimeSeries.MAX_YEAR;
import static main.TimeSeries.MIN_YEAR;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    private TimeSeries totalCounts;
    private HashMap<String, TimeSeries> wordHistories;

    /**
     * Constructs an NGramMap from WORDHISTORYFILENAME and YEARHISTORYFILENAME.
     */
    public NGramMap(String wordHistoryFilename, String yearHistoryFilename) {
        totalCounts = new TimeSeries();
        wordHistories = new HashMap<>();

        In in = new In(wordHistoryFilename);
        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] parts = line.split("\t");
            String word = parts[0];
            int year = Integer.parseInt(parts[1]);
            double count = Double.parseDouble(parts[2]);

            if (!wordHistories.containsKey(word)) {
                wordHistories.put(word, new TimeSeries());
            }
            wordHistories.get(word).put(year, count);
        }

        In in2 = new In(yearHistoryFilename);
        while (!in2.isEmpty()) {
            String line = in2.readLine();
            String[] parts = line.split(",");
            int year = Integer.parseInt(parts[0]);
            double count = Double.parseDouble(parts[1]);
            totalCounts.put(year, count);
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        if (!wordHistories.containsKey(word)) {
            return new TimeSeries();
        }
        return new TimeSeries(wordHistories.get(word), startYear, endYear);
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        if (!wordHistories.containsKey(word)) {
            return new TimeSeries();
        }
        return new TimeSeries(wordHistories.get(word), MIN_YEAR, MAX_YEAR);
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        return new TimeSeries(totalCounts, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        if (!wordHistories.containsKey(word)) {
            return new TimeSeries();
        }
        return countHistory(word, startYear, endYear).dividedBy(new TimeSeries(totalCounts, startYear, endYear));
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        if (!wordHistories.containsKey(word)) {
            return new TimeSeries();
        }
        return countHistory(word).dividedBy(totalCountHistory());
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        TimeSeries result = new TimeSeries();
        for (String word : words) {
            result = result.plus(weightHistory(word, startYear, endYear));
        }
        return result;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries result = new TimeSeries();
        for (String word : words) {
            result = result.plus(weightHistory(word));
        }
        return result;
    }

}

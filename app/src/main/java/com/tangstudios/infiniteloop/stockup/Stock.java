package com.tangstudios.infiniteloop.stockup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Nick on 1/14/2017.
 */
public abstract class Stock implements Serializable {

    private double value;
    private List<Double> previousValues, dailyValues;
    private double openPrice, dailyLow, dailyHigh;
    private String name, abbreviation;
    private Random r;

    public Stock(String name, String abbreviation, double startValue) {
        this.name = name;
        this.abbreviation = abbreviation;
        value = startValue;
        previousValues = new ArrayList<Double>();
        dailyValues = new ArrayList<Double>();
        dailyLow = startValue;
        dailyHigh = startValue;
        r = new Random();

        for(int i = 0; i < 24 * 31; i++) {
            int num = r.nextInt(21) - 10;
            previousValues.add(value);
            if (i >= 24 * 30) {
                if (i == 24 * 30) openPrice = value;
                dailyValues.add(value);
                dailyLow = Math.min(value, dailyLow);
                dailyHigh = Math.max(value, dailyHigh);
            }
            value += value * num / 2000;
        }
    }

    public double getValue() {
        return value;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public double getDailyLow() {
        return dailyLow;
    }

    public double getDailyHigh() {
        return dailyHigh;
    }

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public List<Double> getPreviousValues() {
        return previousValues;
    }

    public List<Double> getDailyValues() {
        return dailyValues;
    }

    public double getDifference() {
        return value - openPrice;
    }

    public double getPercentageDifference() {
        return getDifference() / openPrice * 100;
    }

    public void update(int overallMarketPerformance) {
        int randomPerformance;
        int overallMarket = overallMarketPerformance;
        double stockTrend = 0;

        //random number between -180 and 180
        int random = r.nextInt(301) - 150;
        if (random < -140) {
            randomPerformance = random + 110;
        } else if (random < -100) {
            randomPerformance = (random + 1) / 2 + 40;
        } else if (random < 0) {
            randomPerformance = random / 10 - 1;
        } else if (random > 140) {
            randomPerformance = random - 110;
        } else if (random > 100) {
            randomPerformance = (random - 1) / 2 - 40;
        } else {
            randomPerformance = random / 10 + 1;
        }

        double oneDayAgo = previousValues.get(previousValues.size() - 24);

        if (value > oneDayAgo) {
            stockTrend = (value/oneDayAgo - 1);
        } else {
            stockTrend = (oneDayAgo/value - 1) * -1;
        }

        double overallPerformance = randomPerformance + overallMarket + stockTrend;

        value += value * overallPerformance / 350;

        dailyValues.add(value);
        previousValues.add(value);
        dailyLow = Math.min(value, dailyLow);
        dailyHigh = Math.max(value, dailyHigh);
    }

    public void endDay() {
        dailyValues.clear();
        dailyValues.add(value);
        openPrice = value;
        dailyLow = value;
        dailyHigh = value;
    }

    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        return this.name.equals(other.toString());
    }

}
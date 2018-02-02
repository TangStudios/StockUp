package com.tangstudios.infiniteloop.stockup;
import java.io.Serializable;
import java.util.List;
import java.util.Random;
//import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by William on 1/14/2017.
 */
public class StockMarket implements Serializable {

    private List<Stock> stocks;
    private Random rand;
    private int days;
    private int totalDays;

    public StockMarket(int totalDays) {
        this.stocks = new ArrayList<Stock>();
        stocks.add(new Bullseye());
        stocks.add(new Pear());
        stocks.add(new Macrohard());
        stocks.add(new Nile());
        stocks.add(new Faceblog());
        stocks.add(new Goggle());
        stocks.add(new Tweety());
        stocks.add(new Yippee());
        stocks.add(new Deezknee());
        stocks.add(new ColaCoco());
        rand = new Random();
        this.days = 0;
        this.totalDays = totalDays;
    }

    public void update() {
        int performance = rand.nextInt(11) - 5;
        for(Stock s : stocks){
            s.update(performance);
        }
    }
    
    public void endDay() { 
    	for(Stock s : stocks){
            s.endDay();
        }
        days++;
    }

    public Stock getStock(int index) {
        return stocks.get(index);
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public int getDays() {
        return days;
    }
}

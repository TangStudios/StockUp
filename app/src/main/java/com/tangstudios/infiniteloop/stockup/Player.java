package com.tangstudios.infiniteloop.stockup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by William on 1/14/2017.
 */
public class Player implements Serializable {

    private String playerName;
    private double balance, balanceInStocks;
    private Map<Stock, Integer> stocks;
    private List<String> stocksOwned;

    private boolean isReady;

    public Player(String playerName, double balance){
        this.playerName = playerName;
        this.balance = balance;
        this.isReady = false;
        this.balanceInStocks = 0;
        this.stocks = new HashMap<Stock, Integer>();
        this.stocksOwned = new ArrayList<String>();
    }

    public boolean buyStocks(Stock stock, int shares) {
        if(shares * stock.getValue() <= balance) {
            if (stocksOwned.contains(stock.getName())) {
                Iterator<Stock> i = stocks.keySet().iterator();
                while (i.hasNext()) {
                    Stock next = i.next();
                    if (next.getName().equals(stock.getName())) {
                        int ownedShares = stocks.remove(next);
                        stocks.put(stock, shares + ownedShares);
                        break;
                    }
                }
            } else {
                stocks.put(stock, shares);
                stocksOwned.add(stock.getName());
            }
            balance -= shares * stock.getValue();
            balanceInStocks += shares * stock.getValue();
            return true;
        }
        return false;
    }

    public boolean sellStocks(Stock stock, int shares) {
        if(!stocksOwned.contains(stock.getName()) || getSharesOf(stock) - shares < 0) {
            return false;
        } else {
            Iterator<Stock> i = stocks.keySet().iterator();
            while (i.hasNext()) {
                Stock next = i.next();
                if (next.getName().equals(stock.getName())) {
                    int ownedShares = stocks.remove(next);
                    stocks.put(stock, ownedShares - shares);
                    break;
                }
            }
        }
        balance += shares * stock.getValue();
        balanceInStocks -= shares * stock.getValue();
        return true;
    }

    public void update(List<Stock> stocks) {
        balanceInStocks = 0;
        for (Stock stock : stocks) {
            if (stocksOwned.contains(stock.getName())) {
                balanceInStocks += getSharesOf(stock) * stock.getValue();
            }
        }
    }

    public double getBalance() {
        return balance;
    }

    public double getBalanceInStocks() {
        return balanceInStocks;
    }

    public boolean isReady() {
        return isReady;
    }

    public void ready() {
        isReady = true;
    }

    public void notReady() {
        isReady = false;
    }

    public Map<Stock, Integer> getStocks() {
        return stocks;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getSharesOf(Stock stock) {
        for (Stock i : stocks.keySet()) {
            if (i.getName().equals(stock.getName())) {
                return stocks.get(i);
            }
        }
        return 0;
    }
}

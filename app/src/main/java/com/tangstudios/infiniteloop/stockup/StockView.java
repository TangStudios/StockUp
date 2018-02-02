package com.tangstudios.infiniteloop.stockup;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DecimalFormat;
import java.util.List;

public class StockView extends AppCompatActivity {
    TextView abbreviationTextView;
    TextView titleTextView;
    TextView valueTextView;
    TextView differenceTextView;
    TextView openPriceTextView;
    TextView highPriceTextView;
    TextView lowPriceTextView;
    TextView currentSharesTextView;

    Button buyShareButton;
    Button sellShareButton;

    EditText buyShareEdittext;
    EditText sellShareEdittext;

    GraphView graphView;

    Player player1;
    StockMarket stockMarket;
    int position;
    List<Stock> stocks;
    Stock stock;

    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        df = new DecimalFormat("0.00");

        abbreviationTextView = (TextView) findViewById(R.id.abbreviationView);
        titleTextView = (TextView) findViewById(R.id.titleView);
        differenceTextView = (TextView) findViewById(R.id.differenceView);
        valueTextView = (TextView) findViewById(R.id.valueView);
        openPriceTextView = (TextView) findViewById(R.id.openView);
        highPriceTextView = (TextView) findViewById(R.id.highView);
        lowPriceTextView = (TextView) findViewById(R.id.lowView);
        currentSharesTextView = (TextView) findViewById(R.id.currentSharesView);
        buyShareButton = (Button) findViewById(R.id.buyButtonView);
        sellShareButton = (Button) findViewById(R.id.sellButtonView);
        buyShareEdittext = (EditText) findViewById(R.id.buyEdittext);
        sellShareEdittext = (EditText) findViewById(R.id.sellEdittext);
        graphView = (GraphView) findViewById(R.id.graphView);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        player1 = (Player) intent.getSerializableExtra("player1");
        stockMarket = (StockMarket) intent.getSerializableExtra("stockMarket");

        stocks = stockMarket.getStocks();
        stock = stocks.get(position);
        abbreviationTextView.setText(stock.getAbbreviation() + " - ");
        titleTextView.setText(stock.getName());
        valueTextView.setText("$" + df.format(stock.getValue()) + "  ");
        double difference = stock.getDifference();
        if (difference >= 0) {
            differenceTextView.setText("+" + df.format(difference) + " (" + df.format(stock.getPercentageDifference()) + "%)");
            differenceTextView.setTextColor(Color.GREEN);
        } else {
            differenceTextView.setText("" + df.format(difference) + " (" + df.format(stock.getPercentageDifference()) + "%)");
            differenceTextView.setTextColor(Color.RED);
        }
        openPriceTextView.setText("Open: $" + df.format(stock.getOpenPrice()));
        highPriceTextView.setText("High: $" + df.format(stock.getDailyHigh()));
        lowPriceTextView.setText("Low: $" + df.format(stock.getDailyLow()));
        currentSharesTextView.setText("Shares: " + player1.getSharesOf(stock));

        List<Double> graphData = stock.getDailyValues();
        DataPoint[] data = new DataPoint[graphData.size()];
        for (int i = 0; i < data.length; i++) {
            data[i] = new DataPoint(i, graphData.get(i));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(data);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMaxX(24);
        graphView.addSeries(series);
    }

    public void onBuy(View view) {
        if (!(buyShareEdittext.getText().toString().length() < 1)) {
            if (player1.buyStocks(stock, Integer.parseInt(buyShareEdittext.getText().toString()))) {
                Toast.makeText(StockView.this, "Success!", Toast.LENGTH_LONG).show();
                currentSharesTextView.setText("Shares: " + player1.getSharesOf(stock));
            } else {
                Toast.makeText(StockView.this, "Insufficient Funds", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onSell(View view) {
        if (!(sellShareEdittext.getText().toString().length() < 1)) {
            if (player1.sellStocks(stock, Integer.parseInt(sellShareEdittext.getText().toString()))) {
                Toast.makeText(StockView.this, "Success!", Toast.LENGTH_LONG).show();
                currentSharesTextView.setText("Shares: " + player1.getSharesOf(stock));
            } else {
                Toast.makeText(StockView.this, "Insufficient Shares", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onDayGraph(View view) {
        graphView.removeAllSeries();
        List<Double> graphData = stock.getDailyValues();
        DataPoint[] data = new DataPoint[24];
        for (int i = 0; i < data.length; i++) {
            data[i] = new DataPoint(i, graphData.get(i));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(data);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMaxX(24);
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return super.formatLabel(value, isValueX);
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });
        graphView.addSeries(series);
    }

    public void onWeekGraph(View view) {
        graphView.removeAllSeries();
        List<Double> graphData = stock.getPreviousValues();
        DataPoint[] data = new DataPoint[24 * 7];
        for (int i = 0; i < 24 * 7; i++) {
            data[i] = new DataPoint(i, graphData.get(graphData.size() - 24 * 7 + i - 1));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(data);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMaxX(24 * 7);
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return super.formatLabel(value / 24, isValueX);
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });
        graphView.addSeries(series);
    }

    public void onMonthGraph(View view) {
        graphView.removeAllSeries();
        List<Double> graphData = stock.getPreviousValues();
        DataPoint[] data = new DataPoint[24 * 31];
        for (int i = 0; i < 24 * 31; i++) {
            data[i] = new DataPoint(i, graphData.get(graphData.size() - 24 * 31 + i));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(data);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMaxX(24 * 31);
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return super.formatLabel(value / 24, isValueX);
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });
        graphView.addSeries(series);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(this, StockList.class);
                intent.putExtra("player1", player1);
                intent.putExtra("stockMarket", stockMarket);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    }
}

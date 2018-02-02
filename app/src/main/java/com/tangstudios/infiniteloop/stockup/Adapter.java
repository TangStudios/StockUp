package com.tangstudios.infiniteloop.stockup;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DecimalFormat;
import java.util.*;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;

    List<Stock> data = Collections.emptyList();
    Player player;

    DecimalFormat df;

    public Adapter(Context context, List<Stock> data, Player player) {

        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.player = player;

        df = new DecimalFormat("0.00");

    }

    public void add() {


    }

    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.stock_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Stock current = data.get(position);
        holder.stockName.setText(current.getAbbreviation() + ": " + df.format(current.getValue()));
        holder.openPrice.setText("Open: " + df.format(current.getOpenPrice()));
        holder.highPrice.setText("High: " + df.format(current.getDailyHigh()));
        holder.lowPrice.setText("Low: " + df.format(current.getDailyLow()));
        if (current.getDifference() >= 0) {
            holder.difference.setText("(+" + df.format(current.getDifference()) + ")");
            holder.difference.setTextColor(Color.GREEN);
        } else {
            holder.difference.setText("(" + df.format(current.getDifference()) + ")");
            holder.difference.setTextColor(Color.RED);
        }
        holder.shareAmount.setText("Shares: " + player.getSharesOf(current));
        List<Double> graphData = current.getDailyValues();
        DataPoint[] data = new DataPoint[graphData.size()];
        //data[0] = new DataPoint(0, current.getValue());
        for (int i = 0; i < graphData.size(); i++) {
            data[i] = new DataPoint(i, graphData.get(i));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(data);
        holder.graph.getViewport().setXAxisBoundsManual(true);
        holder.graph.getViewport().setMaxX(24);
        holder.graph.addSeries(series);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView stockName, openPrice, highPrice, lowPrice, shareAmount, difference;
        GraphView graph;

        public MyViewHolder(View itemView) {
            super(itemView);

            stockName = (TextView) itemView.findViewById(R.id.stockNameList);
            openPrice = (TextView) itemView.findViewById(R.id.openPriceList);
            highPrice = (TextView) itemView.findViewById(R.id.highPriceList);
            lowPrice = (TextView) itemView.findViewById(R.id.lowPriceList);
            shareAmount = (TextView) itemView.findViewById(R.id.shareAmount);
            difference = (TextView) itemView.findViewById(R.id.differenceList);
            graph = (GraphView) itemView.findViewById(R.id.graphList);

        }
    }
}
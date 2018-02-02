package com.tangstudios.infiniteloop.stockup;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.DecimalFormat;

public class StockList extends AppCompatActivity {
    TextView netWorthTextView;
    TextView accountBalanceTextView;
    TextView investedBalanceTextView;

    Button doneButton;

    RecyclerView recyclerView;
    public static Adapter mAdapter;

    Player player1;
    StockMarket stockMarket;

    DecimalFormat df;
    Intent getIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        df = new DecimalFormat("0.00");

        recyclerView = (RecyclerView) findViewById(R.id.stockListRecyclerView);
        netWorthTextView = (TextView) findViewById(R.id.netWorthList);
        accountBalanceTextView = (TextView) findViewById(R.id.accountBalanceList);
        investedBalanceTextView = (TextView) findViewById(R.id.investedBalanceList);
        doneButton = (Button) findViewById(R.id.doneButton);

        getIntent = getIntent();
        player1 = (Player) getIntent.getSerializableExtra("player1");
        netWorthTextView.setText("Net Worth: $" + df.format(player1.getBalance() + player1.getBalanceInStocks()));
        accountBalanceTextView.setText("Account Balance: $" + df.format(player1.getBalance()));
        investedBalanceTextView.setText("Invested Balance: $" + df.format(player1.getBalanceInStocks()));

        stockMarket = (StockMarket) getIntent.getSerializableExtra("stockMarket");

        mAdapter = new Adapter(this, stockMarket.getStocks(), player1);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemViewCacheSize(100);
        if (!getIntent.getBooleanExtra("isDuringDay", false)) {
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
                @Override
                public void OnClick(View view, int position) {
                    Intent intent = new Intent(StockList.this, StockView.class);
                    intent.putExtra("position", position);
                    intent.putExtra("player1", player1);
                    intent.putExtra("stockMarket", stockMarket);
                    startActivity(intent);
                }

                @Override
                public void onLongClick(View view, int position) {

                }

            }));
        } else {
            doneButton.setEnabled(false);
            mAdapter.notifyDataSetChanged();
            for (int i = 1; i < 25; i++) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stockMarket.update();
                        player1.update(stockMarket.getStocks());
                        netWorthTextView.setText("Net Worth: $" + df.format(player1.getBalance() + player1.getBalanceInStocks()));
                        accountBalanceTextView.setText("Account Balance: $" + df.format(player1.getBalance()));
                        investedBalanceTextView.setText("Invested Balance: $" + df.format(Math.round(player1.getBalanceInStocks())));
                        mAdapter.notifyDataSetChanged();
                    }
                }, 1000);

            }
            doneButton.setEnabled(true);
            doneButton.setText("End day");
        }

    }

    public void onDoneList(View view) {
        Intent intent = new Intent(this, Standings.class);
        if (!getIntent.getBooleanExtra("isDuringDay", false)) {
            intent.putExtra("waitingToStart", true);
            stockMarket.endDay();
        } else {
            intent.putExtra("waitingToStart", false);
        }
        intent.putExtra("player1", player1);
        intent.putExtra("stockMarket", stockMarket);
        startActivity(intent);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {

            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }


                @Override
                public void onLongPress(MotionEvent e) {

                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

                    if (child != null && clickListener != null) {

                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));

                    }

                }

            });

        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {

                clickListener.OnClick(child, rv.getChildPosition(child));

            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {

        }

    }

    public static interface ClickListener {

        public void OnClick(View view, int position);
        public void onLongClick(View view, int position);

    }

    @Override
    public void onBackPressed() {
    }
}

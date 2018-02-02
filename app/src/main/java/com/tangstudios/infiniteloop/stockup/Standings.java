package com.tangstudios.infiniteloop.stockup;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Standings extends AppCompatActivity {
    TextView standingsTextView;
    TextView daysTextView;
    TextView usernameTextView;
    TextView readyTextView;
    TextView netWorthTextView;
    TextView accountBalanceTextView;
    TextView investedBalanceTextView;
    Button readyButton;
    Button notReadyButton;
    Button startGameButton;

    Player player1;
    StockMarket stockMarket;
    boolean isWaiting;

    private int day;
    private int totalDays;
    private double balance;
    private String username;

    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        df = new DecimalFormat("0.00");

        standingsTextView = (TextView) findViewById(R.id.standingsTextView);
        daysTextView = (TextView) findViewById(R.id.daysStandingsTextView);
        usernameTextView = (TextView) findViewById(R.id.usernameStandings);
        readyTextView = (TextView) findViewById(R.id.readyStandingsTextView);
        netWorthTextView = (TextView) findViewById(R.id.netWorthStandings);
        accountBalanceTextView = (TextView) findViewById(R.id.accountBalanceStandings);
        investedBalanceTextView = (TextView) findViewById(R.id.investedBalanceStandings);
        readyButton = (Button) findViewById(R.id.readyButtonStandings);
        notReadyButton = (Button) findViewById(R.id.notReadyButtonStandings);
        startGameButton = (Button) findViewById(R.id.startGameButtonStandings);

        Intent intent = getIntent();
        player1 = (Player) intent.getSerializableExtra("player1");
        stockMarket = (StockMarket) intent.getSerializableExtra("stockMarket");
        totalDays = stockMarket.getTotalDays();
        balance = player1.getBalance();
        username = player1.getPlayerName();
        day = 0;
        daysTextView.setText("Day " + stockMarket.getDays() + "/" + totalDays);
        usernameTextView.setText(username);
        netWorthTextView.setText("Net Worth: $" + df.format(player1.getBalance() + player1.getBalanceInStocks()));
        accountBalanceTextView.setText("Account Balance: $" + df.format(player1.getBalance()));
        investedBalanceTextView.setText("Invested Balance: $" + df.format(player1.getBalanceInStocks()));

        isWaiting = intent.getBooleanExtra("waitingToStart", false);
        if (isWaiting) {
            player1.ready();
            standingsTextView.setText("Standings Before");
            readyTextView.setText("READY");
            readyTextView.setTextColor(Color.GREEN);
            updateButton();
            readyButton.setVisibility(View.INVISIBLE);
            notReadyButton.setVisibility(View.INVISIBLE);
            startGameButton.setText("Start Day");
        } else if (stockMarket.getDays() == stockMarket.getTotalDays()) {
            readyTextView.setVisibility(View.INVISIBLE);
            readyButton.setVisibility(View.INVISIBLE);
            notReadyButton.setVisibility(View.INVISIBLE);
            startGameButton.setText("Final Standings");
            startGameButton.setEnabled(true);
        }
    }

    public void onReady(View view) {
        player1.ready();
        readyTextView.setText("READY");
        readyTextView.setTextColor(Color.GREEN);
        updateButton();
    }

    public void onNotReady(View view) {
        player1.notReady();
        readyTextView.setText("NOT READY");
        readyTextView.setTextColor(Color.RED);
        updateButton();
    }

    public void onStartGame(View view) {
        if (stockMarket.getDays() == stockMarket.getTotalDays() && !isWaiting) {
            Intent intent = new Intent(this, FinalStandings.class);
            intent.putExtra("player1", player1);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, StockList.class);
            intent.putExtra("isDuringDay", isWaiting);
            intent.putExtra("player1", player1);
            intent.putExtra("stockMarket", stockMarket);
            startActivity(intent);
        }
    }

    private void updateButton() {
        if (player1.isReady()) {
            startGameButton.setText("Start Buying");
            startGameButton.setEnabled(true);
        } else {
            startGameButton.setText("Waiting for Players...");
            startGameButton.setEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
    }
}

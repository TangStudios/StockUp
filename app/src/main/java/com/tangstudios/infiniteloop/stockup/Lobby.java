package com.tangstudios.infiniteloop.stockup;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Lobby extends AppCompatActivity {
    String username;
    String lobbyName;
    TextView usernameTextView;
    TextView lobbyNameTextView;
    TextView readyTextView;
    TextView daysTextView;
    TextView balanceTextView;

    Button minusDaysButton;
    Button plusDaysButton;
    Button minusBalanceButton;
    Button plusBalanceButton;
    Button startGameButton;

    Player player1;
    StockMarket stockMarket;

    private int days;
    private int balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        days = 10;
        balance = 20000;

        usernameTextView = (TextView) findViewById(R.id.usernameLobby);
        lobbyNameTextView = (TextView) findViewById(R.id.lobbyNameLobby);
        readyTextView = (TextView) findViewById(R.id.readyTextView);
        daysTextView = (TextView) findViewById(R.id.daysTextView);
        balanceTextView = (TextView) findViewById(R.id.balanceTextView);
        minusDaysButton = (Button) findViewById(R.id.minusDaysButton);
        plusDaysButton = (Button) findViewById(R.id.plusDaysButton);
        minusBalanceButton = (Button) findViewById(R.id.minusBalanceButton);
        plusBalanceButton = (Button) findViewById(R.id.plusBalanceButton);
        startGameButton = (Button) findViewById(R.id.startGameButton);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        lobbyName = intent.getStringExtra("lobbyname");
        usernameTextView.setText("1. " + username);
        if (lobbyName.equals("")) {
            lobbyNameTextView.setText("Lobby");
        } else {
            lobbyNameTextView.setText(lobbyName);
        }

        player1 = new Player(username, balance);
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

    public void onPlusDays(View view) {
        days++;
        if (days > 100) {
            days = 100;
        }
        daysTextView.setText("" + days);
    }

    public void onMinusDays(View view) {
        days--;
        if (days < 5) {
            days = 5;
        }
        daysTextView.setText("" + days);
    }

    public void onPlusBalance(View view) {
        balance += 1000;
        if (balance > 100000) {
            balance = 100000;
        }
        balanceTextView.setText("$" + balance);
    }

    public void onMinusBalance(View view) {
        balance -= 1000;
        if (balance < 5000) {
            balance = 5000;
        }
        balanceTextView.setText("$" + balance);
    }

    public void onStartGame(View view) {
        Intent intent = new Intent(this, Standings.class);
        stockMarket = new StockMarket(days);
        player1 = new Player(username, balance);
        intent.putExtra("player1", player1);
        intent.putExtra("stockMarket", stockMarket);
        startActivity(intent);
    }

    private void updateButton() {
        if (player1.isReady()) {
            startGameButton.setText("Start Game");
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

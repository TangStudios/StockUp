package com.tangstudios.infiniteloop.stockup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

public class FinalStandings extends AppCompatActivity {
    TextView usernameTextView;
    TextView netWorthTextView;
    TextView accountBalanceTextView;
    TextView investedBalanceTextView;

    Player player1;

    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_standings);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        df = new DecimalFormat("0.00");

        usernameTextView = (TextView) findViewById(R.id.usernameFinalStandings);
        netWorthTextView = (TextView) findViewById(R.id.netWorthFinalStandings);
        accountBalanceTextView = (TextView) findViewById(R.id.accountBalanceFinalStandings);
        investedBalanceTextView = (TextView) findViewById(R.id.investedBalanceFinalStandings);

        Intent intent = getIntent();
        player1 = (Player) intent.getSerializableExtra("player1");

        usernameTextView.setText("1. " + player1.getPlayerName());
        netWorthTextView.setText("Net Worth: $" + df.format(player1.getBalance() + player1.getBalanceInStocks()));
        accountBalanceTextView.setText("Account Balance: $" + df.format(player1.getBalance()));
        investedBalanceTextView.setText("Invested Balance: $" + df.format(player1.getBalanceInStocks()));
    }

    public void toMainMenu(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}

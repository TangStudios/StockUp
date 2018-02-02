package com.tangstudios.infiniteloop.stockup;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView titleTextView;
    Button createLobbyButton;
    Button joinLobbyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        titleTextView = (TextView) findViewById(R.id.titleMain);
        createLobbyButton = (Button) findViewById(R.id.toCreateButton);
        joinLobbyButton = (Button) findViewById(R.id.joinButton);

        Typeface custom = Typeface.createFromAsset(getAssets(), "fonts/ALBAS___.TTF");
        titleTextView.setTypeface(custom);
    }

    public void onToCreateLobby(View view) {
        startActivity(new Intent(this, CreateLobby.class));
    }

    public void onJoinLobby(View view) {
        startActivity(new Intent(this, JoinLobby.class));
    }
}

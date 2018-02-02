package com.tangstudios.infiniteloop.stockup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateLobby extends AppCompatActivity {
    EditText usernameEdittext;
    EditText lobbyNameEdittext;
    Button createLobbyButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lobby);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        usernameEdittext = (EditText) findViewById(R.id.usernameEdittext);
        lobbyNameEdittext = (EditText) findViewById(R.id.lobbyNameEdittext);
        createLobbyButton = (Button) findViewById(R.id.createLobbyButton);
    }

    public void onCreateLobby(View view) {
        Intent intent = new Intent(this, Lobby.class);
        intent.putExtra("username", usernameEdittext.getText().toString());
        intent.putExtra("lobbyname", lobbyNameEdittext.getText().toString());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }
}

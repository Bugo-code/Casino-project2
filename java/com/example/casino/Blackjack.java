package com.example.casino;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Blackjack extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blackjack);

    }
    public void GoTOHome(View view)
    {
        startActivity(new Intent(this, Home.class));
    }
    public void HTP(View view)
    {
        startActivity(new Intent(this , RulesBj.class));
    }


}
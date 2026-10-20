package com.example.casino;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Home extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        name = findViewById(R.id.name);

        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String email = currentUser.getEmail();
            String username = email.split("@")[0];
            name.setText("Hello" + username);
        } else
            name.setText("Hello Guest");
    }
    @Override
    protected void onResume() {
        super.onResume();
        TextView txtCoins = findViewById(R.id.txtCoins);
        if (Coins.isGuest()) {
            txtCoins.setText("Coins: ∞");
        } else {
            txtCoins.setText("Coins: " + Coins.get(this));
        }
    }
    public void GoToSettings(View view) {

        startActivity(new Intent(this, Settings.class));
    }
    public void GoTOBlackjack(View view)
    {
        startActivity(new Intent(this, Blackjack.class));
    }
    public void GoToSlots(View view)
    {
        startActivity(new Intent(this, Slots.class));
    }

    public void GoToHL(View view)
    {
        startActivity(new Intent(this , HigherLower.class));
    }
}
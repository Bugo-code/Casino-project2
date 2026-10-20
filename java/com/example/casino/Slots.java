package com.example.casino;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Slots extends AppCompatActivity {

    android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
    String[] symbols = {"🍒", "🍋", "🔔", "⭐", "7"};
    java.util.Random random = new java.util.Random();
    boolean spinning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slots);

    }
    @Override
    protected void onResume() {
        super.onResume();
        showCoins();
    }

    public void showCoins() {
        TextView txtBalance = findViewById(R.id.txtBalance);
        if (Coins.isGuest()) {
            txtBalance.setText("Coins: ∞");
        } else {
            txtBalance.setText("Coins: " + Coins.get(this));
        }
    }
    public void Spin(View view) {
        EditText editBet = findViewById(R.id.editBet);
        String betText = editBet.getText().toString();

        if (betText.isEmpty()) {
            Toast.makeText(this, "Enter a bet", Toast.LENGTH_SHORT).show();
            return;
        }

        int bet = Integer.parseInt(betText);

        if (bet <= 0) {
            Toast.makeText(this, "The bet must be more than 0", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Coins.isGuest() && bet > Coins.get(this)) {
            Toast.makeText(this, "Not enough coins", Toast.LENGTH_SHORT).show();
            return;
        }
        spinning = true;

        TextView reel1 = findViewById(R.id.txtReel1);
        TextView reel2 = findViewById(R.id.txtReel2);
        TextView reel3 = findViewById(R.id.txtReel3);

        Button btnSpin = findViewById(R.id.btnSpin);
        btnSpin.setEnabled(false);

        spinReel(reel1, 15, null);
        spinReel(reel2, 25, null);
        spinReel(reel3, 35, () -> finishSpin(bet));

    }
    public void spinReel(TextView reel, int ticksLeft, Runnable whenDone) {
        reel.setText(symbols[random.nextInt(symbols.length)]);

        if (ticksLeft <= 1) {
            if (whenDone != null) {
                whenDone.run();
            }
            return;
        }

        int delay = 40;
        if (ticksLeft <= 5) {
            delay = 40 + (6 - ticksLeft) * 40;
        }

        handler.postDelayed(() -> spinReel(reel, ticksLeft - 1, whenDone), delay);
    }
    public void finishSpin(int bet) {
        TextView reel1 = findViewById(R.id.txtReel1);
        TextView reel2 = findViewById(R.id.txtReel2);
        TextView reel3 = findViewById(R.id.txtReel3);
        EditText editBet = findViewById(R.id.editBet);

        String s1 = reel1.getText().toString();
        String s2 = reel2.getText().toString();
        String s3 = reel3.getText().toString();

        TextView result = findViewById(R.id.txtResult);
        int change;

        if (s1.equals(s2) && s2.equals(s3)) {
            change = bet * 4;
            result.setText("Congrats! x5");
        } else if (s1.equals(s2) || s2.equals(s3) || s1.equals(s3)) {
            change = 0;
            result.setText("Almost! Bet returned");
        } else {
            change = -bet;
            result.setText("Try again");
        }

        if (!Coins.isGuest()) {
            Coins.set(this, Coins.get(this) + change);
        }

        showCoins();
        editBet.setText("");

        Button btnSpin = findViewById(R.id.btnSpin);
        btnSpin.setEnabled(true);

        spinning = false;
    }
    public void GoTOHome(View view)
    {
        startActivity(new Intent(this, Home.class));
    }
    public void HTP(View view)
    {
        startActivity(new Intent(this , RulesSlots.class));
    }

    public void Start(View view) {
        findViewById(R.id.btnHome).setVisibility(View.GONE);
        findViewById(R.id.btnHowToPlay).setVisibility(View.GONE);
        findViewById(R.id.btnStart).setVisibility(View.GONE);

        findViewById(R.id.btnLeave).setVisibility(View.VISIBLE);
        findViewById(R.id.editBet).setVisibility(View.VISIBLE);
        findViewById(R.id.btnSpin).setVisibility(View.VISIBLE);

        TextView txtResult = findViewById(R.id.txtResult);
        txtResult.setText("");

        findViewById(R.id.layoutReels).animate().scaleX(1.1f).scaleY(1.1f).setDuration(400);

        Toast.makeText(this, "Enter a bet and press SPIN. Press LEAVE to exit.", Toast.LENGTH_LONG).show();
    }

    public void Leave(View view) {
        if (spinning) {
            Toast.makeText(this, "Wait for the spin to finish", Toast.LENGTH_SHORT).show();
            return;
        }

        findViewById(R.id.btnHome).setVisibility(View.VISIBLE);
        findViewById(R.id.btnHowToPlay).setVisibility(View.VISIBLE);
        findViewById(R.id.btnStart).setVisibility(View.VISIBLE);

        findViewById(R.id.btnLeave).setVisibility(View.GONE);
        findViewById(R.id.editBet).setVisibility(View.GONE);
        findViewById(R.id.btnSpin).setVisibility(View.GONE);

        findViewById(R.id.layoutReels).animate().scaleX(1f).scaleY(1f).setDuration(400);
    }


}
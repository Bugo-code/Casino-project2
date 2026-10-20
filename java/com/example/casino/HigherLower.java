package com.example.casino;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HigherLower extends AppCompatActivity {

    java.util.Random random = new java.util.Random();
    String[] suits = {"♠", "♥", "♦", "♣"};
    int currentRank = 0;
    int bet;
    int streak;
    boolean roundActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_higher_lower);
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

    public String rankName(int rank) {
        if (rank == 11) return "J";
        if (rank == 12) return "Q";
        if (rank == 13) return "K";
        if (rank == 14) return "A";
        return String.valueOf(rank);
    }

    public void drawCard() {
        int newRank = random.nextInt(13) + 2;
        while (newRank == currentRank) {
            newRank = random.nextInt(13) + 2;
        }
        currentRank = newRank;

        int suit = random.nextInt(4);
        TextView card = findViewById(R.id.txtCard);
        card.setText(rankName(currentRank) + "\n" + suits[suit]);

        if (suit == 1 || suit == 2) {
            card.setTextColor(Color.parseColor("#FF4D4D"));
        } else {
            card.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    public double getMultiplier(int streak) {
        if (streak == 1) return 1.2;
        if (streak == 2) return 1.5;
        if (streak == 3) return 2;
        if (streak == 4) return 3;
        if (streak == 5) return 5;
        return 10;
    }

    public void Start(View view) {
        roundActive = false;
        streak = 0;
        currentRank = 0;
        drawCard();

        TextView txtStreak = findViewById(R.id.txtStreak);
        TextView txtResult = findViewById(R.id.txtResult);
        txtStreak.setText("Streak: 0     Prize: 0");
        txtResult.setText("");

        findViewById(R.id.btnHome).setVisibility(View.GONE);
        findViewById(R.id.btnHowToPlay).setVisibility(View.GONE);
        findViewById(R.id.btnStart).setVisibility(View.GONE);

        findViewById(R.id.editBet).setVisibility(View.VISIBLE);
        findViewById(R.id.btnLeave).setVisibility(View.VISIBLE);
        findViewById(R.id.txtStreak).setVisibility(View.VISIBLE);
        findViewById(R.id.btnHigher).setVisibility(View.VISIBLE);
        findViewById(R.id.btnLower).setVisibility(View.VISIBLE);
        findViewById(R.id.btnCashOut).setVisibility(View.VISIBLE);
        findViewById(R.id.btnCashOut).setEnabled(false);

        findViewById(R.id.txtCard).animate().scaleX(1.2f).scaleY(1.2f).setDuration(400);
    }

    public void Leave(View view) {
        if (roundActive) {
            Toast.makeText(this, "Finish the round first: guess or cash out", Toast.LENGTH_SHORT).show();
            return;
        }

        findViewById(R.id.btnHome).setVisibility(View.VISIBLE);
        findViewById(R.id.btnHowToPlay).setVisibility(View.VISIBLE);
        findViewById(R.id.btnStart).setVisibility(View.VISIBLE);

        findViewById(R.id.editBet).setVisibility(View.GONE);
        findViewById(R.id.btnLeave).setVisibility(View.GONE);
        findViewById(R.id.txtStreak).setVisibility(View.INVISIBLE);
        findViewById(R.id.btnHigher).setVisibility(View.GONE);
        findViewById(R.id.btnLower).setVisibility(View.GONE);
        findViewById(R.id.btnCashOut).setVisibility(View.GONE);

        findViewById(R.id.txtCard).animate().scaleX(1f).scaleY(1f).setDuration(400);
    }

    public boolean startRound() {
        EditText editBet = findViewById(R.id.editBet);
        String betText = editBet.getText().toString();

        if (betText.isEmpty()) {
            Toast.makeText(this, "Enter a bet", Toast.LENGTH_SHORT).show();
            return false;
        }

        bet = Integer.parseInt(betText);

        if (bet <= 0) {
            Toast.makeText(this, "The bet must be more than 0", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!Coins.isGuest() && bet > Coins.get(this)) {
            Toast.makeText(this, "Not enough coins", Toast.LENGTH_SHORT).show();
            return false;
        }

        roundActive = true;
        streak = 0;

        TextView txtStreak = findViewById(R.id.txtStreak);
        TextView txtResult = findViewById(R.id.txtResult);
        txtStreak.setText("Streak: 0     Prize: 0");
        txtResult.setText("");

        editBet.setEnabled(false);
        return true;
    }

    public void endRound() {
        roundActive = false;

        EditText editBet = findViewById(R.id.editBet);
        editBet.setEnabled(true);
        editBet.setText("");

        findViewById(R.id.btnCashOut).setEnabled(false);
    }

    public void Higher(View view) {
        guess(true);
    }

    public void Lower(View view) {
        guess(false);
    }

    public void guess(boolean guessedHigher) {
        if (!roundActive) {
            boolean ok = startRound();
            if (!ok) {
                return;
            }
        }

        int oldRank = currentRank;
        drawCard();
        boolean isHigher = currentRank > oldRank;

        TextView txtStreak = findViewById(R.id.txtStreak);
        TextView txtResult = findViewById(R.id.txtResult);

        if (isHigher == guessedHigher) {
            streak++;
            int prize = (int) (bet * getMultiplier(streak));
            txtStreak.setText("Streak: " + streak + "     Prize: " + prize);
            txtResult.setText("Correct!");
            findViewById(R.id.btnCashOut).setEnabled(true);
        } else {
            if (!Coins.isGuest()) {
                Coins.set(this, Coins.get(this) - bet);
            }
            showCoins();
            txtResult.setText("Wrong! You lost " + bet);
            endRound();
        }
    }
    public void CashOut(View view) {
        int prize = (int) (bet * getMultiplier(streak));
        int profit = prize - bet;

        if (!Coins.isGuest()) {
            Coins.set(this, Coins.get(this) + profit);
        }
        showCoins();

        TextView txtResult = findViewById(R.id.txtResult);
        txtResult.setText("You won " + profit + " coins!");

        endRound();
    }

    public void GoTOHome(View view)
    {
        startActivity(new Intent(this, Home.class));
    }
    public void HTP(View view)
    {
        startActivity(new Intent(this, RulesHigherLower.class));
    }
}
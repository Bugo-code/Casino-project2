package com.example.casino;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Coins {

    public static boolean isGuest() {
        return FirebaseAuth.getInstance().getCurrentUser() == null;
    }

    public static int get(Context context) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return 0;
        }
        SharedPreferences prefs = context.getSharedPreferences("casino", Context.MODE_PRIVATE);
        return prefs.getInt("coins_" + user.getUid(), 1000);
    }

    public static void set(Context context, int amount) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        SharedPreferences prefs = context.getSharedPreferences("casino", Context.MODE_PRIVATE);
        prefs.edit().putInt("coins_" + user.getUid(), amount).apply();
    }
}
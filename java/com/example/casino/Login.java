package com.example.casino;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    FirebaseAuth auth;
    EditText etEmail, etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }
    public void init()
    {
        auth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.editEmail);
        etPassword = findViewById(R.id.editPassword);
    }
    public void GoMain(View view)
    {
        startActivity(new Intent(this, MainActivity.class));
    }
    public void GoTOHome(View view)
    {
        startActivity(new Intent(this, Home.class));
    }
    public void login(View view) {
        //חיבור משתמש קיים

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 1. בדיקת תקינות אימייל (שאינו ריק ושהפורמט תקין)
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("נא להזין אימייל");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("כתובת האימייל אינה תקינה");
            etEmail.requestFocus();
            return;
        }

        // 2. בדיקת תקינות סיסמה
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("נא להזין סיסמה");
            etPassword.requestFocus();
            return;
        }

        // 3. ביצוע ההתחברות מול Firebase
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // ההתחברות הצליחה!
                        Toast.makeText(this, "התחברת בהצלחה!", Toast.LENGTH_SHORT).show();
                        GoTOHome(view);
                        // מעבר למסך הראשי
                        // Intent intent = new Intent(this, MainActivity.class);
                        // startActivity(intent);
                        // finish();
                    } else {
                        // ההתחברות נכשלה (למשל: סיסמה שגויה או משתמש לא קיים)
                        String errorMessage = task.getException() != null ?
                                task.getException().getMessage() : "שגיאה בהתחברות";

                        Toast.makeText(this, "התחברות נכשלה: האימייל או הסיסמה שגויים", Toast.LENGTH_LONG).show();                    }
                });
    }
    public void signup(View view) {
        //רישום משתמש חדש

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // בדיקות תקינות קלט
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("נא להזין אימייל");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("כתובת האימייל אינה תקינה");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("נא להזין סיסמה");
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("הסיסמה חייבת להכיל לפחות 6 תווים");
            return;
        }

        // ביצוע הרשמה ב-Firebase
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // ההרשמה הצליחה!
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(this, "ההרשמה בוצעה בהצלחה!", Toast.LENGTH_SHORT).show();

                        // העברה למסך הראשי
                        startActivity(new Intent(this, Home.class));
                    } else {
                        // ארעה שגיאה (למשל: המייל כבר קיים במערכת)
                        String error = task.getException() != null ? task.getException().getMessage() : "שגיאה בחיבור";
                        Toast.makeText(this, "הרשמה נכשלה: " + error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean isValidEmail(CharSequence email) {
        //פעולה הבודקת אם המייל נכתב בתבנית חוקית
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}
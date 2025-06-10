package com.example.drinkingwaterapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class EnterCodeActivity extends AppCompatActivity {

    private TextInputEditText codeEditText;
    private String userId, correctCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_code);

        // Получаем данные из Intent
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        correctCode = intent.getStringExtra("code");

        codeEditText = findViewById(R.id.codeEditText);
        Button verifyButton = findViewById(R.id.verifyButton);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Введите код");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        verifyButton.setOnClickListener(v -> {
            String enteredCode = codeEditText.getText().toString().trim();

            if (enteredCode.isEmpty()) {
                Toast.makeText(this, "Введите код", Toast.LENGTH_SHORT).show();
                return;
            }

            if (enteredCode.length() != 6) {
                Toast.makeText(this, "Код должен содержать 6 цифр", Toast.LENGTH_SHORT).show();
                return;
            }

            if (enteredCode.equals(correctCode)) {
                goToUpdatePasswordActivity();
            } else {
                Toast.makeText(this, "Неверный код", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToUpdatePasswordActivity() {
        Intent intent = new Intent(this, UpdatePasswordActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
        finish();
    }
}
package com.example.drinkingwaterapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdatePasswordActivity extends AppCompatActivity {

    private TextInputEditText newPasswordEditText, confirmPasswordEditText;
    private Button updatePasswordButton;
    private DatabaseReference mDatabase;
    private Toolbar toolbar;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        // Получаем данные из Intent
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        String email = intent.getStringExtra("email");

        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        updatePasswordButton = findViewById(R.id.updatePasswordButton);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Смена пароля");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        updatePasswordButton.setOnClickListener(v -> {
            String newPassword = newPasswordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            if (validatePasswords(newPassword, confirmPassword)) {
                updatePasswordInDatabase(userId, newPassword);
            }
        });
    }

    private boolean validatePasswords(String newPassword, String confirmPassword) {
        if (newPassword.isEmpty()) {
            newPasswordEditText.setError("Введите новый пароль");
            return false;
        }

        if (newPassword.length() < 6) {
            newPasswordEditText.setError("Пароль должен содержать минимум 6 символов");
            return false;
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("Подтвердите пароль");
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Пароли не совпадают");
            return false;
        }

        return true;
    }

    private void updatePasswordInDatabase(String userId, String newPassword) {
        mDatabase.child(userId).child("password").setValue(newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Пароль успешно изменен", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Ошибка при изменении пароля", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
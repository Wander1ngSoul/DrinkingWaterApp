package com.example.drinkingwaterapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResetPasswordActivity extends AppCompatActivity {
    private TextInputEditText emailEditText;
    private Button sendCodeButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        emailEditText = findViewById(R.id.emailEditText);
        sendCodeButton = findViewById(R.id.sendCodeButton);

        sendCodeButton.setOnClickListener(v -> {
            userEmail = emailEditText.getText().toString().trim();
            if (userEmail.isEmpty()) {
                Toast.makeText(this, "Введите email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Проверяем, есть ли пользователь с таким email
            mAuth.fetchSignInMethodsForEmail(userEmail)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            sendPasswordResetEmail(userEmail);
                        } else {
                            Toast.makeText(this, "Пользователь не найден", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void sendPasswordResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Ссылка для сброса отправлена", Toast.LENGTH_LONG).show();
                        setupAuthStateListener(email); // Отслеживаем вход после сброса

                        // Переходим на экран входа
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Ошибка: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setupAuthStateListener(String email) {
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && user.getEmail().equalsIgnoreCase(email)) {
                    // Пользователь вошел после сброса пароля
                    updatePasswordInDatabase(user.getUid(), "новый_пароль"); // ⚠️ Замените на реальный пароль
                    mAuth.removeAuthStateListener(this);
                }
            }
        });
    }

    private void updatePasswordInDatabase(String userId, String newPassword) {
        mDatabase.child("users").child(userId).child("password")
                .setValue(newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Пароль обновлен в базе", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Ошибка обновления пароля", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
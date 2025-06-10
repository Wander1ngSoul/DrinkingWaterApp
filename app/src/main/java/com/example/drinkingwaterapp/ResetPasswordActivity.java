package com.example.drinkingwaterapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputEditText emailEditText;
    private Button sendCodeButton;
    private DatabaseReference mDatabase;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        emailEditText = findViewById(R.id.emailEditText);
        sendCodeButton = findViewById(R.id.sendResetLinkButton);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Восстановление пароля");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        sendCodeButton.setOnClickListener(v -> {
            String userEmail = emailEditText.getText().toString().trim();

            if (userEmail.isEmpty()) {
                Toast.makeText(this, "Введите email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Проверяем существование пользователя в базе данных
            checkUserExists(userEmail);
        });
    }

    private void checkUserExists(String email) {
        Query query = mDatabase.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Пользователь найден, переходим к смене пароля
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String userId = snapshot.getKey();
                        goToUpdatePasswordActivity(userId, email);
                    }
                } else {
                    Toast.makeText(ResetPasswordActivity.this,
                            "Пользователь с таким email не найден", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ResetPasswordActivity.this,
                        "Ошибка базы данных: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToUpdatePasswordActivity(String userId, String email) {
        Intent intent = new Intent(this, UpdatePasswordActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }
}
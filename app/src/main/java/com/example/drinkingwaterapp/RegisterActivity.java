package com.example.drinkingwaterapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText loginEditText;
    private EditText passwordEditText;
    private Button registerButton;

    private final String USER_KEY = "Users";
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        mDatabase = FirebaseDatabase.getInstance().getReference(USER_KEY);

        registerButton = findViewById(R.id.registerButton);
        loginEditText = findViewById(R.id.loginEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        registerButton.setOnClickListener(v -> {
            String registerLogin = loginEditText.getText().toString();
            String registerPassword = passwordEditText.getText().toString();

            if (registerLogin.isEmpty() || registerPassword.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            String id = mDatabase.push().getKey();
            User user = new User(registerLogin, registerPassword, id);

            if (id != null) {
                mDatabase.child(id).setValue(user)
                        .addOnSuccessListener(aVoid ->
                                Toast.makeText(RegisterActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e ->
                                Toast.makeText(RegisterActivity.this, "Ошибка регистрации: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
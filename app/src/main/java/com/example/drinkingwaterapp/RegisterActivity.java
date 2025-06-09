package com.example.drinkingwaterapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText firstNameEditText, lastNameEditText, patronymicEditText;
    private TextInputEditText emailEditText, phoneEditText, passwordEditText;
    private Button registerButton, goToLoginButton;

    private final String USER_KEY = "Users";
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDatabase = FirebaseDatabase.getInstance().getReference(USER_KEY);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        patronymicEditText = findViewById(R.id.patronymicEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        goToLoginButton = findViewById(R.id.goToLoginButton);

        registerButton.setOnClickListener(v -> {
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String patronymic = patronymicEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (!validateInputs(firstName, lastName, email, phone, password)) {
                return;
            }

            String id = mDatabase.push().getKey();
            User user = new User(firstName, lastName, patronymic, email, phone, password, id);

            if (id != null) {
                mDatabase.child(id).setValue(user)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(RegisterActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(RegisterActivity.this, "Ошибка регистрации: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

        goToLoginButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private boolean validateInputs(String firstName, String lastName, String email, String phone, String password) {
        // Validate first name
        if (TextUtils.isEmpty(firstName)) {
            firstNameEditText.setError("Введите имя");
            firstNameEditText.requestFocus();
            return false;
        } else if (!firstName.matches("[а-яА-ЯёЁa-zA-Z-]+")) {
            firstNameEditText.setError("Имя может содержать только буквы и дефис");
            firstNameEditText.requestFocus();
            return false;
        }

        // Validate last name
        if (TextUtils.isEmpty(lastName)) {
            lastNameEditText.setError("Введите фамилию");
            lastNameEditText.requestFocus();
            return false;
        } else if (!lastName.matches("[а-яА-ЯёЁa-zA-Z-]+")) {
            lastNameEditText.setError("Фамилия может содержать только буквы и дефис");
            lastNameEditText.requestFocus();
            return false;
        }

        // Validate email
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Введите email");
            emailEditText.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Введите корректный email");
            emailEditText.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(phone)) {
            phoneEditText.setError("Введите номер телефона");
            phoneEditText.requestFocus();
            return false;
        } else if (!Pattern.matches("^\\+?[0-9]{11,15}$", phone)) {
            phoneEditText.setError("Введите корректный номер телефона");
            phoneEditText.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Введите пароль");
            passwordEditText.requestFocus();
            return false;
        } else if (password.length() < 6) {
            passwordEditText.setError("Пароль должен содержать минимум 6 символов");
            passwordEditText.requestFocus();
            return false;
        }

        return true;
    }
}
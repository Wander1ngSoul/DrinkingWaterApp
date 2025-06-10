package com.example.drinkingwaterapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText firstNameEditText, lastNameEditText, patronymicEditText;
    private TextInputEditText emailEditText, phoneEditText, passwordEditText, passwordConfirmEditText;
    private Button registerButton, goToLoginButton;

    private final String USER_KEY = "Users";
    private DatabaseReference mDatabase;

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Zа-яА-ЯёЁ\\- ]+$");

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
        passwordConfirmEditText = findViewById(R.id.passwordConfirmEditText);
        registerButton = findViewById(R.id.registerButton);
        goToLoginButton = findViewById(R.id.goToLoginButton);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Регистрация");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        registerButton.setOnClickListener(v -> {
            if (validateInputs()) {
                registerUser();
            }
        });

        goToLoginButton.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }



    private boolean validateInputs() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String patronymic = patronymicEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String passwordConfirm = passwordConfirmEditText.getText().toString();

        if (firstName.isEmpty()) {
            firstNameEditText.setError("Введите имя");
            firstNameEditText.requestFocus();
            return false;
        }
        if (firstName.length() > 20) {
            firstNameEditText.setError("Имя не должно превышать 20 символов");
            firstNameEditText.requestFocus();
            return false;
        }
        if (!NAME_PATTERN.matcher(firstName).matches()) {
            firstNameEditText.setError("Имя должно содержать только буквы");
            firstNameEditText.requestFocus();
            return false;
        }

        if (lastName.isEmpty()) {
            lastNameEditText.setError("Введите фамилию");
            lastNameEditText.requestFocus();
            return false;
        }
        if (lastName.length() > 20) {
            lastNameEditText.setError("Фамилия не должна превышать 20 символов");
            lastNameEditText.requestFocus();
            return false;
        }
        if (!NAME_PATTERN.matcher(lastName).matches()) {
            lastNameEditText.setError("Фамилия должна содержать только буквы");
            lastNameEditText.requestFocus();
            return false;
        }

        if (!patronymic.isEmpty()) {
            if (patronymic.length() > 20) {
                patronymicEditText.setError("Отчество не должно превышать 20 символов");
                patronymicEditText.requestFocus();
                return false;
            }
            if (!NAME_PATTERN.matcher(patronymic).matches()) {
                patronymicEditText.setError("Отчество должно содержать только буквы");
                patronymicEditText.requestFocus();
                return false;
            }
        }

        if (email.isEmpty()) {
            emailEditText.setError("Введите email");
            emailEditText.requestFocus();
            return false;
        }
        if (email.length() > 75) {
            emailEditText.setError("Email не должен превышать 75 символов");
            emailEditText.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Введите корректный email");
            emailEditText.requestFocus();
            return false;
        }

        if (phone.isEmpty()) {
            phoneEditText.setError("Введите номер телефона");
            phoneEditText.requestFocus();
            return false;
        }
        if (phone.length() != 11) {
            phoneEditText.setError("Номер телефона должен содержать 11 цифр");
            phoneEditText.requestFocus();
            return false;
        }
        if (!phone.matches("\\d{11}")) {
            phoneEditText.setError("Номер телефона должен содержать только цифры");
            phoneEditText.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Введите пароль");
            passwordEditText.requestFocus();
            return false;
        }
        if (password.length() > 50) {
            passwordEditText.setError("Пароль не должен превышать 50 символов");
            passwordEditText.requestFocus();
            return false;
        }
        if (passwordConfirm.isEmpty()) {
            passwordConfirmEditText.setError("Подтвердите пароль");
            passwordConfirmEditText.requestFocus();
            return false;
        }
        if (!password.equals(passwordConfirm)) {
            passwordConfirmEditText.setError("Пароли не совпадают");
            passwordConfirmEditText.requestFocus();
            return false;
        }

        return true;
    }

    private void registerUser() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String patronymic = patronymicEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        User user = new User(firstName, lastName, patronymic, email, phone, password);

        String userId = mDatabase.push().getKey();

        if (userId == null) {
            Toast.makeText(this, "Ошибка регистрации. Попробуйте снова.", Toast.LENGTH_SHORT).show();
            return;
        }

        mDatabase.child(userId).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity.this, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this, "Ошибка регистрации: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}

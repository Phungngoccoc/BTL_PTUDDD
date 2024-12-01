package com.example.necomovie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {
    private ConstraintLayout parentLayout;
    EditText usernameEdt;
    EditText emailEdt;
    EditText passwordEdt;
    ImageButton showingPasswordBtn;
    EditText confirmPasswordEdt;
    ImageButton showingConfirmPasswordBtn;
    CheckBox checkBox;
    Button signUpBtn;
    TextView signInTv;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        parentLayout = findViewById(R.id.parentLayout);
        usernameEdt = findViewById(R.id.usernameEdt);
        emailEdt = (EditText) findViewById(R.id.emailEdt);
        passwordEdt = (EditText) findViewById(R.id.passwordEdt);
        showingPasswordBtn = (ImageButton) findViewById(R.id.showingPasswordBtn);
        confirmPasswordEdt = (EditText) findViewById(R.id.confirmPasswordEdt);
        showingConfirmPasswordBtn = (ImageButton) findViewById(R.id.showingConfirmPasswordBtn);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        signInTv = (TextView) findViewById(R.id.signInTv);
        progressBar = findViewById(R.id.progressBar);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
        signInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
            }
        });
        showingPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showingPasswordBtn.setSelected(!showingPasswordBtn.isSelected());
                if (showingPasswordBtn.isSelected()) {
                    showingPasswordBtn.setImageResource(R.drawable.hide_password_icon);
                    passwordEdt.setTransformationMethod(null);
                    passwordEdt.setSelection(passwordEdt.length());
                    return;
                }
                showingPasswordBtn.setImageResource(R.drawable.showing_password_icon);
                passwordEdt.setTransformationMethod(new PasswordTransformationMethod());
                passwordEdt.setSelection(passwordEdt.length());
            }
        });

        showingConfirmPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showingConfirmPasswordBtn.setSelected(!showingConfirmPasswordBtn.isSelected());
                if (showingConfirmPasswordBtn.isSelected()) {
                    showingConfirmPasswordBtn.setImageResource(R.drawable.hide_password_icon);
                    confirmPasswordEdt.setTransformationMethod(null);
                    confirmPasswordEdt.setSelection(confirmPasswordEdt.length());
                    return;
                }
                showingConfirmPasswordBtn.setImageResource(R.drawable.showing_password_icon);
                confirmPasswordEdt.setTransformationMethod(new PasswordTransformationMethod());
                confirmPasswordEdt.setSelection(confirmPasswordEdt.length());
            }
        });
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void signUp() {
        String username = usernameEdt.getText().toString();
        String email = emailEdt.getText().toString();
        String password = passwordEdt.getText().toString();
        String confirmPassword = confirmPasswordEdt.getText().toString();
        String passwordRegex = "^.{8,}$";
        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}";

        if (username.isEmpty()) {
            Toast.makeText(this, "Please enter username!", Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Please enter email!", Toast.LENGTH_SHORT).show();
        } else if (!email.matches(emailRegex)) {
            Toast.makeText(this, "Please enter valid email address!", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Please enter password!", Toast.LENGTH_SHORT).show();
        } else if (!password.matches(passwordRegex)) {
            Toast.makeText(this, "Password have at least 8 characters!", Toast.LENGTH_SHORT).show();
        } else if (confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please enter confirm password!", Toast.LENGTH_SHORT).show();
        } else if (!confirmPassword.equals(password)) {
            Toast.makeText(this, "Confirm password incorrect!", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (task.isSuccessful()) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                .build();
                        task.getResult().getUser().updateProfile(profileUpdates);
                        Toast.makeText(SignUpActivity.this, "Sign up successfully. Please go back to sign in screen", Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            Toast.makeText(SignUpActivity.this, "This email already used!", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseNetworkException e) {
                            Toast.makeText(SignUpActivity.this, "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(SignUpActivity.this, "Authentication failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}
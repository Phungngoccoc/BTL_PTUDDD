package com.example.necomovie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private ConstraintLayout parentLayout;
    private EditText emailEdt;
    private EditText passwordEdt;
    private ImageButton showingPasswordBtn;
    private CheckBox rememberMeCB;
    private TextView forgotPasswordTv;
    private Button signInBtn;
    private TextView signUpTv;
    FrameLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);
        parentLayout = findViewById(R.id.parentLayout);
        emailEdt = findViewById(R.id.emailEdt);
        passwordEdt = findViewById(R.id.passwordEdt);
        showingPasswordBtn = findViewById(R.id.showingPasswordBtn);
        rememberMeCB = findViewById(R.id.checkBox);
        forgotPasswordTv = findViewById(R.id.forgotPasswordTv);
        signInBtn = findViewById(R.id.signInBtn);
        signUpTv = findViewById(R.id.signUpTv);
        progressBar = findViewById(R.id.progressBar);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
            }
        });
        forgotPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
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
    }

    private void signIn() {
        hideSoftKeyboard();
        String email = emailEdt.getText().toString();
        String password = passwordEdt.getText().toString();
//        String email = "dtnbdlkm2@gmail.com";
//        String password = "12345678";

        if (email.isEmpty()) {
            Toast.makeText(SignInActivity.this, "Please enter email!", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(SignInActivity.this, "Please enter password!", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            Toast.makeText(SignInActivity.this, "User not found. Please register.", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            Toast.makeText(SignInActivity.this, "Invalid password. Please try again.", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseNetworkException e) {
                            Toast.makeText(SignInActivity.this, "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(SignInActivity.this, "Authentication failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }



    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
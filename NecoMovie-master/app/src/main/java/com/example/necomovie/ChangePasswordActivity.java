package com.example.necomovie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

public class ChangePasswordActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    EditText currentPasswordEdt;
    EditText newPasswordEdt;
    ImageButton backBtn;
    EditText confirmNewPasswordEdt;
    ImageButton showCurrentPasswordBtn;
    ImageButton showNewPasswordBtn;
    ImageButton showConfirmNewPasswordBtn;
    Button saveBtn;
    TextView toolbarTextView;
    ConstraintLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        parentLayout = findViewById(R.id.parentLayout);
        toolbar = findViewById(R.id.toolbar);
        backBtn = findViewById(R.id.backButton);
        currentPasswordEdt = findViewById(R.id.currentPasswordEdt);
        newPasswordEdt = findViewById(R.id.newPasswordEdt);
        toolbarTextView = findViewById(R.id.activityName);
        confirmNewPasswordEdt = findViewById(R.id.confirmNewPasswordEdt);
        showCurrentPasswordBtn = findViewById(R.id.showingCurrentPasswordBtn);
        showNewPasswordBtn = findViewById(R.id.showingNewPasswordBtn);
        showConfirmNewPasswordBtn = findViewById(R.id.showingConfirmNewPasswordBtn);
        saveBtn = findViewById(R.id.savePasswordBtn);
        toolbarTextView.setText("Change password");
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });
        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        showCurrentPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCurrentPasswordBtn.setSelected(!showCurrentPasswordBtn.isSelected());
                if (showCurrentPasswordBtn.isSelected()) {
                    showCurrentPasswordBtn.setImageResource(R.drawable.hide_password_icon);
                    currentPasswordEdt.setTransformationMethod(null);
                    currentPasswordEdt.setSelection(currentPasswordEdt.length());
                    return;
                }
                showCurrentPasswordBtn.setImageResource(R.drawable.showing_password_icon);
                currentPasswordEdt.setTransformationMethod(new PasswordTransformationMethod());
                currentPasswordEdt.setSelection(currentPasswordEdt.length());
            }
        });
        showNewPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewPasswordBtn.setSelected(!showNewPasswordBtn.isSelected());
                if (showNewPasswordBtn.isSelected()) {
                    showNewPasswordBtn.setImageResource(R.drawable.hide_password_icon);
                    newPasswordEdt.setTransformationMethod(null);
                    newPasswordEdt.setSelection(newPasswordEdt.length());
                    return;
                }
                showNewPasswordBtn.setImageResource(R.drawable.showing_password_icon);
                newPasswordEdt.setTransformationMethod(new PasswordTransformationMethod());
                newPasswordEdt.setSelection(newPasswordEdt.length());
            }
        });
        showConfirmNewPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmNewPasswordBtn.setSelected(!showConfirmNewPasswordBtn.isSelected());
                if (showConfirmNewPasswordBtn.isSelected()) {
                    showConfirmNewPasswordBtn.setImageResource(R.drawable.hide_password_icon);
                    confirmNewPasswordEdt.setTransformationMethod(null);
                    confirmNewPasswordEdt.setSelection(confirmNewPasswordEdt.length());
                    return;
                }
                showConfirmNewPasswordBtn.setImageResource(R.drawable.showing_password_icon);
                confirmNewPasswordEdt.setTransformationMethod(new PasswordTransformationMethod());
                confirmNewPasswordEdt.setSelection(confirmNewPasswordEdt.length());
            }
        });

    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    void changePassword(){
        String currentPassword = currentPasswordEdt.getText().toString();
        String newPassword = newPasswordEdt.getText().toString();
        String confirmNewPassword = confirmNewPasswordEdt.getText().toString();
        String passwordRegex = "^.{8,}$";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (currentPassword.isEmpty()) {
            Toast.makeText(this, "Please enter current password!", Toast.LENGTH_SHORT).show();
        } else if (newPassword.isEmpty()) {
            Toast.makeText(this, "Please enter new password!", Toast.LENGTH_SHORT).show();
        } else if (!newPassword.matches(passwordRegex)) {
            Toast.makeText(this, "New password must have at least 8 characters!!", Toast.LENGTH_SHORT).show();
        } else if (confirmNewPassword.isEmpty()) {
            Toast.makeText(this, "Please enter confirm new password!", Toast.LENGTH_SHORT).show();
        } else if (!confirmNewPassword.equals(newPassword)) {
            Toast.makeText(this, "Confirm new password incorrect!", Toast.LENGTH_SHORT).show();
        } else {
            user.reauthenticate(EmailAuthProvider.getCredential(user.getEmail(), currentPassword)).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        user.updatePassword(newPassword);
                        Toast.makeText(ChangePasswordActivity.this, "Change password successfully!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            Toast.makeText(ChangePasswordActivity.this, "User not found. Please register.", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            Toast.makeText(ChangePasswordActivity.this, "Invalid password. Please try again.", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseNetworkException e) {
                            Toast.makeText(ChangePasswordActivity.this, "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(ChangePasswordActivity.this, "Authentication failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}
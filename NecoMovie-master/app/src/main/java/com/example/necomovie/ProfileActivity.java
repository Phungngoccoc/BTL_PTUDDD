package com.example.necomovie;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    ImageButton backBtn;
    Button logoutBtn;
    TextView toolbarTextView;
    TextView usernameTv;
    ImageView avatarIv;
    ImageButton changeAvatarBtn;
    ImageButton changeUsernameBtn;
    EditText changeUsernameEdt;
    Button saveBtn;
    ProfileViewModel profileViewModel;
    ConstraintLayout parentLayout;
    ConstraintLayout changePasswordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        changePasswordLayout = findViewById(R.id.changePasswordLayout);
        toolbar = findViewById(R.id.toolbar);
        backBtn = findViewById(R.id.backButton);
        saveBtn = findViewById(R.id.saveBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        avatarIv = findViewById(R.id.avatarImageView);
        changeAvatarBtn = findViewById(R.id.changeAvatarBtn);
        changeUsernameBtn = findViewById(R.id.changeUsernameBtn);
        changeUsernameEdt = findViewById(R.id.changeUsernameEdt);
        toolbarTextView = (TextView) findViewById(R.id.activityName);
        usernameTv = findViewById(R.id.usernameTV);
        parentLayout = findViewById(R.id.parentLayout);
        toolbarTextView.setText("Profile");
        usernameTv.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null) {
            Glide.with(this)
                    .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                    .circleCrop()
                    .placeholder(R.drawable.person_circle_fill)
                    .into(avatarIv);
        }
        changePasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        changeAvatarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(ProfileActivity.this)
                        .crop()                //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
            }
        });
        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        changeUsernameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeUsernameEdt.setVisibility(View.VISIBLE);
                usernameTv.setVisibility(View.INVISIBLE);
                changeUsernameBtn.setVisibility(View.INVISIBLE);
                changeUsernameEdt.setText(usernameTv.getText());
                changeUsernameEdt.requestFocus();
                changeUsernameEdt.setSelection(usernameTv.getText().length());
                saveBtn.setVisibility(View.VISIBLE);

            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                changeUsernameEdt.setVisibility(View.INVISIBLE);
                usernameTv.setVisibility(View.VISIBLE);
                changeUsernameBtn.setVisibility(View.VISIBLE);
                saveBtn.setVisibility(View.INVISIBLE);
                usernameTv.setText("changeUsernameEdt");
                usernameTv.setText(changeUsernameEdt.getText().toString());
                profileViewModel.changeUsername(changeUsernameEdt.getText().toString());

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Glide.with(this)
                    .load(uri)
                    .circleCrop()
                    .placeholder(R.drawable.person_circle_fill)
                    .into(avatarIv);
            profileViewModel.changeAvatar(uri);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
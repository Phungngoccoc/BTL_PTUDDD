package com.example.necomovie;

import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileViewModel extends ViewModel {

    MutableLiveData<Boolean> isChangeUsernameSuccessful = new MutableLiveData<>();
    MutableLiveData<String> userName = new MutableLiveData<>();

    void changeUsername(String username){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();
        FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates);
    }
    void changeAvatar(Uri uri) {
        long timestamp = System.currentTimeMillis() / 1000L;
        String directory = "images/"+FirebaseAuth.getInstance().getCurrentUser().getUid() + "_avatar_"+ timestamp+".png";
        StorageReference imagesRef = FirebaseStorage.getInstance().getReference().child(directory);
        UploadTask uploadTask = imagesRef.putFile(uri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               imagesRef.getDownloadUrl().addOnCompleteListener( new OnCompleteListener<Uri>() {
                   @Override
                   public void onComplete(@NonNull Task<Uri> task) {
                       UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                               .setPhotoUri(task.getResult())
                               .build();
                       FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates);
                   }
               });
            }
        });
    }
}

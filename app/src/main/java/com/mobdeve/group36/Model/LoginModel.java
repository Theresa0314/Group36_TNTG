package com.mobdeve.group36.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mobdeve.group36.Data.firebase.FirebaseLoginInstance;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginModel extends ViewModel {

    private FirebaseLoginInstance loginInstance;
    public LiveData<Task> logInUser;
    public LiveData<FirebaseUser> firebaseUserLoginStatus;
    public LiveData<FirebaseAuth> firebaseAuthLiveData;
    public LiveData<Task> successPasswordReset;
    public LiveData<Boolean> successUpdateToken;


    public LoginModel() {
        loginInstance = new FirebaseLoginInstance();
    }

    public void userLogIn(String emailLogIn, String pwdLogIn) {
        logInUser = loginInstance.loginUser(emailLogIn, pwdLogIn);
    }

    public void getFirebaseUserLogInStatus() {
        firebaseUserLoginStatus = loginInstance.getFirebaseUserLoginStatus();
    }

    public void getFirebaseAuth() {
        firebaseAuthLiveData = loginInstance.getFirebaseAuth();
    }


    public void updateToken(String newToken) {
        successUpdateToken = loginInstance.successUpdateToken(newToken);
    }

}

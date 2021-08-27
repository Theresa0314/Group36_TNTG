package com.mobdeve.group36.Model;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mobdeve.group36.Data.firebase.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public class DatabaseModel extends ViewModel {
    private FirebaseDatabase instance;
    public LiveData<Boolean> successAddUserDb;
    public LiveData<DataSnapshot> fetchUserCurrentData;
    public LiveData<DataSnapshot> fetchUserNames;
    public LiveData<DataSnapshot> fetchSelectedProfileUserData;
    public LiveData<DataSnapshot> fetchedChat;
    public LiveData<StorageReference> imageFileReference;
    public LiveData<Boolean> successAddImageUrlInDatabase;
    public LiveData<Boolean> successAddUsernameInDatabase;
    public LiveData<Boolean> successAddBioInDatabase;
    public LiveData<Boolean> successAddStatusInDatabase;
    public LiveData<DataSnapshot> fetchSearchUser;
    public LiveData<DataSnapshot> getChaListUserDataSnapshot;
    public LiveData<DatabaseReference> getTokenRefDb;




    public DatabaseModel() {
        instance = new FirebaseDatabase();
    }

    public void addUserDatabase(String userId, String userName, String emailId, String timestamp, String imageUrl) {
        successAddUserDb = instance.addUserInDatabase(userId, userName, emailId, timestamp, imageUrl);
    }

    public void fetchingUserDataCurrent() {
        fetchUserCurrentData = instance.fetchUserDataCurrent();
    }

    public void fetchUserByNameAll() {
        fetchUserNames = instance.fetchAllUserByNames();
    }

    public void fetchSelectedUserProfileData(String userId) {
        fetchSelectedProfileUserData = instance.fetchSelectedUserIdData(userId);
    }

    public void fetchChatUser() {
        fetchedChat = instance.fetchChatUser();
    }

    public void fetchImageFileReference(String timeStamp, Uri imageUri, Context context) {
        imageFileReference = instance.fetchFileReference(timeStamp, imageUri, context);
    }

    public void addImageUrlInDatabase(String imageUrl, Object mUri) {
        successAddImageUrlInDatabase = instance.addImageUrlInDatabase(imageUrl, mUri);
    }

    public void addBioInDatabase(String bio, Object bioUpdated) {
        successAddBioInDatabase = instance.addBioInDatabase(bio, bioUpdated);
    }

    public void addUsernameInDatabase(String usernameUpdated, Object username) {
        successAddUsernameInDatabase = instance.addUsernameInDatabase(usernameUpdated, username);
    }

    public void addStatusInDatabase(String statusUpdated,Object status){
        successAddStatusInDatabase = instance.addStatusInDatabase(statusUpdated, status);
    }

    public void fetchSearchedUser(String searchQuery){
        fetchSearchUser = instance.fetchSearchUser(searchQuery);
    }


    public void getChaListUserDataSnapshot(String currentUserId){
        getChaListUserDataSnapshot = instance.getChatList(currentUserId);
    }

    public void getTokenDatabaseRef(){
        getTokenRefDb = instance.getTokenRef();
    }


}

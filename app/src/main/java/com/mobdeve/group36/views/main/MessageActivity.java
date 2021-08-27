package com.mobdeve.group36.views.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobdeve.group36.Data.firebase.Client;
import com.mobdeve.group36.Data.model.Chat;
import com.mobdeve.group36.Data.model.User;
import com.mobdeve.group36.Model.DatabaseModel;
import com.mobdeve.group36.Model.LoginModel;
import com.mobdeve.group36.R;
import com.mobdeve.group36.views.adapters.MessageAdapter;
import com.mobdeve.group36.views.fragments.APIService;
import com.mobdeve.group36.views.fragments.GetProfile;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    LoginModel logInViewModel;
    DatabaseModel databaseViewModel;

    CircleImageView iv_profile_image;
    TextView tv_profile_user_name;
    ImageView iv_back_button;
    ImageView iv_user_status_message_view;

    String profileUserNAme;
    String profileImageURL;
    String bio;
    FirebaseUser currentFirebaseUser;

    EditText et_chat;
    ImageView btn_sendIv;

    String chat;
    String timeStamp;
    String userId_receiver; // userId of other user who'll receive the text // Or the user id of profile currently opened
    String userId_sender;  // current user id
    String user_status;
    MessageAdapter messageAdapter;
    ArrayList<Chat> chatsArrayList;
    RecyclerView recyclerView;
    Context context;
    GetProfile bottomSheetProfileDetailUser;

    APIService apiService;
    boolean notify = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        userId_receiver = getIntent().getStringExtra("userid");

        init();
        getCurrentFirebaseUser();
        fetchAndSaveCurrentProfileTextAndData();


        iv_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetDetailFragment(profileUserNAme, profileImageURL, bio);
            }
        });


        btn_sendIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;

                chat = et_chat.getText().toString().trim();
                if (!chat.equals("")) {

                } else {
                    Toast.makeText(MessageActivity.this, "Message can't be empty.", Toast.LENGTH_SHORT).show();
                }
                et_chat.setText("");
            }
        });

    }

    private void openBottomSheetDetailFragment(String username, String imageUrl, String bio) {
        bottomSheetProfileDetailUser = new GetProfile(username, imageUrl, bio, context);
        assert getSupportActionBar() != null;
        bottomSheetProfileDetailUser.show(getSupportFragmentManager(), "edit");
    }

    private void getCurrentFirebaseUser() {
        logInViewModel.getFirebaseUserLogInStatus();
        logInViewModel.firebaseUserLoginStatus.observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                currentFirebaseUser = firebaseUser;
                userId_sender = currentFirebaseUser.getUid();
            }
        });
    }



    private void fetchAndSaveCurrentProfileTextAndData() {
        if(userId_receiver == null){
            userId_receiver=  getIntent().getStringExtra("userId");
        }
        databaseViewModel.fetchSelectedUserProfileData(userId_receiver);
        databaseViewModel.fetchSelectedProfileUserData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                assert user != null;
                profileUserNAme = user.getUsername();
                profileImageURL = user.getImageUrl();
                bio = user.getBio();
                user_status = user.getStatus();


                tv_profile_user_name.setText(profileUserNAme);
                if (profileImageURL.equals("default")) {
                    iv_profile_image.setImageResource(R.drawable.sample_img);
                } else {
                    Glide.with(getApplicationContext()).load(profileImageURL).into(iv_profile_image);
                }
                fetchChatFromDatabase(userId_receiver, userId_sender);
            }
        });
    }



    private void fetchChatFromDatabase(String myId, String senderId) {
        databaseViewModel.fetchChatUser();
        databaseViewModel.fetchedChat.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                chatsArrayList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chats = snapshot.getValue(Chat.class);
                    assert chats != null;
                    if (chats.getReceiverId().equals(senderId) && chats.getSenderId().equals(myId) || chats.getReceiverId().equals(myId) && chats.getSenderId().equals(senderId)) {
                        chatsArrayList.add(chats);
                    }

                    messageAdapter = new MessageAdapter(chatsArrayList, context, userId_sender);
                    recyclerView.setAdapter(messageAdapter);
                }
            }
        });
    }



    private void init() {
        databaseViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(DatabaseModel.class);
        logInViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(LoginModel.class);
        context = MessageActivity.this;

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        iv_user_status_message_view = findViewById(R.id.iv_user_status_message_view);
        iv_profile_image = findViewById(R.id.iv_user_image);

        tv_profile_user_name = findViewById(R.id.tv_profile_user_name);
        iv_back_button = findViewById(R.id.iv_back_button);

        iv_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        et_chat = findViewById(R.id.et_chat);
        btn_sendIv = findViewById(R.id.iv_send_button);

        recyclerView = findViewById(R.id.recycler_view_messages_record);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        chatsArrayList = new ArrayList<>();


    }

    private void currentUser(String userid){
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();
    }


    private void addStatusInDatabase(String status) {
        databaseViewModel.addStatusInDatabase("status", status);
    }

    @Override
    protected void onResume() {
        super.onResume();
        addStatusInDatabase("online");
        currentUser(userId_receiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        addStatusInDatabase("offline");
        currentUser("none");
    }


}
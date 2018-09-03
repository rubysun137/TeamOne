package com.ruby.teamone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class UserActivity extends AppCompatActivity {

    private FirebaseUser mUser;
    private DatabaseReference mReference;
    private FirebaseDatabase mDatabase;
    private String mUserEmail;
    private String mUserUid;
    private String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserEmail = mUser.getEmail();
        mUserUid = mUser.getUid();
        mUserName = mUser.getDisplayName();
        Log.d("User Email  ", "onCreate: " + mUserEmail);
        Log.d("User UID  ", "onCreate: " + mUserUid);
        mReference = FirebaseDatabase.getInstance().getReference();

        addUser();

        addArticle();

    }

    private void addArticle() {
        long time = System.currentTimeMillis();
        String articleId = mReference.child("article_database").push().getKey();
        Article article = new Article("testtest",articleId,"生活","Test 1",time,mUserUid);
        mReference.child("article_database").child(articleId).setValue(article);

    }

    private void addUser() {
        Friend friend = new Friend("發送邀請中","ruby@gmail.com");
        ArrayList<Friend> friends = new ArrayList<>();
        friends.add(friend);

        User user = new User(mUserEmail,friends,mUserName,mUserUid);
        String emailKey = mUserEmail.replace('@','_').replace('.','_');

        mReference.child("user_database").child(emailKey).setValue(user);
//        mReference.child("user_database").child(emailKey).child("name").setValue("Ruby Sun");
        Log.d("TRY   ", "onCreate: " + mReference.toString());
        Log.d("TRY TRY  ", "onCreate: " + mReference.child("user_database").toString());
        Log.d("TRY TRY TRY ", "onCreate: " + mReference.child("user_database").child("ybYLaENWESY3KRbPzo95XNG7RCv2").getDatabase());

    }
}

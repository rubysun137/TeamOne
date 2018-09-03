package com.ruby.teamone;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    private FirebaseUser mUser;
    private DatabaseReference mReference;
    private FirebaseDatabase mDatabase;
    private String mUserEmail;
    private EditText mFriendEmailEditText;
    private EditText mArticleTitle;
    private EditText mArticleContent;
    private Spinner mArticleTagSpinner;
    private Button mAddFriendButton;
    private Button mAddArticleButton;
    private ArrayAdapter<String> mTagList;
    private String tag;
    private String mEmailKey;
    private String mFriendMail;
    private String mfriendNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        mFriendEmailEditText = findViewById(R.id.friendEmail);

        mArticleTitle = findViewById(R.id.articleTitle);
        mArticleContent = findViewById(R.id.articleContent);
        mArticleTagSpinner = findViewById(R.id.articleTag);
        final String[] tags = {"表特", "八卦", "就可", "生活"};
        mTagList = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, tags);
        mArticleTagSpinner.setAdapter(mTagList);
        mArticleTagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tag = mTagList.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserEmail = mUser.getEmail();
        Log.d("User Email  ", "onCreate: " + mUserEmail);
        mReference = FirebaseDatabase.getInstance().getReference();
        mAddArticleButton = findViewById(R.id.addArticleButton);
        mAddFriendButton = findViewById(R.id.addFriendButton);

        mAddArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArticle();
            }
        });

        mAddFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriend();
            }
        });


    }

    private void addFriend() {
        mFriendMail = "ruby@gmail.com";//mFriendEmailEditText.getText().toString();
        addToMyFriendList(mFriendMail,"是否接受邀請",mUserEmail);



    }

    private void addToMyFriendList(String friendMail, final String accept, final String userEmail) {

        mEmailKey = friendMail.replace('@', '_').replace('.', '_');
        mfriendNumber = "0";
        Query queryReference = mReference.child("user_database").orderByChild("email").equalTo(friendMail);
        queryReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //User object 要有空的 constructor 才能 get value
                    User user = snapshot.getValue(User.class);
                    if (user.getFriends() != null) {

                        mfriendNumber = user.getFriends().size() + "";
                    }
                }
                Log.d("get Friend name ", "onDataChange: " + mfriendNumber);
//                Friend friendOfMine = new Friend("發送邀請中", mFriendMail);
                Friend friend = new Friend(accept, userEmail);
//                ArrayList<Friend> friends = new ArrayList<>();
//                friends.add(friend);
                mReference.child("user_database").child(mEmailKey).child("friends").child(mfriendNumber).setValue(friend).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        addToFriendList(mUserEmail,"發送邀請中",mFriendMail);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("get Friend name ", "FAIL!!!!!!!");
            }
        });
    }

    private void addToFriendList(String friendMail, final String accept, final String userEmail) {

        mEmailKey = friendMail.replace('@', '_').replace('.', '_');
        mfriendNumber = "0";
        Query queryReference = mReference.child("user_database").orderByChild("email").equalTo(friendMail);
        queryReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //User object 要有空的 constructor 才能 get value
                    User user = snapshot.getValue(User.class);
                    if (user.getFriends() != null) {

                        mfriendNumber = user.getFriends().size() + "";
                    }
                }
                Log.d("get Friend name ", "onDataChange: " + mfriendNumber);
//                Friend friendOfMine = new Friend("發送邀請中", mFriendMail);
                Friend friend = new Friend(accept, userEmail);
//                ArrayList<Friend> friends = new ArrayList<>();
//                friends.add(friend);
                mReference.child("user_database").child(mEmailKey).child("friends").child(mfriendNumber).setValue(friend);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("get Friend name ", "FAIL!!!!!!!");
            }
        });
    }

    private void addArticle() {
        String title = mArticleTitle.getText().toString();
        String content = mArticleContent.getText().toString();
        long time = System.currentTimeMillis();

        String articleId = mReference.child("article_database").push().getKey();
        Article article = new Article(content, articleId, tag, title, time, mUserEmail);
        mReference.child("article_database").child(articleId).setValue(article);

    }


}

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
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserActivity extends AppCompatActivity {

    private FirebaseUser mUser;
    private DatabaseReference mReference;
    private FirebaseDatabase mDatabase;
    private String mUserEmail;
    private EditText mFriendEmailEditText;
    private EditText mArticleTitle;
    private EditText mArticleContent;
    private Spinner mArticleTagSpinner;
    private Spinner mSearchTagSpinner;
    private Spinner mSearchFriendArticleSpinner;
    private Spinner mFriendPermissionSpinner;
    private Button mAddFriendButton;
    private Button mAddArticleButton;
    private Button mSearchTagButton;
    private Button mSearchFriendArticleButton;
    private Button mSearchFriendTagButton;
    private Button mRejectButton;
    private Button mAcceptButton;
    private ArrayAdapter<String> mTagList;
    private ArrayAdapter<String> mFriendArticleAdapter;
    private ArrayAdapter<String> mFriendPermissionAdapter;
    private String mTag;
    private String mRealFriendEmail;
    private String mNotSureFriendEmail;
    private String mEmailKey;
    private String mFriendMail;
    private String mFriendEmailKey;
    private List<String> mFriendList;
    private List<String> mNotSureFriendList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserEmail = mUser.getEmail();
        Log.d("User Email  ", "onCreate: " + mUserEmail);
        mReference = FirebaseDatabase.getInstance().getReference();

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
                mTag = mTagList.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mTag = mTagList.getItem(0);
            }
        });

        mSearchTagSpinner = findViewById(R.id.searchTagSpinner);
        mSearchTagSpinner.setAdapter(mTagList);
        mSearchTagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mTag = mTagList.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mTag = mTagList.getItem(0);
            }
        });

        mSearchFriendArticleSpinner = findViewById(R.id.searchFriendArticleSpinner);
        setSpinnerAdapter();

        mFriendPermissionSpinner = findViewById(R.id.friendPermissionSpinner);
        setFriendSpinnerAdapter();


        mAddArticleButton = findViewById(R.id.addArticleButton);
        mAddFriendButton = findViewById(R.id.addFriendButton);
        mSearchTagButton = findViewById(R.id.searchTagButton);
        mSearchFriendArticleButton = findViewById(R.id.searchFriendArticleButton);
        mSearchFriendTagButton = findViewById(R.id.searchFriendTagButton);
        mAcceptButton = findViewById(R.id.acceptButton);
        mRejectButton = findViewById(R.id.rejectButton);

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

        mSearchTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchTagArticles();
            }
        });

        mSearchFriendArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFriendArticle();
            }
        });

        mSearchFriendTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFriendTagArticle();
            }
        });

        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptFriend();
            }
        });

        mRejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFriend();
            }
        });

    }

    private void acceptFriend() {
        mEmailKey = mUserEmail.replace('@', '_').replace('.', '_');
        final String notSureFriend = mNotSureFriendEmail.replace('@', '_').replace('.', '_');
        mReference.child("user_database").child(mEmailKey).child("friends").child(notSureFriend).child("accept").setValue("好友").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mReference.child("user_database").child(notSureFriend).child("friends").child(mEmailKey).child("accept").setValue("好友");
                setFriendSpinnerAdapter();
            }
        });
    }


    private void deleteFriend() {

        mEmailKey = mUserEmail.replace('@', '_').replace('.', '_');
        final String notSureFriend = mNotSureFriendEmail.replace('@', '_').replace('.', '_');
        mReference.child("user_database").child(mEmailKey).child("friends").child(notSureFriend).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mReference.child("user_database").child(notSureFriend).child("friends").child(mEmailKey).removeValue();
                setFriendSpinnerAdapter();
            }
        });

    }

    private void searchFriendTagArticle() {
        Query query = mReference.child("article_database").orderByChild("email").equalTo(mRealFriendEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue(Article.class).getArticle_tag().equals(mTag)) {
                        Log.d("Search By Friend", "onDataChange: " + snapshot.getValue(Article.class).getArticle_title());
                        Log.d("Search By Friend", "onDataChange: " + snapshot.getValue(Article.class).getArticle_tag());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchFriendArticle() {
        Query query = mReference.child("article_database").orderByChild("email").equalTo(mRealFriendEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("Search By Friend", "onDataChange: " + snapshot.getValue(Article.class).getArticle_title());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setFriendSpinnerAdapter() {
        mEmailKey = mUserEmail.replace('@', '_').replace('.', '_');
        Query query = mReference.child("user_database").child(mEmailKey).child("friends").orderByChild("accept").equalTo("是否接受邀請");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mNotSureFriendList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mNotSureFriendList.add(snapshot.getValue(Friend.class).getFriend_email());
                }
                mFriendPermissionAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, mNotSureFriendList);
                mFriendPermissionSpinner.setAdapter(mFriendPermissionAdapter);
                mFriendPermissionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mNotSureFriendEmail = mNotSureFriendList.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        mNotSureFriendEmail = mNotSureFriendList.get(0);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setSpinnerAdapter() {
        mEmailKey = mUserEmail.replace('@', '_').replace('.', '_');
        Query query = mReference.child("user_database").child(mEmailKey).child("friends").orderByChild("accept").equalTo("好友");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mFriendList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mFriendList.add(snapshot.getValue(Friend.class).getFriend_email());
                }
                mFriendArticleAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, mFriendList);
                mSearchFriendArticleSpinner.setAdapter(mFriendArticleAdapter);
                mSearchFriendArticleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mRealFriendEmail = mFriendList.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        mRealFriendEmail = mFriendList.get(0);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void searchTagArticles() {
        Query query = mReference.child("article_database").orderByChild("article_tag").equalTo(mTag);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("Search Article By Tag", "onDataChange: " + snapshot.getValue(Article.class).getArticle_title());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addFriend() {
        mFriendMail = mFriendEmailEditText.getText().toString();
        if (!mFriendMail.equals(mUserEmail)) {
            mEmailKey = mFriendMail.replace('@', '_').replace('.', '_');
            Query query = mReference.child("user_database").orderByKey().equalTo(mEmailKey);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //是否有這個使用者
                    if (dataSnapshot.getChildrenCount() != 0) {
                        Log.d("FRIENDS!!!", "onDataChange:  有這個人!!!!");

                        mEmailKey = mUserEmail.replace('@', '_').replace('.', '_');
                        Query queryReference = mReference.child("user_database").child(mEmailKey).child("friends").orderByChild("friend_email").equalTo(mFriendMail);
                        queryReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount() != 0) {
                                    Log.d(" friend!!!   ", "addFriend: 已經存在~");
                                    Toast.makeText(UserActivity.this, "好友已經存在", Toast.LENGTH_SHORT).show();
                                } else {
                                    addToMyFriendList(mFriendMail, "是否接受邀請", mUserEmail);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d("get Friend name ", "FAIL!!!!!!!");
                                addToMyFriendList(mFriendMail, "是否接受邀請", mUserEmail);
                            }
                        });
                    } else {
                        Log.d(" friend!!!   ", "addFriend: 查無此人");
                        Toast.makeText(UserActivity.this, "查無此人", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void addToMyFriendList(String friendMail, final String accept, final String userEmail) {

        mEmailKey = userEmail.replace('@', '_').replace('.', '_');
        mFriendEmailKey = friendMail.replace('@', '_').replace('.', '_');
//
        Log.d("get Friend name ", "onDataChange: " + mFriendEmailKey);
        Friend friend = new Friend(accept, userEmail);
        mReference.child("user_database").child(mFriendEmailKey).child("friends").child(mEmailKey).setValue(friend).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                addToFriendList(mUserEmail, "發送邀請中", mFriendMail);
            }
        });


    }

    private void addToFriendList(String userMail, final String accept, final String friendEmail) {

        mEmailKey = userMail.replace('@', '_').replace('.', '_');
        mFriendEmailKey = friendEmail.replace('@', '_').replace('.', '_');

        Log.d("get Friend name ", "onDataChange: " + mFriendEmailKey);
        Friend friend = new Friend(accept, friendEmail);
        mReference.child("user_database").child(mEmailKey).child("friends").child(mFriendEmailKey).setValue(friend);
    }

    private void addArticle() {
        String title = mArticleTitle.getText().toString();
        String content = mArticleContent.getText().toString();
//        long time = System.currentTimeMillis();
        if(!"".equals(title) && !"".equals(content)){
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd hh:mm", Locale.ENGLISH);
            String formatDate = simpleDateFormat.format(date);

            String articleId = mReference.child("article_database").push().getKey();
            Article article = new Article(content, articleId, mTag, title, formatDate, mUserEmail);
            mReference.child("article_database").child(articleId).setValue(article);
        }else{
            Toast.makeText(this, "There is something missing. Please check again!", Toast.LENGTH_LONG).show();
        }
    }
}

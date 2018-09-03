package com.ruby.teamone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.DataBufferResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private FirebaseAuth mAuth;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mName;
    private Button mSendButton;
    private Button mSignUpButton;
    private String mUserEmail;
    private String mUserUid;
    private String mUserName;
    private FirebaseUser mFirebaseUser;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(MainActivity.this,UserActivity.class);
            startActivity(intent);
        }
        mAuth = FirebaseAuth.getInstance();
        mName = findViewById(R.id.nameText);
        mEmail = findViewById(R.id.emailText);
        mPassword = findViewById(R.id.passwordText);
        mSendButton = findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String name = mName.getText().toString();
                            mFirebaseUser = mAuth.getCurrentUser();
                            if (!"".equals(name)) {
                                UserProfileChangeRequest profileChange = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();
                                mFirebaseUser.updateProfile(profileChange).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(MainActivity.this, "Hi, My Name Is " + mFirebaseUser.getDisplayName(), Toast.LENGTH_LONG).show();
                                        addUser();
                                    }
                                });
                            } else {
                                if (mFirebaseUser.getDisplayName() == null) {
                                    UserProfileChangeRequest profileChange = new UserProfileChangeRequest.Builder()
                                            .setDisplayName("Who am I?")
                                            .build();
                                    mFirebaseUser.updateProfile(profileChange).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(MainActivity.this, "Hi, My Name Is " + mFirebaseUser.getDisplayName(), Toast.LENGTH_LONG).show();
                                            addUser();
                                        }
                                    });
                                } else {
                                    Toast.makeText(MainActivity.this, "Hi, My Name Is " + mFirebaseUser.getDisplayName(), Toast.LENGTH_LONG).show();
                                }
                            }


                            Log.d("Login ", "signInWithEmail:success");
                            Intent intent = new Intent(MainActivity.this, UserActivity.class);
                            startActivity(intent);
                        } else {
                            Log.w("Login ", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mSignUpButton = findViewById(R.id.signUpButton);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            addUser();

                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Sing Up ", "createUserWithEmail:success");

                            Intent intent = new Intent(MainActivity.this, UserActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Sign Up ", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private void addUser() {

        mFirebaseUser = mAuth.getCurrentUser();
        UserProfileChangeRequest profileChange = new UserProfileChangeRequest.Builder()
                .setDisplayName(mName.getText().toString())
                .build();
        mFirebaseUser.updateProfile(profileChange).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mUserEmail = mFirebaseUser.getEmail();
                mUserUid = mFirebaseUser.getUid();
                mUserName = mFirebaseUser.getDisplayName();

                DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
//        Friend friend = new Friend("發送邀請中", "ruby@gmail.com");
                ArrayList<Friend> friends = new ArrayList<>();
//        friends.add(friend);

                User user = new User(mUserEmail, friends, mUserName, mUserUid);
                String emailKey = mUserEmail.replace('@', '_').replace('.', '_');

                mReference.child("user_database").child(emailKey).setValue(user);
                Log.d("TRY   ", "onCreate: " + mReference.toString());
                Log.d("TRY TRY  ", "onCreate: " + mReference.child("user_database").toString());
                Log.d("TRY TRY TRY ", "onCreate: " + mReference.child("user_database").child("ybYLaENWESY3KRbPzo95XNG7RCv2").getDatabase());

            }
        });

    }

}

package com.aviad.footballwithfriends;

import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private TextView mTextMessage;
 //   private Fragment mStopwatchFragment = new StopwatchFragment();
    private Fragment mChatFragment = new ChatFragment();
    private Fragment mContactsFragment = new ContactsFragment();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;



    public Fragment getChatFragment() {
        return mChatFragment;
    }

    public void setChatFragment(Fragment mChatFragment) {
        if(mChatFragment != null) {
            this.mChatFragment = mChatFragment;
        }
    }

    public Fragment getContactsFragment() {
        return mContactsFragment;
    }

    public void setContactsFragment(Fragment mContactsFragment) {
        if(mContactsFragment != null) {
            this.mContactsFragment = mContactsFragment;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setNavigationListener();
        if(savedInstanceState != null) {
            reloadFragment();
        }else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame, mChatFragment, "ChatFragment")
                    .commit();
        }
        FirebaseAuth.getInstance().addAuthStateListener(this);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void reloadFragment() {
        FragmentManager fm = getSupportFragmentManager();
      //  setStopwatchFragment(fm.findFragmentByTag("StopWatch"));
        setChatFragment(fm.findFragmentByTag("ChatFragment"));
        setContactsFragment(fm.findFragmentByTag("Contact"));
    }

    private void setNavigationListener() {
        this.mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_chat:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame, mChatFragment, "ChatFragment")
                                .commit();
                        return true;
                    case R.id.navigation_stopwatch:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame, new StopwatchFragment(), "StopWatch")
                                .commit();
                        return true;
                    case R.id.navigation_contacts:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame, mContactsFragment, "Contact")
                                .commit();

                        return true;
                }
                return false;
            }
        };
    }



    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
//            finish();
        }
    }
}

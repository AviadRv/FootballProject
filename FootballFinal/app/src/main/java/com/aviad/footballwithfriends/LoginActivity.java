package com.aviad.footballwithfriends;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "PhoneAuth";

    private EditText etPhone;
    private EditText etCode;
    private Button sendBtn;
    private Button verifyBtn;
    private Button resendBtn;
    private TextView tvStatus;
    String number;
    CountryCodePicker ccp;

    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPhone = findViewById(R.id.etPhone);
        etCode = findViewById(R.id.etCode);
        sendBtn = findViewById(R.id.sendBtn);
        verifyBtn = findViewById(R.id.verifyBtn);
        tvStatus = findViewById(R.id.tvStatus);
        resendBtn = findViewById(R.id.resendBtn);

        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(etPhone);

        fbAuth = FirebaseAuth.getInstance();

    }

    public void sendCode(View view) {

        number = ccp.getFullNumberWithPlus();

        setUpVerificationCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
          number, 60 , TimeUnit.SECONDS , this , verificationCallbacks
        );
    }

    private void setUpVerificationCallbacks() {

        verificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                resendBtn.setEnabled(false);
                verifyBtn.setEnabled(false);
                etCode.setText("");
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                if (e instanceof FirebaseAuthInvalidCredentialsException){
                    Toast.makeText(LoginActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                }
                else if (e instanceof FirebaseTooManyRequestsException){
                    Toast.makeText(LoginActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                phoneVerificationId = verificationId;
                resendToken = token;

                verifyBtn.setEnabled(true);
                sendBtn.setEnabled(false);
                resendBtn.setEnabled(true);
            }
        };
    }


    public void verifyCode(View view) {

        String code = etCode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId,code);
        signInWithPhoneAuthCredential(credential);

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fbAuth.signInWithCredential(credential).addOnCompleteListener(this, (task) -> {
        if (task.isSuccessful()){
            etCode.setText("");
            verifyBtn.setEnabled(false);
            FirebaseUser user = task.getResult().getUser();
            String phoneNumber = user.getPhoneNumber();

            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("phone",phoneNumber);
            startActivity(intent);
            finish();
        } else  {
            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
            }
        }
    });
    }

    public void resendCode(View view) {

        number = ccp.getFullNumberWithPlus();
        setUpVerificationCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number, 60 , TimeUnit.SECONDS , this , verificationCallbacks , resendToken
        );

    }





}

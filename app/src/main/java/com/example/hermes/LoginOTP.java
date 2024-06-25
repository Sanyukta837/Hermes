package com.example.hermes;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hermes.utils.AndroidUtils;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginOTP extends AppCompatActivity {

    String phone_number;
    Long timeoutSeconds = 60L;
    String verificationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;

    EditText otp_input;
    Button otp_enter_button;
    ProgressBar otp_progress_bar;
    TextView resend_otp_text;
    FirebaseAuth myAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        otp_enter_button.setVisibility(View.GONE);
        phone_number = getIntent().getExtras().getString("phone_obtained");
        Toast.makeText(getApplicationContext(),phone_number, Toast.LENGTH_LONG).show();

        otp_input = findViewById(R.id.login_otp);
        otp_enter_button = findViewById(R.id.button_enter_otp);
        otp_progress_bar = findViewById(R.id.otp_progressbar);
        resend_otp_text = findViewById(R.id.resend_otp);

    }

    void sendOtp(String phone_number, boolean isResend){
        setInprogress(true);
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(myAuth)
                        .setPhoneNumber(phone_number)
                        .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signIn(phoneAuthCredential);
                                setInprogress(false);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                AndroidUtils.showToast(getApplicationContext(),"OTP verification failed");
                                setInprogress(false);
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                resendingToken = forceResendingToken;
                                AndroidUtils.showToast(getApplicationContext(),"OTP sent successfully");
                                setInprogress(false);
                            }
                        });
        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        }else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }


    void setInprogress(boolean state){
        if(state){
            otp_progress_bar.setVisibility(View.VISIBLE);
            otp_enter_button.setVisibility(View.GONE);
        } else{
            otp_progress_bar.setVisibility(View.GONE);
            otp_enter_button.setVisibility(View.VISIBLE);
        }
    }

    void signIn(PhoneAuthCredential phoneAuthCredential){
        //login
    }
}
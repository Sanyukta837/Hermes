package com.example.hermes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.TimerTask;
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
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        otp_input = findViewById(R.id.login_otp);
        otp_enter_button = findViewById(R.id.button_enter_otp);
        otp_progress_bar = findViewById(R.id.otp_progressbar);
        resend_otp_text = findViewById(R.id.resend_otp);

        otp_enter_button.setVisibility(View.GONE);



        phone_number = getIntent().getExtras().getString("phone_obtained");

        sendOtp(phone_number, false);


        otp_enter_button.setOnClickListener(v->{
            String obtainedOTP = otp_input.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, obtainedOTP);
            signIn(credential);
            setInprogress(true);
        });

        resend_otp_text.setOnClickListener(v->{
            sendOtp(phone_number, true);
        });



    }

    void sendOtp(String phone_number, boolean isResend){
        startResendTimer();
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
                                String errorMessage = "OTP verification failed: " + e.getMessage();
                                AndroidUtils.showToast(getApplicationContext(), errorMessage);
                                Log.e("OTP Verification", errorMessage); // Log the error for detailed inspection
                                setInprogress(false);
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                resendingToken = forceResendingToken;
                                AndroidUtils.showToast(getApplicationContext(),"OTP sent successfully");
                                Log.d("OTP Verification", "OTP sent: " + verificationCode); // Log the OTP sent
                                setInprogress(false);
                            }
                        });
        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        }else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

     void startResendTimer() {
        resend_otp_text.setEnabled(false);
         Timer timer = new Timer();
         timer.schedule(new TimerTask() {
             @Override
             public void run() {
                 timeoutSeconds--;
                 resend_otp_text.setText("Resend OTP in "+ timeoutSeconds +" seconds");
                 if(timeoutSeconds <= 0){
                     timeoutSeconds = 60L;
                     timer.cancel();
                     runOnUiThread(()->{
                         resend_otp_text.setText("Resend OTP");
                         resend_otp_text.setEnabled(true);
                     });
                 }
             }
         },0,1000);
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
        myAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setInprogress(false);
                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginOTP.this, LoginName.class);
                    intent.putExtra("phone", phone_number);
                    startActivity(intent);

                }else{
                    AndroidUtils.showToast(getApplicationContext(), "OTP verification failed");

                }
            }
        });

    }
}
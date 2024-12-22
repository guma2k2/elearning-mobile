package com.example.elearningmobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningmobile.R;
import com.example.elearningmobile.api.AuthApi;
import com.example.elearningmobile.model.ErrorVm;
import com.example.elearningmobile.model.VerifyPostVM;
import com.example.elearningmobile.model.order.OrderVM;
import com.google.gson.Gson;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyActivity extends AppCompatActivity {

    private EditText ed_verificationCode;
    private TextView textViewTimer;
    private Button btn_verify, btn_resend, btn_back_to_register;

    private static final long TIMER_DURATION = 15 * 60 * 1000;

    private String email;
    private CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        setControl();
        startCountdownTimer();
        setEvent();
    }

    private void setEvent() {

        btn_back_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegister();
            }
        });
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInput();
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                String verificationCode = ed_verificationCode.getText().toString().trim();
                if (email != null) {
                    VerifyPostVM verifyPostVM = new VerifyPostVM();
                    verifyPostVM.setEmail(email);
                    verifyPostVM.setVerificationCode(verificationCode);
                    verifyPostVM.setType("register");
                    AuthApi.authApi.verify(verifyPostVM).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                                navigateToLogin();
                            }else {
                                try {
                                    if (response.errorBody() != null) {
                                        // Convert the error body to an ErrorVm object
                                        ErrorVm errorVm = new Gson().fromJson(response.errorBody().charStream(), ErrorVm.class);
                                        Toast.makeText(getApplicationContext(), errorVm.getDetails() , Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Unknown error occurred.", Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Failed to parse error details.", Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Mã xác nhận không chính xác. Vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();

                        }

                    });
                }
                // call api verify
            }
        });
        btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_verify.setEnabled(true);
                ed_verificationCode.setEnabled(true);
                startCountdownTimer();
                AuthApi.authApi.resend(email).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra mail để nhận mã xác nhận!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Lỗi xảy ra!", Toast.LENGTH_SHORT).show();

                    }
                });
                // call api resend
            }
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void navigateToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void validateInput() {
        String verificationCode = ed_verificationCode.getText().toString().trim();
        if (verificationCode == "") {
            Toast.makeText(getApplicationContext(), "Mã xác nhận không được để trống", Toast.LENGTH_SHORT).show();
        }
    }

    private void setControl() {
        ed_verificationCode = findViewById(R.id.ed_verificationCode);
        textViewTimer = findViewById(R.id.textViewTimer);
        btn_verify = findViewById(R.id.btn_verify);
        btn_resend = findViewById(R.id.btn_resend);
        btn_back_to_register = findViewById(R.id.btn_back_to_register);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("email");
        }
    }

    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(TIMER_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 1000 / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                textViewTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                textViewTimer.setText("00:00");
                ed_verificationCode.setEnabled(false);
                btn_verify.setEnabled(false);
                Toast.makeText(VerifyActivity.this, "Time expired. Please resend a new code.", Toast.LENGTH_LONG).show();
            }
        }.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
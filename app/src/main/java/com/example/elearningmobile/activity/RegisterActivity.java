package com.example.elearningmobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningmobile.R;
import com.example.elearningmobile.api.AuthApi;
import com.example.elearningmobile.model.AuthenticationVm;
import com.example.elearningmobile.model.ErrorVm;
import com.example.elearningmobile.model.RegistrationPostVm;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText ed_firstName_register, ed_lastName_register, ed_email_register, ed_password_register, ed_confirmPassword_register;
    Button btn_register, btn_login_register;

    TextView first_name_error_message, last_name_error_message, email_error_message, password_error_message, confirm_password_error_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setControl();
        setEvent();
    }

    private void setEvent() {


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = ed_firstName_register.getText().toString();
                String lastName = ed_lastName_register.getText().toString();
                String email = ed_email_register.getText().toString();
                String password = ed_password_register.getText().toString();
                if (validateData() == false) {
                    return;
                }
                // call api register
                RegistrationPostVm registrationPostVm = new RegistrationPostVm();
                registrationPostVm.setFirstName(firstName);
                registrationPostVm.setLastName(lastName);
                registrationPostVm.setPassword(password);
                registrationPostVm.setEmail(email);
                AuthApi.authApi.register(registrationPostVm).enqueue(new Callback<AuthenticationVm>() {
                    @Override
                    public void onResponse(Call<AuthenticationVm> call, Response<AuthenticationVm> response) {
                        if (response.isSuccessful()) {
                            navigateToVerify();
                            resetForm();
                        } else {
                            try {
                                if (response.errorBody() != null) {
                                    // Convert the error body to an ErrorVm object
                                    ErrorVm errorVm = new Gson().fromJson(response.errorBody().charStream(), ErrorVm.class);
                                    if (errorVm.getStatusCode().equals("400 BAD_REQUEST")) {
                                        Toast.makeText(getApplicationContext(), errorVm.getDetails() , Toast.LENGTH_LONG).show();
                                        navigateToVerify();
                                    }
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
                    public void onFailure(Call<AuthenticationVm> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Có lỗi xảy ra trong quá trình đăng ký", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private boolean validateData() {
        String firstName = ed_firstName_register.getText().toString().trim();
        String lastName = ed_lastName_register.getText().toString().trim();
        String email = ed_email_register.getText().toString().trim();
        String password = ed_password_register.getText().toString().trim();
        String confirmPassword = ed_confirmPassword_register.getText().toString().trim();

        if (firstName.equals("")) {
            resetMessage();
//            first_name_error_message.setFocusable(true);
            first_name_error_message.setText("Họ không được để trống");
            return false;
        }

        if (lastName.equals("")){
            resetMessage();
            last_name_error_message.setText("Tên không được để trống");
            return false;
        }
        if (email.equals("")) {
            resetMessage();
            email_error_message.setText("Email không được để trống");
            return false;
        }

        if (password.equals("")){
            resetMessage();
            password_error_message.setText("Mật khẩu không được để trống");
            return false;
        }
        if (confirmPassword.equals("")) {
            resetMessage();
            confirm_password_error_message.setText("Xác nhận mật khẩu không được để trống");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Mật khẩu và xác nhận mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void resetForm() {
        ed_email_register.setText("");
        ed_firstName_register.setText("");
        ed_lastName_register.setText("");
        ed_password_register.setText("");
        ed_confirmPassword_register.setText("");
    }

    private void resetMessage () {
        first_name_error_message.setText("");
        last_name_error_message.setText("");
        email_error_message.setText("");
        password_error_message.setText("");
        confirm_password_error_message.setText("");
    }

    private void navigateToVerify() {
        String email = ed_email_register.getText().toString().trim();
        Intent intent = new Intent(this, VerifyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void setControl() {
        ed_firstName_register = findViewById(R.id.ed_firstName_register);
        ed_lastName_register = findViewById(R.id.ed_lastName_register);
        ed_email_register = findViewById(R.id.ed_email_register);
        ed_password_register = findViewById(R.id.ed_password_register);
        ed_confirmPassword_register = findViewById(R.id.ed_confirmPassword_register);
        btn_register = findViewById(R.id.btn_register);
        btn_login_register = findViewById(R.id.btn_login_register);
        first_name_error_message = findViewById(R.id.first_name_error_message);
        last_name_error_message = findViewById(R.id.last_name_error_message);
        email_error_message = findViewById(R.id.email_error_message);
        password_error_message = findViewById(R.id.password_error_message);
        confirm_password_error_message = findViewById(R.id.confirm_password_error_message);
    }
}
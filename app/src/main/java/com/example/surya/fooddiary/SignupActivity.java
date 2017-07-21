package com.example.surya.fooddiary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bluelinelabs.logansquare.LoganSquare;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends BaseActivity {


    @BindView(R.id.emailET)
    EditText emailET;

    @BindView(R.id.passwordET)
    EditText passwordET;

    @BindView(R.id.confirmPasswordET)
    EditText confrimPasswordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mContext = this;
        ButterKnife.bind(this);

        validationUtils = new ValidationUtils(mContext);
    }

    @OnClick(R.id.signupBtn) public void onSignup() {

        if(validationUtils.checkSignupValid(emailET, passwordET, confrimPasswordET)) {
            showProgressDialog();
            AndroidNetworking.post(MyApplication.BASE_URL + "user/signup")
                    .addBodyParameter("email", emailET.getText().toString())
                    .addBodyParameter("password", passwordET.getText().toString())
                    .setTag("signup")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response
                            hideProgressDialog();
                            try {
                                String status = response.getString("status");
                                if(status.equals("1")) {
                                    JSONObject user = response.getJSONObject("data");
                                    saveKeyValue("me", user.toString());
                                    saveKeyValue("password", passwordET.getText().toString());
                                    User me = LoganSquare.parse(user.toString(), User.class);
                                    startActivity(new Intent(mContext, MainActivity.class));
                                    finish();
                                } else if (status.equals("false")) {
                                    showToast("Incorrect login");
                                } else {
                                    showToast("Network Error!");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                showToast("Network Error!");
                            }
                        }
                        @Override
                        public void onError(ANError error) {
                            // handle error
                            hideProgressDialog();
                            showToast(error.getErrorBody().toString());
                        }
                    });
        }
    }

    @OnClick(R.id.loginBtn) public void onLogin() {
        finish();
    }
}

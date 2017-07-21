package com.example.surya.fooddiary;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bluelinelabs.logansquare.LoganSquare;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.onClick;

public class LoginActivity extends BaseActivity {

    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    @BindView(R.id.emailET)
    EditText emailET;

    @BindView(R.id.passwordET)
    EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        successFBLogin();
                    }

                    @Override
                    public void onCancel() {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Error")
                                .setMessage("Your Facebook login was canceled.")
                                .setPositiveButton(android.R.string.ok, null).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Error")
                                .setMessage(exception.getMessage())
                                .setPositiveButton(android.R.string.ok, null).show();
                    }
                });

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile,
                                                   Profile currentProfile) {
            }
        };

        setContentView(R.layout.activity_login);

        mContext = this;
        ButterKnife.bind(this);
        validationUtils = new ValidationUtils(mContext);

        String meStr = getValueFromKey("me");
        try {
            User me = LoganSquare.parse(meStr, User.class);
            if (meStr != null && !meStr.equals("") && me.userid != null && !me.userid.equals("")) {
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.loginBtn) public void onLogin() {

        if(validationUtils.checkLoginValid(emailET, passwordET)) {
            showProgressDialog();
            AndroidNetworking.post(MyApplication.BASE_URL + "user/login")
                    .addBodyParameter("email", emailET.getText().toString())
                    .addBodyParameter("password", passwordET.getText().toString())
                    .addBodyParameter("one_id", "one_id")
                    .setTag("login")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response
                            hideProgressDialog();
                            try {
                                String status = response.getString("status");
                                if (status.equals("1")) {
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

    @OnClick(R.id.signupBtn) public void onSignup() {
        startActivity(new Intent(mContext, SignupActivity.class));
    }

    @OnClick(R.id.fbBtn) public void onFbLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile, email"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void successFBLogin() {
        final AccessToken currentAccessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        if (object != null) {
                            try {
                                String fbId = object.getString("id");
                                String fbEmail = object.getString("email");
                                String photo = "https://graph.facebook.com/" + object.getString("id") + "/picture?type=large";
                                String username = object.getString("name");
                                if(username == null) {
                                    username = "";
                                }
                                /*String fbName = object.getString("name");
                                String first_name = object.getString("first_name");
                                String last_name = object.getString("last_name");
                                String gender = object.getString("gender");*/

                                fbLoginToServer(fbId, fbEmail);

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        } else {
                            showToast("Network Error");

                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("GraphRequest is failed.")
                                    .setMessage("GraphicRequest is failed.")
                                    .setPositiveButton(android.R.string.ok, null).show();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,email,birthday,location,locale,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void fbLoginToServer (final String fb_id, final String email) {
        showProgressDialog();
        AndroidNetworking.post(MyApplication.BASE_URL + "user/login_fb")
                .addBodyParameter("email", email)
                .addBodyParameter("fb_id", fb_id)
                .addBodyParameter("one_id", "")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgressDialog();
                        try {
                            String status = response.getString("status");
                            if(status.equals("1")) {
                                JSONObject user = response.getJSONObject("data");
                                saveKeyValue("me", user.toString());
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
                        hideProgressDialog();
                        showToast(error.getMessage());
                    }
                });
    }
}

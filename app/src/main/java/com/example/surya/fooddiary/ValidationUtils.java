package com.example.surya.fooddiary;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.widget.EditText;

import java.io.File;

/**
 * Created by Administrator on 11/12/2016.
 */

public class ValidationUtils {
    Context mContext;

    public ValidationUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void ValidationUtils() {

    }

    public boolean checkLoginValid (EditText emailET, EditText passET) {
        boolean result = true;
        if(emailET.getText().toString().equals("")) {
            result = false;
            Common.showToast(mContext, "Please input email or name");
            return result;
        }
        if(passET.getText().toString().equals("")) {
            result = false;
            Common.showToast(mContext, "Please input password");
            return result;
        }
        return result;
    }

    public boolean checkSignupValid (EditText emailET, EditText passwdET, EditText confirmPasswdET) {
        boolean result = true;
        if(emailET.getText().toString().equals("")) {
            result = false;
            Common.showToast(mContext, "Please input email");
            return result;
        }

        if(passwdET.getText().toString().equals("")) {
            result = false;
            Common.showToast(mContext, "Please input password");
            return result;
        }
        if(confirmPasswdET.getText().toString().equals("")) {
            result = false;
            Common.showToast(mContext, "Please input confirm password");
            return result;
        }

        String passwd = passwdET.getText().toString();
        String conpass = confirmPasswdET.getText().toString();
        if(!passwd.equals(conpass)) {
            result = false;
            Common.showToast(mContext, "Password and confirm password does not match");
            return result;
        }

        return result;
    }

    public boolean checkForgotPassValid (EditText emailET, EditText passwordET, EditText confirmPasswordET) {
        boolean result = true;
        if(emailET.getText().toString().equals("")) {
            result = false;
            Common.showToast(mContext, "Please input email");
            return result;
        }

        if(passwordET.getText().toString().equals("")) {
            result = false;
            Common.showToast(mContext, "Please input password");
            return result;
        }

        if(confirmPasswordET.getText().toString().equals("")) {
            result = false;
            Common.showToast(mContext, "Please input confirm password");
            return result;
        }

        if(!passwordET.getText().toString().equals(confirmPasswordET.getText().toString())) {
            result = false;
            Common.showToast(mContext, "Please input same passwords");
            return result;
        }
        return result;
    }

    public boolean checkUploadSetting (EditText titleET) {
        boolean result = true;
        if(titleET.getText().toString().equals("")) {
            result = false;
            Common.showToast(mContext, "Please input title");
            return result;
        }
        return result;
    }
}

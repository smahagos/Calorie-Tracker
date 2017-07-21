package com.example.surya.fooddiary;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;

import java.io.IOException;


/**
 * Created by Administrator on 7/18/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public Context mContext;
    public String TAG = "BaseActivity";

    /** UI */
    private ProgressDialog mProgressDialog;
    private ProgressDialog mUploadingProgressDialog;

    ValidationUtils validationUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void showProgressDialog() {
        if (mProgressDialog == null && mContext != null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void showUploadingProgressDialog() {
        if (mUploadingProgressDialog == null) {
            mUploadingProgressDialog = new ProgressDialog(this);
            mUploadingProgressDialog.setCancelable(false);
            mUploadingProgressDialog.setMessage("Uploading...");
        }
        mUploadingProgressDialog.show();
    }

    public void hideUploadingProgressDialog() {
        if (mUploadingProgressDialog != null && mUploadingProgressDialog.isShowing()) {
            mUploadingProgressDialog.dismiss();
        }
    }

    public void saveKeyValue (String key, String value) {
        Common.saveInfoWithKeyValue(mContext, key, value);
    }

    public String getValueFromKey (String key) {
        return Common.getInfoWithValueKey(mContext, key);
    }

    public User getUser(String str) {
        User user = null;
        try {
            user = LoganSquare.parse(str.toString(), User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    private Toast mToast;
    public void showToast(String message) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}

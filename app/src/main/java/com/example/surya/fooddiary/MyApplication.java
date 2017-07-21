package com.example.surya.fooddiary;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;

import com.androidnetworking.AndroidNetworking;
import com.facebook.FacebookSdk;

/**
 * Created by Administrator on 6/28/2016.
 */
public class MyApplication extends Application {

    public static String TAG = "MyApplication";

    public static final String BASE_URL = "http://ec2-34-228-199-199.compute-1.amazonaws.com/RestDemo/api/";
    public static final String IMAGE_BASE_URL = "http://ec2-34-228-199-199.compute-1.amazonaws.com/RestDemo/";


    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AndroidNetworking.initialize(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Typeface getFontAwesome(Context context){
        return Typeface.createFromAsset(context.getAssets(), "Noteworthy Bold.ttf");
    }
}



/** Android Networking useful */

/* Get
AndroidNetworking.get("https://fierce-cove-29863.herokuapp.com/getAllUsers/{pageNumber}")
        .addPathParameter("pageNumber", "0")
        .addQueryParameter("limit", "3")
        .addHeaders("token", "1234")
        .setTag("test")
        .setPriority(Priority.LOW)
        .build()
        .getAsJSONArray(new JSONArrayRequestListener() {
@Override
public void onResponse(JSONArray response) {
        // do anything with response
        }
@Override
public void onError(ANError error) {
        // handle error
        }
        });
*/
/* Post
AndroidNetworking.post("https://fierce-cove-29863.herokuapp.com/createAnUser")
        .addBodyParameter("firstname", "Amit")
        .addBodyParameter("lastname", "Shekhar")
        .setTag("test")
        .setPriority(Priority.MEDIUM)
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
@Override
public void onResponse(JSONObject response) {
        // do anything with response
        }
@Override
public void onError(ANError error) {
        // handle error
        }
        });
*/
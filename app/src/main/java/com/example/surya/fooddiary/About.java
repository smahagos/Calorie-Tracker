package com.example.surya.fooddiary;

/**
 * Created by surya on 5/22/2017.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bluelinelabs.logansquare.LoganSquare;
import com.facebook.login.LoginManager;


public class About extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {

            //noinspection SimplifiableIfStatement
            case R.id.logout:
                try {
                    User me = LoganSquare.parse(getValueFromKey("me"), User.class);
                    if(me.fb_id != null && !me.fb_id.equals("")) {
                        LoginManager.getInstance().logOut();
                    }
                    saveKeyValue("me", "");

                    startActivity(new Intent(mContext, LoginActivity.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.About:
                Intent intent = new Intent(About.this,About.class);
                About.this.startActivity(intent);
                break;

            case R.id.AddMeal:
                Intent intent1 = new Intent(About.this,AddMeal.class);
                About.this.startActivity(intent1);
                break;

            case R.id.DeleteMeal:
                Intent intent2 = new Intent(About.this,DeleteMeal.class);
                About.this.startActivity(intent2);
                break;
            case R.id.ReviewMeals:
                Intent intent3 = new Intent(About.this,ReviewMeal.class);
                About.this.startActivity(intent3);
                break;
        }
        return true;
    }
}

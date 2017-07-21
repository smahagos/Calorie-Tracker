package com.example.surya.fooddiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bluelinelabs.logansquare.LoganSquare;
import com.facebook.login.LoginManager;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        ButterKnife.bind(this);
    }

    @OnClick(R.id.addBtn) public void onAddMealClick() {
        Intent intent1 = new Intent(MainActivity.this,AddMeal.class);
        MainActivity.this.startActivity(intent1);
    }

    @OnClick(R.id.delBtn) public void onDeleteClick() {
        Intent intent2 = new Intent(MainActivity.this,DeleteMeal.class);
        MainActivity.this.startActivity(intent2);
    }

    @OnClick(R.id.reviewBtn) public void onReviewClick() {
        Intent intent3 = new Intent(MainActivity.this,ReviewMeal.class);
        MainActivity.this.startActivity(intent3);
    }

    @OnClick(R.id.logoutBtn) public void onLogout() {
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
                Intent intent = new Intent(MainActivity.this,About.class);
                MainActivity.this.startActivity(intent);
                break;

            case R.id.AddMeal:
                Intent intent1 = new Intent(MainActivity.this,AddMeal.class);
                MainActivity.this.startActivity(intent1);
                break;

            case R.id.DeleteMeal:
                Intent intent2 = new Intent(MainActivity.this,DeleteMeal.class);
                MainActivity.this.startActivity(intent2);
                break;
            case R.id.ReviewMeals:
                Intent intent3 = new Intent(MainActivity.this,ReviewMeal.class);
                MainActivity.this.startActivity(intent3);
                break;
        }
        return true;
    }
}

package com.example.surya.fooddiary;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bluelinelabs.logansquare.LoganSquare;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewMeal extends BaseActivity {

    @BindView(R.id.startDateTV)
    TextView startDateTV;

    @BindView(R.id.endDateTV)
    TextView endDateTV;

    @BindView(R.id.listView)
    ListView listView;

    @BindView(R.id.need_improvement)
    RadioButton need_improvement;

    @BindView(R.id.good_progress)
    RadioButton good_progress;

    @BindView(R.id.commentET)
    EditText commentET;

    ListViewAdapter adapter;
    Calendar c;
    int year, month, day;
    static int hour, min;

    List<Food> mFoods = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_meal);

        ButterKnife.bind(this);
        mContext = this;

        c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
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
                Intent intent = new Intent(ReviewMeal.this,About.class);
                ReviewMeal.this.startActivity(intent);
                break;

            case R.id.AddMeal:
                Intent intent1 = new Intent(ReviewMeal.this,AddMeal.class);
                ReviewMeal.this.startActivity(intent1);
                break;

            case R.id.DeleteMeal:
                Intent intent2 = new Intent(ReviewMeal.this,DeleteMeal.class);
                ReviewMeal.this.startActivity(intent2);
                break;
            case R.id.ReviewMeals:
                break;
        }
        return true;
    }

    @OnClick(R.id.startBtn) public void onStartDate() {
        DatePickerDialog dd = new DatePickerDialog(ReviewMeal.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        try {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ss");
                            String dateInString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            Date date = formatter.parse(dateInString);

                            startDateTV.setText(formatter.format(date).toString());

                        } catch (Exception ex) {

                        }


                    }
                }, year, month, day);
        dd.show();
    }

    @OnClick(R.id.endBtn) public void onEndDate() {
        DatePickerDialog dd = new DatePickerDialog(ReviewMeal.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        try {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ss");
                            String dateInString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            Date date = formatter.parse(dateInString);

                            endDateTV.setText(formatter.format(date).toString());

                        } catch (Exception ex) {

                        }


                    }
                }, year, month, day);
        dd.show();
    }

    @OnClick(R.id.goBtn) public void onGo() {
        if(!startDateTV.getText().toString().equals("")) {
            getFoods();
        } else {
            showToast("Please select date");
        }
    }

    @OnClick(R.id.saveBtn) public void onSave() {
        if(!need_improvement.isChecked() && !good_progress.isChecked()) {
            showToast("Please select one of type");
            return;
        } else if (commentET.getText().toString().equals("")) {
            showToast("Please input comment");
            return;
        }
        try {
            User me = LoganSquare.parse(getValueFromKey("me"), User.class);
            String start = startDateTV.getText().toString() + " 00:00:00";
            String end = endDateTV.getText().toString() + " 23:59:59";
            String opt = "";
            if(need_improvement.isChecked()) {
                opt = "need_improvement";
            } else {
                opt = "good_progress";
            }
            String comment = commentET.getText().toString();

            showProgressDialog();
            AndroidNetworking.post(MyApplication.BASE_URL + "food/reviewFoods")
                    .addBodyParameter("userid", me.userid)
                    .addBodyParameter("start", start)
                    .addBodyParameter("end", end)
                    .addBodyParameter("opt", opt)
                    .addBodyParameter("comment", comment)
                    .setTag("getFoods")
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
                                    try {
                                        startDateTV.setText("");
                                        endDateTV.setText("");
                                        commentET.setText("");
                                        need_improvement.setChecked(false);
                                        good_progress.setChecked(false);
                                        mFoods = new ArrayList<Food>();
                                        adapter = new ListViewAdapter(mContext, mFoods);
                                        listView.setAdapter(adapter);
                                        showToast("Saved successfully");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFoods () {
        try {
            User me = LoganSquare.parse(getValueFromKey("me"), User.class);
            String start = startDateTV.getText().toString() + " 00:00:00";
            String end = endDateTV.getText().toString() + " 23:59:59";

            showProgressDialog();
            AndroidNetworking.post(MyApplication.BASE_URL + "food/getFoods")
                    .addBodyParameter("userid", me.userid)
                    .addBodyParameter("start", start)
                    .addBodyParameter("end", end)
                    .setTag("getFoods")
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
                                    try {
                                        mFoods = LoganSquare.parseList(response.getString("data"), Food.class);
                                        adapter = new ListViewAdapter(mContext, mFoods);
                                        listView.setAdapter(adapter);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final class ListViewAdapter extends BaseAdapter {

        private String TAG = "HorizontalListViewAdapter";
        private List<Food> foods = null;
        private Context context;

        public ListViewAdapter(Context con, List<Food> fs) {
            super();
            context = con;
            foods = fs;
        }

        @Override
        public int getCount() {
            return foods.size();
        }

        @Override
        public Object getItem(int position) {
            return foods.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                v = LayoutInflater.from(context).inflate(R.layout.del_list_item, parent, false);
            }

            if (v != null) {
                Food food = foods.get(position);
                ImageView iv = (ImageView) v.findViewById(R.id.iv);
                TextView nameTV = (TextView) v.findViewById(R.id.nameTV);
                TextView dateTV = (TextView) v.findViewById(R.id.dateTV);

                nameTV.setText(food.name);
                dateTV.setText(food.eat_time.substring(0, 16));
                if(food.photo != null && !food.photo.equals("")){
                    Picasso.with(context).load(MyApplication.IMAGE_BASE_URL+food.photo).into(iv);
                }
            }
            return v;
        }

        public void add(Food food) {
            foods.add(food);
            notifyDataSetChanged();
        }
    }
}

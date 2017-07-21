package com.example.surya.fooddiary;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bluelinelabs.logansquare.LoganSquare;
import com.facebook.login.LoginManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddMeal extends BaseActivity {

    static int hour, min;

    @BindView(R.id.nameET)
    EditText nameET;
    @BindView(R.id.catET)
    EditText catET;
    @BindView(R.id.commentET)
    EditText commentET;

    ImageView meal_image_view;
    TextView txtdate, txttime;
    Button btntimepicker, btndatepicker;
    HorizontalListView hListView;
    HorizontalListViewAdapter adapter;

    java.sql.Time timeValue;
    SimpleDateFormat format;
    Calendar c;
    int year, month, day;

    private Uri mCropImageUri;
    private File mFile = null;

    String name, cat, comment, date, time;
    ArrayList<File> images = new ArrayList<File>();
    ArrayList<Uri> uris = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        ButterKnife.bind(this);
        mContext = this;

        meal_image_view = (ImageView) findViewById(R.id.meal_image_view);

        btndatepicker = (Button) findViewById(R.id.add_date);
        btntimepicker = (Button) findViewById(R.id.add_time);

        txtdate = (TextView) findViewById(R.id.txtdate);
        txttime = (TextView) findViewById(R.id.txttime);

        c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        hListView = (HorizontalListView) findViewById(R.id.listview);
        adapter = new HorizontalListViewAdapter(mContext, uris);
        hListView.setAdapter(adapter);

        Button addphoto = (Button) findViewById(R.id.take_photo);

        btndatepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date

                DatePickerDialog dd = new DatePickerDialog(AddMeal.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                try {
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ss");
                                    String dateInString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                    Date date = formatter.parse(dateInString);

                                    txtdate.setText(formatter.format(date).toString());

                                } catch (Exception ex) {

                                }


                            }
                        }, year, month, day);
                dd.show();
            }
        });
        btntimepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TimePickerDialog td = new TimePickerDialog(AddMeal.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                try {
                                    String dtStart = String.valueOf(hourOfDay) + ":" + String.valueOf(minute)+ ":" + String.valueOf("00");
                                    format = new SimpleDateFormat("HH:mm:ss");

                                    timeValue = new java.sql.Time(format.parse(dtStart).getTime());
                                    txttime.setText(String.valueOf(timeValue));
                                   /* String amPm = hourOfDay % 12 + ":" + minute + " " + ((hourOfDay >= 12) ? "PM" : "AM");
                                    txttime.setText(amPm + "\n" + String.valueOf(timeValue));*/
                                } catch (Exception ex) {
                                    txttime.setText(ex.getMessage().toString());
                                }
                            }
                        },
                        hour, min,
                        DateFormat.is24HourFormat(AddMeal.this)
                );
                td.show();
            }
        });


        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.startPickImageActivity(AddMeal.this);
            }
        });
    }

    @OnClick(R.id.insert)
    public void onUpload() {

        name = nameET.getText().toString();
        cat = catET.getText().toString();
        comment = commentET.getText().toString();
        date = txtdate.getText().toString();
        time = txttime.getText().toString();

        if (name.equals("")) {
            showToast("Please input food name");
            return;
        }

        if (cat.equals("")) {
            showToast("Please input category");
            return;
        }

        if (comment.equals("")) {
            showToast("Please input comment");
            return;
        }

        if (date.equals("")) {
            showToast("Please select date");
            return;
        }

        if (time.equals("")) {
            showToast("Please select time");
            return;
        }

        String eatTime = date + " " + time;

        try {
            User me = LoganSquare.parse(getValueFromKey("me"), User.class);

            showProgressDialog();
            ANRequest.MultiPartBuilder builder = AndroidNetworking.upload(MyApplication.BASE_URL + "food/uploadFood");
            int count = images.size();

            for (int i = 0; i < count; i++) {
                String fileName = "file" + i;
                builder.addMultipartFile(fileName, images.get(i));
            }

            builder.addMultipartParameter("userid", me.userid)
                    .addMultipartParameter("name", name)
                    .addMultipartParameter("cat", cat)
                    .addMultipartParameter("comment", comment)
                    .addMultipartParameter("eat_time", eatTime)
                    .addMultipartParameter("count", images.size()+"")
                    .setPriority(Priority.LOW)
                    .setTag("publish")
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideProgressDialog();
                            try {
                                String status = response.getString("status");
                                if (status.equals("1")) {
                                    showToast("Success!");
                                    nameET.setText("");
                                    catET.setText("");
                                    commentET.setText("");
                                    txtdate.setText("");
                                    txttime.setText("");
                                    images = new ArrayList<File>();
                                    uris = new ArrayList<Uri>();
                                    adapter = new HorizontalListViewAdapter(mContext, uris);
                                    hListView.setAdapter(adapter);
                                } else if (status.equals("0")) {
                                    String error = response.getString("error");
                                    showToast(error);
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
                            showToast(error.getErrorBody().toString());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            showToast("Please login again");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri uri = result.getUri();
                meal_image_view.setImageURI(uri);
                mFile = new File(FileUtils.saveImageToInternalStorageFromUri(AddMeal.this, uri));
                images.add(mFile);
                uris.add(uri);
                //adapter.notifyDataSetChanged();
                //adapter.add(uri);
                adapter = new HorizontalListViewAdapter(mContext, uris);
                hListView.setAdapter(adapter);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(AddMeal.this, "Cropping failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            Toast.makeText(AddMeal.this, "Cancelling, required permissions are not granted", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Start crop image activity for the given image.
     **/
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
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
                Intent intent = new Intent(AddMeal.this, About.class);
                AddMeal.this.startActivity(intent);
                break;

            case R.id.AddMeal:
                //Intent intent1 = new Intent(AddMeal.this, AddMeal.class);
                //AddMeal.this.startActivity(intent1);
                break;

            case R.id.DeleteMeal:
                Intent intent2 = new Intent(AddMeal.this, DeleteMeal.class);
                AddMeal.this.startActivity(intent2);
                break;
            case R.id.ReviewMeals:
                Intent intent3 = new Intent(AddMeal.this, ReviewMeal.class);
                AddMeal.this.startActivity(intent3);
                break;
        }
        return true;
    }

    private final class HorizontalListViewAdapter extends BaseAdapter {

        private String TAG = "HorizontalListViewAdapter";
        private ArrayList<Uri> mImages = null;
        private Context context;

        public HorizontalListViewAdapter(Context con, ArrayList<Uri> uris) {
            super();
            context = con;
            mImages = uris;
        }

        @Override
        public int getCount() {
            return mImages.size();
        }

        @Override
        public Object getItem(int position) {
            return mImages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                v = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            }

            if (v != null) {
                ImageView iv = (ImageView) v.findViewById(R.id.iv);
                Uri uri = mImages.get(position);
                iv.setImageURI(uri);
            }
            return v;
        }

        public void add(Uri uri) {
            mImages.add(uri);
            notifyDataSetChanged();
        }
    }
}

package com.example.surya.fooddiary;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Administrator on 11/21/2016.
 */

@JsonObject
public class User {

    @JsonField(name = "id")
    public String userid;

    @JsonField(name = "one_id")
    public String one_id;

    @JsonField(name = "email")
    public String email;

    @JsonField(name = "username")
    public String username;

    @JsonField(name = "password")
    public String password;

    @JsonField(name = "user_type")
    public String user_type;

    @JsonField(name = "fb_id")
    public String fb_id;

    @JsonField(name = "tw_id")
    public String tw_id;

    @JsonField(name = "g_id")
    public String g_id;

    @JsonField(name = "photo")
    public String photo;

    @JsonField(name = "address")
    public String address;

    @JsonField(name = "lati")
    public String lati;

    @JsonField(name = "lang")
    public String lang;

    @JsonField(name = "description")
    public String desc;

    @JsonField(name = "birthday")
    public String birthday;

    @JsonField(name = "gender")
    public String gender;

    @JsonField(name = "phone")
    public String phone;

    @JsonField(name = "rating")
    public String rating;

    @JsonField(name = "count5")
    public String count5;

    @JsonField(name = "count4")
    public String count4;

    @JsonField(name = "count3")
    public String count3;

    @JsonField(name = "count2")
    public String count2;

    @JsonField(name = "count1")
    public String count1;
}

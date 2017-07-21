package com.example.surya.fooddiary;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Administrator on 11/21/2016.
 */

@JsonObject
public class Food {

    @JsonField(name = "id")
    public String foodid;

    @JsonField(name = "user_id")
    public String userid;

    @JsonField(name = "name")
    public String name;

    @JsonField(name = "photo")
    public String photo;

    @JsonField(name = "description")
    public String description;

    @JsonField(name = "category")
    public String category;

    @JsonField(name = "eat_time")
    public String eat_time;

    @JsonField(name = "opt")
    public String opt;
}

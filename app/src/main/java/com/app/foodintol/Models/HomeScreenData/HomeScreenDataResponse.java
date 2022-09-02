package com.app.foodintol.Models.HomeScreenData;

import com.app.foodintol.Models.BaseResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HomeScreenDataResponse extends BaseResponse {

    @SerializedName("children_count")
    private String childrenCount;

    @SerializedName("children_list")
    private ArrayList<HomeScreenDataResponseChildren> children;

    public String getChildrenCount() {
        return childrenCount;
    }

    public ArrayList<HomeScreenDataResponseChildren> getChildren() {
        return children;
    }
}

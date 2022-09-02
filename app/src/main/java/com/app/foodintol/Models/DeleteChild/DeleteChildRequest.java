package com.app.foodintol.Models.DeleteChild;

import com.google.gson.annotations.SerializedName;

public class DeleteChildRequest {

    @SerializedName("child_id")
    private String childId;

    public void setChildId(String childId) {
        this.childId = childId;
    }
}

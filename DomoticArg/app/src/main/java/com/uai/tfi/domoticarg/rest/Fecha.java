
package com.uai.tfi.domoticarg.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fecha {

    @SerializedName("type")
    @Expose
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
package com.gianlu.zonevision.NetIO;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Domain implements Serializable {
    public final String name;

    public Domain(JSONObject obj) throws JSONException {
        name = obj.getString("name");

        System.out.println(obj); // TODO
    }
}

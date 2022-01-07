package com.prabhutech.prabhupay_sdk.utils;

import com.google.gson.JsonElement;

/**
 * Created by Niken on 7/31/2020.
 */
public class JsonUtils {

    public static String safeString(JsonElement ele, String fallBack){
        return (ele == null)? fallBack: ele.getAsString();
    }
}

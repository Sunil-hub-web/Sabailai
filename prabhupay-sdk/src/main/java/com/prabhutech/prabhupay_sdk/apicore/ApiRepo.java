package com.prabhutech.prabhupay_sdk.apicore;

import android.content.Context;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Niken on 7/30/2020.
 */
public class ApiRepo {

    public Observable<JsonObject> initiateTransaction(Context context, JsonObject req){
             return new RestApi().getApiServices().initiate(req).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).map(new Function<JsonObject, JsonObject>() {
                @Override
                public JsonObject apply(JsonObject res) throws Exception {
                    if (res.get("success").getAsBoolean()) {
                        return res;
                    } else {
                        throw new Error(res.get("message").getAsString());
                    }
                }
            });
    }

    public Observable<JsonObject> getTrxnDetail(Context context, JsonObject req){
        return new RestApi().getApiServices().trxnDetail(req).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).map(new Function<JsonObject, JsonObject>() {
            @Override
            public JsonObject apply(JsonObject res) throws Exception {
                if (res.get("success").getAsBoolean()) {
                    return res;
                } else {
                    throw new Error(res.get("message").getAsString());
                }
            }
        });
    }


    public Observable<JsonObject> login(Context context, JsonObject req){
        return new RestApi().getApiServices().trxnLogin(req).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).map(new Function<JsonObject, JsonObject>() {
            @Override
            public JsonObject apply(JsonObject res) throws Exception {
                if (res.get("success").getAsBoolean() && res.get("status").getAsString().equals("00")) {
                    return res;
                } else {
                    throw new Error(res.get("message").getAsString());
                }
            }
        });
    }

    public Observable<JsonObject> confirmPayment(Context context, JsonObject req){
        return new RestApi().getApiServices().trxnConfirm(req).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).map(new Function<JsonObject, JsonObject>() {
            @Override
            public JsonObject apply(JsonObject res) throws Exception {
                if (res.get("success").getAsBoolean()) {
                    return res;
                } else {
                    throw new Error(res.get("message").getAsString());
                }
            }
        });
    }

}

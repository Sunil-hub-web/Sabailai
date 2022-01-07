package com.prabhutech.prabhupay_sdk.apicore;

import com.google.gson.JsonObject;


import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Niken on 7/30/2020.
 */
public interface ApiServices {

    @POST(ApiUrl.Initiate)
    Observable<JsonObject> initiate(@Body JsonObject req);

    @POST(ApiUrl.trxnDetail)
    Observable<JsonObject> trxnDetail(@Body JsonObject req);

    @POST(ApiUrl.trxnLogin)
    Observable<JsonObject> trxnLogin(@Body JsonObject req);

    @POST(ApiUrl.trxnConfirm)
    Observable<JsonObject> trxnConfirm(@Body JsonObject req);

}

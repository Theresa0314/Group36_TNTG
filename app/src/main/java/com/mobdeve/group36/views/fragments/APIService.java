package com.mobdeve.group36.views.fragments;

import com.mobdeve.group36.Data.firebase.MessageSent;
import com.mobdeve.group36.Data.firebase.Response;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService  {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA3RUjeQw:APA91bG_YT2o_gT5WmihQ7VOWpxSioHuCb5kFKyVsrQAESwfBmTJvW3w39aTAgiK6gFwzXXNbecVmuYcUICOOScqrkavklHtkpaxgKiOMIYj9SPtN3uXrSRN4sMqnrDa1hqfQrbS8RBn"
            }
    )

    @POST("fcm/send")
    Call<Response> sendNotification(@Body MessageSent body);
}

package com.ankurupadhyay.finaljffadmin.Notifications;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface IFCMApi {


    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAx_Uv_og:APA91bGAM9V7zCwrU8VZSoYkODiSU1r7Gu3o78UlLts4Q4GHj5hsSN_b2TM9uwdG_C3e7Kw5gSvk3QUOsfkTvPt5ATVVR-yFUqPws11IKiXWS8SWUOXLyMeyJk1rpYUPBkb7mZGIYmnT"
    })


    @POST("fcm/send")
    Observable<FCMResponse> sendNotification(@Body FCMSendData body);




}

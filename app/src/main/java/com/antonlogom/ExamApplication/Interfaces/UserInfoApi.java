package com.antonlogom.ExamApplication.Interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserInfoApi {
    @GET("user")
    Call<Object> getUserInfo(@Query("access_token") String access_token);
}

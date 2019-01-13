package com.antonlogom.ExamApplication.Models;

import com.antonlogom.ExamApplication.Interfaces.UserInfoApi;
import com.antonlogom.ExamApplication.Interfaces.UserInfoCallback;
import com.google.gson.Gson;
import com.antonlogom.ExamApplication.Application;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainModel{

    public void requestUserInfo(String token, final UserInfoCallback callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserInfoApi client = retrofit.create(UserInfoApi.class);

        Call call = client.getUserInfo(token);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                JSONObject user;
                HashMap<String, String> data= new HashMap<>();
                String gson = new Gson().toJson(response.body());
                try {
                    user = new JSONObject(gson);

                    data.put("login", user.getString("login"));
                    data.put("url", user.getString("url"));
                    data.put("avatar_url", user.getString("avatar_url"));
                } catch (JSONException e) {
                    callback.onUserInfoFailure(e.toString());
                }

                callback.onUserInfoResponse(data);


            }

            @Override
            public void onFailure(Call call, Throwable t) {
                callback.onUserInfoFailure(call.toString());
            }
        });
    }
}

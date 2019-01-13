package com.antonlogom.ExamApplication.Models;

import com.antonlogom.ExamApplication.Application;
import com.antonlogom.ExamApplication.AccessToken;
import com.antonlogom.ExamApplication.GitCl;
import com.antonlogom.ExamApplication.Interfaces.GetAccesTokenCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class GitHubModel {

    public void getAccessToken(String client_id, String clientSecret, String code, final GetAccesTokenCallback callback) {

        GitCl client = new Retrofit.Builder()
                .baseUrl("https://github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GitCl.class);

        Call<AccessToken> accessTokenCall = client.getAccessToken(
                client_id,
                clientSecret,
                code
        );

        accessTokenCall.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                callback.onResponseSuccess(response);
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                callback.onResponseFailure(t.toString());
            }
        });

    }
}

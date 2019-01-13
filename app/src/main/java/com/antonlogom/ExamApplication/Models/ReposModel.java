package com.antonlogom.ExamApplication.Models;

import com.antonlogom.ExamApplication.Interfaces.OnReposLoadedCallback;
import com.antonlogom.ExamApplication.Interfaces.ReposApi;
import com.google.gson.Gson;
import com.antonlogom.ExamApplication.Application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReposModel{

    public void getRepos(String token, final OnReposLoadedCallback callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ReposApi client = retrofit.create(ReposApi.class);

        Call call = client.getRepos(token);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                JSONArray repos = null;
                ArrayList<HashMap<String, String>> data = new ArrayList<>();
                try {
                    String gson = new Gson().toJson(response.body());
                    repos = new JSONArray(gson);

                    for (int i = 0; i < repos.length(); i++){
                        JSONObject repository = repos.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<>();

                        String name = repository.getString("name");
                        String description = "";
                        try {
                            repository.getString("description");
                        }catch (JSONException jException){
                            jException.printStackTrace();
                        }


                        map.put("name", name);
                        map.put("description", description);

                        data.add(map);

                    }
                } catch (JSONException e) {
                    callback.onReposFailure(e.toString());
                }

                callback.onReposResponse(data);

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                callback.onReposFailure(call.toString());
            }
        });
    }
}

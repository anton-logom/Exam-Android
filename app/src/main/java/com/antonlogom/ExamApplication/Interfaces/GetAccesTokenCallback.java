package com.antonlogom.ExamApplication.Interfaces;

import com.antonlogom.ExamApplication.AccessToken;

import retrofit2.Response;

public interface GetAccesTokenCallback {
    void onResponseFailure(String errorMessage);
    void onResponseSuccess(Response<AccessToken> response);
}

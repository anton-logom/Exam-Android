package com.antonlogom.ExamApplication.Interfaces;

import java.util.HashMap;

public interface UserInfoCallback {
    void onUserInfoResponse(HashMap<String, String> user);
    void onUserInfoFailure(String errorMessage);
}

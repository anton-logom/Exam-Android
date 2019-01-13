package com.antonlogom.ExamApplication.Interfaces;

import java.util.ArrayList;
import java.util.HashMap;

public interface OnReposLoadedCallback {
    void onReposResponse(ArrayList<HashMap<String, String>> data);
    void onReposFailure(String errorMessage);
}

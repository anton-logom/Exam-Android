package com.antonlogom.ExamApplication.Interfaces;

import java.util.ArrayList;
import java.util.HashMap;

public interface IReposView {
    void showToast(String message);
    void setData(ArrayList<HashMap<String, String>> data);
}

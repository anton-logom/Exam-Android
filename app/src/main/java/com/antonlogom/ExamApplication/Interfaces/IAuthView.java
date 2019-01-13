package com.antonlogom.ExamApplication.Interfaces;


public interface IAuthView {
    void authorizeUser(String client_id, String redirect_url);
    String getToken();
    void saveToken(String token);
    void showMainActivity(String token);
    void showError();
    void showToast(String message);
}

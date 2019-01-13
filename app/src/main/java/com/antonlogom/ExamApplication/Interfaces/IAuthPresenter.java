package com.antonlogom.ExamApplication.Interfaces;


public interface IAuthPresenter {
    void attachView(IAuthView view);
    void detachView();
    void onLoginClicked();
    void authSuccess(String code);
    void authError();
}

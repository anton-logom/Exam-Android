package com.antonlogom.ExamApplication.Interfaces;

public interface ICameraPresenter {
    void attachView(ICameraView view);
    void detachView();
    void photoClick();
    void prepareCamera();
}

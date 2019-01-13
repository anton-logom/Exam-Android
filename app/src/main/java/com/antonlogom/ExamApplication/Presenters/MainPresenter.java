package com.antonlogom.ExamApplication.Presenters;

import com.antonlogom.ExamApplication.Interfaces.IMainPresenter;
import com.antonlogom.ExamApplication.Interfaces.IMainView;
import com.antonlogom.ExamApplication.Interfaces.UserInfoCallback;
import com.antonlogom.ExamApplication.Models.MainModel;

import java.util.HashMap;

public class MainPresenter implements IMainPresenter, UserInfoCallback {
    private MainModel model;
    private IMainView view;

    public MainPresenter(MainModel model, IMainView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void loadUserInfo(String token) {
        model.requestUserInfo(token, this);
    }


    @Override
    public void onUserInfoResponse(HashMap<String, String> user) {
        if (view != null){
            view.setUserInfo(user.get("login"), user.get("url"), user.get("avatar_url"));
        }
    }

    @Override
    public void onUserInfoFailure(String errorMessage) {
        if (view != null){
            view.showToast(errorMessage);
        }

    }
}

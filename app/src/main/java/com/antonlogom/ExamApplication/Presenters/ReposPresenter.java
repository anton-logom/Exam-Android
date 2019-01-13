package com.antonlogom.ExamApplication.Presenters;

import com.antonlogom.ExamApplication.Interfaces.IReposPresenter;
import com.antonlogom.ExamApplication.Interfaces.IReposView;
import com.antonlogom.ExamApplication.Interfaces.OnReposLoadedCallback;
import com.antonlogom.ExamApplication.Models.ReposModel;

import java.util.ArrayList;
import java.util.HashMap;

public class ReposPresenter implements IReposPresenter, OnReposLoadedCallback {
    ReposModel model;
    IReposView view;

    public ReposPresenter(ReposModel model, IReposView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void loadRepos(String token) {
        model.getRepos(token, this);
    }

    @Override
    public void onReposResponse(ArrayList<HashMap<String, String>> data) {
        if (view != null){
            view.setData(data);
        }
    }

    @Override
    public void onReposFailure(String errorMessage) {
        if (view != null){
            view.showToast(errorMessage);
        }
    }
}

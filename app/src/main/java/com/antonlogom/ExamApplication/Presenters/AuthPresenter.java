package com.antonlogom.ExamApplication.Presenters;

import com.antonlogom.ExamApplication.AccessToken;
import com.antonlogom.ExamApplication.Models.GitHubModel;
import com.antonlogom.ExamApplication.Interfaces.GetAccesTokenCallback;
import com.antonlogom.ExamApplication.Interfaces.IAuthPresenter;
import com.antonlogom.ExamApplication.Interfaces.IAuthView;

import retrofit2.Response;

import static com.antonlogom.ExamApplication.TokensHolder.CLIENT_ID;
import static com.antonlogom.ExamApplication.TokensHolder.CLIENT_SECRET;
import static com.antonlogom.ExamApplication.TokensHolder.REDIRECT_URI;

public class AuthPresenter implements IAuthPresenter, GetAccesTokenCallback {
    private IAuthView view;
    private GitHubModel model;


    public AuthPresenter(GitHubModel model) {
        this.model = model;
    }

    @Override
    public void attachView(IAuthView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void onLoginClicked() {
        String token = view.getToken();
        if (token == null){
            view.authorizeUser(CLIENT_ID, REDIRECT_URI);
        }else{
            view.showMainActivity(token);
        }

    }

    @Override
    public void authSuccess(String code) {
      //  if (url.startsWith("testapp://")) { Intent intent = new Intent(Intent.ACTION_VIEW); intent.setData( Uri.parse(url) ); view.getContext().startActivity( intent ); return true; //with return true, the webview wont try rendering the url } return false; } } );
        model.getAccessToken(CLIENT_ID, CLIENT_SECRET, code, this);
    }

    @Override
    public void authError() {
        view.showError();
    }


    @Override
    public void onResponseFailure(String errorMessage) {
        if (view != null){
            view.showToast(errorMessage);
        }

    }

    @Override
    public void onResponseSuccess(Response<AccessToken> response) {
        if (response != null && response.body() != null) {
            if (view != null){
                String token = response.body().getAccessToken();
                view.saveToken(token);
                view.showMainActivity(token);
            }
        }else{
            onResponseFailure("Empty body");
        }
    }
}

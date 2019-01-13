package com.antonlogom.ExamApplication.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.antonlogom.ExamApplication.Models.GitHubModel;
import com.antonlogom.ExamApplication.TokensHolder;
import com.antonlogom.ExamApplication.Interfaces.IAuthPresenter;
import com.antonlogom.ExamApplication.Interfaces.IAuthView;
import com.antonlogom.ExamApplication.Presenters.AuthPresenter;
import com.antonlogom.ExamApplication.R;

import static com.antonlogom.ExamApplication.Application.PREF_TOKEN;

public class AuthView extends AppCompatActivity implements IAuthView {
    public static final String PREFERENCES = "myPreferences";


    private SharedPreferences sp;

    private IAuthPresenter presenter;

    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_view);

        presenter = new AuthPresenter(new GitHubModel());
        presenter.attachView(this);

        login = findViewById(R.id.auth_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onLoginClicked();
            }
        });
        sp = getSharedPreferences(PREFERENCES, MODE_PRIVATE);

    }

    @Override
    public void authorizeUser(String client_id, String redirect_url) {
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://github.com/login/oauth/authorize" + "?client_id=" + client_id
                        + "&scope=repo&redirect_uri=" + redirect_url));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK){
            Log.d(getLocalClassName(), "RESULT_OK");
        }
    }

    @Override
    public String getToken() {
        return sp.getString(PREF_TOKEN, null);
    }

    @Override
    public void saveToken(String token) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREF_TOKEN, token);
        editor.apply();
    }

    @Override
    public void showMainActivity(String token) {
        Intent intent = new Intent(AuthView.this, MainActivity.class);
        intent.putExtra(PREF_TOKEN, token);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void showError() {
        Toast.makeText(this, "Error connection", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = getIntent().getData();



        if (uri != null && uri.toString().startsWith(TokensHolder.REDIRECT_URI)) {
            Log.d(getLocalClassName(), uri.toString());
            String code = uri.getQueryParameter("code");
            if (code != null) {
                presenter.authSuccess(code);
            } else if (uri.getQueryParameter("error") != null) {
                presenter.authError();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


}

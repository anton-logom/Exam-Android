package com.antonlogom.ExamApplication;

import com.yandex.mapkit.MapKitFactory;

public class Application extends android.app.Application {

    public static final String PREF_TOKEN = "token";

    @Override
    public void onCreate() {
        super.onCreate();
        MapKitFactory.setApiKey(TokensHolder.API_MAP_KEY);

    }

}

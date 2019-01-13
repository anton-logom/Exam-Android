package com.antonlogom.ExamApplication.Presenters;

import com.antonlogom.ExamApplication.Interfaces.ICameraPresenter;
import com.antonlogom.ExamApplication.Interfaces.ICameraView;

import java.io.File;
import java.io.IOException;

public class CameraPresenter implements ICameraPresenter {

    private ICameraView view;

    @Override
    public void attachView(ICameraView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void photoClick() {
        if (view != null){
            view.checkPermission();
        }
    }

    @Override
    public void prepareCamera() {
        if (view != null){
            try {
                File file = view.createImageFile();
                if (file != null){
                    view.openCamera(file);
                }else{
                    view.showToast("Error creating file");
                }

            } catch (IOException e) {
                view.showToast("Error creating file");
            }

        }
    }


}

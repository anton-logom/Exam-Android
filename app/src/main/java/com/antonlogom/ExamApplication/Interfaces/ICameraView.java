package com.antonlogom.ExamApplication.Interfaces;

import java.io.File;
import java.io.IOException;

public interface ICameraView {
    void checkPermission();
    void openCamera(File file);
    void showToast(String message);
    File createImageFile() throws IOException;
    void scanMediaForImage();
}

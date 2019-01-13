package com.antonlogom.ExamApplication.Views;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.antonlogom.ExamApplication.Interfaces.ICameraPresenter;
import com.antonlogom.ExamApplication.Interfaces.ICameraView;
import com.antonlogom.ExamApplication.Presenters.CameraPresenter;
import com.antonlogom.ExamApplication.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.SENSOR_SERVICE;


public class Fragment extends android.support.v4.app.Fragment implements View.OnClickListener, ICameraView {

    private TextView valueX, valueY, valueZ;
    private ImageView photo;
    private Button takePhoto;
    private SensorManager sensorManager;

    private static final int CAMERA_REQUEST = 0;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    private ICameraPresenter presenter;
    private Sensor sensor;
    private Uri photoURI;
    private String mCurrentPhotoPath;


    public static Fragment newInstance() {
        return new Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CameraPresenter();
        presenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_sensor_view, container, false);

        valueX = view.findViewById(R.id.valueX);
        valueY = view.findViewById(R.id.valueY);
        valueZ = view.findViewById(R.id.valueZ);
        photo = view.findViewById(R.id.photo_from_camera);
        takePhoto = view.findViewById(R.id.takePhoto);
        takePhoto.setOnClickListener(this);


        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView();
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listenerAccelerometer, sensor);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(listenerAccelerometer, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.takePhoto){
            presenter.photoClick();
        }
    }


    SensorEventListener listenerAccelerometer = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            valueX.setText( String.valueOf(event.values[0]));
            valueY.setText(String.valueOf(event.values[1]));
            valueZ.setText(String.valueOf(event.values[2]));
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photo.setImageURI(photoURI);
            scanMediaForImage();
        }
    }


    @Override
    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_CONTACTS)) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Permission needed")
                        .setMessage("This permission is needed for the contacts list")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                            }
                        })
                        .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        } else{
            presenter.prepareCamera();
        }
    }

    @Override
    public void openCamera(File file) {
        Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (imageIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            photoURI = FileProvider.getUriForFile(getActivity(),
                    "com.example.android.provider",
                    file);
            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(imageIntent, CAMERA_REQUEST);
        }

    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "JPEG_" + timeStamp + "_";
        File storageDirectory = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image;
        image = File.createTempFile(imageName, ".jpg", storageDirectory);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void scanMediaForImage() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Toast.makeText(getActivity(), "onRequestPermissionsResult", Toast.LENGTH_SHORT).show();
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                presenter.prepareCamera();
            }
        }
    }
}

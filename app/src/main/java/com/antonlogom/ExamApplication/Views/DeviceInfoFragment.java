package com.antonlogom.ExamApplication.Views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.antonlogom.ExamApplication.R;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.TELEPHONY_SERVICE;

public class DeviceInfoFragment extends Fragment {

    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 3;
    private ArrayList<HashMap<String, String>> data;
    private ListView infoList;

    private TelephonyManager telephonyManager;;

    public static DeviceInfoFragment newInstance() {
        return new DeviceInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new ArrayList<>();
        telephonyManager = (TelephonyManager) getActivity().getSystemService(TELEPHONY_SERVICE);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_device_info, container, false);

        infoList = view.findViewById(R.id.info_list);

        checkPermission();

        return view;
    }


    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_PHONE_STATE)) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Permission needed")
                        .setMessage("This permission is needed for the contacts list")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.READ_PHONE_STATE},
                                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
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
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        } else{
            showInfo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Toast.makeText(getActivity(), "onRequestPermissionsResult", Toast.LENGTH_SHORT).show();
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showInfo();
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void showInfo() {
        HashMap<String, String> map = new HashMap<>();

        addElement("Версия Android", Build.VERSION.RELEASE);
        addElement("Брэнд", Build.BRAND);
        addElement("Процессор", Build.HARDWARE);
        addElement("Номер модели", Build.MODEL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            addElement("IMEI", telephonyManager.getImei());
        }

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), data,R.layout.info_device_item ,
                new String[]{"Name", "Value"},
                new int[]{R.id.info_name, R.id.info_value});
        infoList.setAdapter(adapter);

    }

    private void addElement(String name, String value){
        HashMap<String, String> map = new HashMap<>();
        map.put("Name", name);
        map.put("Value", value);
        data.add(map);

    }
}

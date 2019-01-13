package com.antonlogom.ExamApplication.Views;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.antonlogom.ExamApplication.Contacts.ContactsModel;
import com.antonlogom.ExamApplication.Interfaces.IContractsPresenter;
import com.antonlogom.ExamApplication.Interfaces.IContractsView;
import com.antonlogom.ExamApplication.Presenters.ContactsPresenter;
import com.antonlogom.ExamApplication.R;

import java.util.List;
import java.util.Map;


public class ContactsFragment extends Fragment implements IContractsView {

    private ListView lvContacts;

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    private static final String KEY_NAME = "name";
    private static final String KEY_NUMBER = "number";

    private final String[] from = new String[]{ KEY_NAME, KEY_NUMBER};
    private final int[] to = new int[]{R.id.contacts_name, R.id.contacts_phone};

    ContentResolver cr;
    IContractsPresenter presenter;

    // TODO: Rename and change types and number of parameters
    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ContactsPresenter(new ContactsModel(), this);
        cr = getActivity().getContentResolver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts_view, container, false);
        lvContacts = view.findViewById(R.id.lvContacts);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        checkPermission();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        presenter = null;
    }

    @Override
    public void showContacts(List<Map<String, String>> contacts) {
        lvContacts.setAdapter(new SimpleAdapter(getActivity(), contacts, R.layout.contacts_item, from, to));
    }


    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS)
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
                                        new String[]{Manifest.permission.READ_CONTACTS},
                                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
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
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            presenter.loadContacts(cr);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                presenter.loadContacts(cr);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }
}

package com.antonlogom.ExamApplication.Presenters;


import com.antonlogom.ExamApplication.Contacts.ContactsModel;
import com.antonlogom.ExamApplication.Interfaces.IContractsPresenter;
import com.antonlogom.ExamApplication.Interfaces.IContractsView;
import com.antonlogom.ExamApplication.Interfaces.onContactsLoadCallback;

import java.util.List;
import java.util.Map;

public class ContactsPresenter implements IContractsPresenter, onContactsLoadCallback {

    private ContactsModel model;
    private IContractsView view;

    public ContactsPresenter(ContactsModel model, IContractsView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void loadContacts(Object cursor) {
        model.getContacts(cursor, this);
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void onContactsLoaded(List<Map<String, String>> maps) {
        if (view != null){
            view.showContacts(maps);
        }
    }
}

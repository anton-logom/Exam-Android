package com.antonlogom.ExamApplication.Contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;

import com.antonlogom.ExamApplication.Interfaces.onContactsLoadCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsModel{

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_NUMBER = "number";


    public void getContacts(Object cr, onContactsLoadCallback callback) {
        LoadContactsTask task = new LoadContactsTask((ContentResolver) cr, callback);
        task.execute();
    }

    private static class LoadContactsTask extends AsyncTask<Void, Void, List<Map<String, String>>> {
        ContentResolver cr;
        onContactsLoadCallback callback;

        public LoadContactsTask(ContentResolver cr, onContactsLoadCallback callback) {
            this.cr = cr;
            this.callback = callback;
        }

        @Override
        protected List<Map<String, String>> doInBackground(Void... voids) {
            ArrayList<Map<String, String>> contacts = new ArrayList<>();
            Cursor cur = cr.query(android.provider.ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
            Map<String, String> map;
            if (cur != null && cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    map = new HashMap<>();
                    String id = cur.getString(cur.getColumnIndex(android.provider.ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(android.provider.ContactsContract.Contacts.DISPLAY_NAME));
                    map.put(KEY_NAME, name);

                    if (Integer.parseInt(cur.getString(
                            cur.getColumnIndex(android.provider.ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor pCur = cr.query(
                                android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                android.provider.ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                                new String[]{id}, null);
                        if (pCur != null && pCur.moveToNext()) {
                            String phone = pCur.getString(pCur.getColumnIndex(android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER));
                            map.put(KEY_NUMBER, phone);
                            contacts.add(map);
                            pCur.close();
                        }

                    }

                }
                cur.close();
            }
            return contacts;
        }

        @Override
        protected void onPostExecute(List<Map<String, String>> maps) {
            super.onPostExecute(maps);
            if (callback != null){
                callback.onContactsLoaded(maps);
            }
        }
    }

}

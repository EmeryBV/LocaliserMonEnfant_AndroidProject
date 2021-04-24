package com.example.localisermonenfant_enfant.activity.Contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import com.example.localisermonenfant_enfant.R;
import com.example.localisermonenfant_enfant.activity.RecyclerItemClickListener;
import com.example.localisermonenfant_enfant.activity.SMS.SmsActivity;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {
    private String TAG = "TAG";
    private static final int PERMS_CONTACT_ID = 1235;
    public ArrayList<Contacts> contactsArrayList = new ArrayList<>();
    boolean loadContact = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void getContactList() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        Log.i(TAG, "Name: " + name);
//                        Log.i(TAG, "Phone Number: " + phoneNo);
                        Contacts contactsObject = new Contacts(name,phoneNo);
//                        System.out.println(contactsObject.getName());
                        contactsArrayList.add(contactsObject);

                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkContactPermissions();

    }
    private void checkContactPermissions()
    {
        if(ActivityCompat.checkSelfPermission(this,  Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            {
                Log.i(TAG, "Contacts permission NOT granted");
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_CONTACTS,
                }, PERMS_CONTACT_ID);
                return;
            }
        }
       loadContact();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMS_CONTACT_ID){
            checkContactPermissions();
        }
    }

    void loadContact(){

        getContactList();
        setContentView(R.layout.activity_contact);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(
                new LinearLayoutManager
                        (this));
        ContactsAdapter  monAdapter = new ContactsAdapter(contactsArrayList);
        recyclerView.setAdapter(monAdapter);
        loadContact = true;

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getApplication(), SmsActivity.class);
                        intent.putExtra("phoneNumber", contactsArrayList.get(position).getNumber().replaceAll("\\s+",""));
                        intent.putExtra("name", contactsArrayList.get(position).getName());
//                        Toast.makeText(getApplicationContext(), "mon message" + contactsArrayList.get(position).getNumber(), Toast.LENGTH_SHORT).show();
//                        Log.i(TAG, "tel " + contactsArrayList.get(position).getNumber());
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

}

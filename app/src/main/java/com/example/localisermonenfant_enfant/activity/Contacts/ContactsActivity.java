package com.example.localisermonenfant_enfant.activity.Contacts;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.localisermonenfant_enfant.R;
import com.example.localisermonenfant_enfant.ServerAPI.Connection;
import com.example.localisermonenfant_enfant.activity.Authentification.Log_in;
import com.example.localisermonenfant_enfant.activity.MainMenu.MainMenu;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {


    public ArrayList<Contacts> contactsArrayList = new ArrayList<>();
    boolean loadContact = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadContact();

    }

    @Override
    public void onResume() {
        super.onResume();

    }


    void loadContact() {

            Log_in.c.GetContacts(getApplicationContext(), MainMenu.child, new Connection.GetContactsCallback() {
                @Override
                public void Success(ArrayList<Connection.Contact> contacts) {
                    for (Connection.Contact contact: contacts) {
                        contactsArrayList.add( new Contacts(contact.getId(),contact.getName(),contact.getNum()));
                    }

                    setContentView(R.layout.activity_contact);
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

                    recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    ContactsAdapter monAdapter = new ContactsAdapter(contactsArrayList);
                    recyclerView.setAdapter(monAdapter);
                    loadContact = true;
                }

                @Override
                public void Error() {
                    Toast.makeText(getApplicationContext(), "Erreur lors du chargement des contacts" , Toast.LENGTH_LONG).show();
                }
            });
    }


}

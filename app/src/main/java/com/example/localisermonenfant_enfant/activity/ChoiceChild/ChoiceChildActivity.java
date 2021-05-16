package com.example.localisermonenfant_enfant.activity.ChoiceChild;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.os.Debug;
import com.example.localisermonenfant_enfant.R;
import com.example.localisermonenfant_enfant.ServerAPI.Connection;
import com.example.localisermonenfant_enfant.activity.Authentification.Log_in;
import com.example.localisermonenfant_enfant.activity.Contacts.ContactsAdapter;

import java.util.ArrayList;

public class ChoiceChildActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_child);
        final Context context = getApplicationContext();
        Log_in.c.getChildren(getApplicationContext(), new Connection.GetChildrenCallback() {
            @Override

            public void Success(ArrayList<Connection.Child> children) {


                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                ChildAdapter monAdapter = new ChildAdapter(children);
                recyclerView.setAdapter(monAdapter);

            }

            @Override
            public void Error() {
                Toast.makeText(getApplicationContext(), "Erreur lors du chargement des contacts", Toast.LENGTH_LONG).show();
                Log.e("TAAAag", "Erreur ");
            }
        });
    }
}
package com.example.localisermonenfant_enfant.activity.ChoiceChild;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        Log.e("TAAAag", "Je suis la " );
        Log_in.c.getChildren(getApplicationContext(), new Connection.GetChildrenCallback() {
            @Override

            public void Success(ArrayList<Connection.Child> children) {
                Log.e("TAAefezAag", "Je suis la ");
                Log.e("TAAAag", "Je suis la "+ String.valueOf(children.size() ));
                Toast.makeText(getApplicationContext(), "Je suis la "  + String.valueOf(children.size() ), Toast.LENGTH_LONG).show();
//                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//                ChildAdapter monAdapter = new ChildAdapter(children);
//                recyclerView.setAdapter(monAdapter);

            }

            @Override
            public void Error() {
                Toast.makeText(getApplicationContext(), "Erreur lors du chargement des contacts", Toast.LENGTH_LONG).show();
                Log.e("TAAAag", "Erreur ");
            }
        });
    }
}
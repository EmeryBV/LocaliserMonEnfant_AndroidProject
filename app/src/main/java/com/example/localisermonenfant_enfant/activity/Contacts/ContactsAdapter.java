package com.example.localisermonenfant_enfant.activity.Contacts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.localisermonenfant_enfant.R;
import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ViewHolderContacts> {

    ArrayList<Contacts> contacts;

    public ContactsAdapter(ArrayList<Contacts> contacts) {
        this.contacts =contacts;
    }

    @NonNull
    @Override
    public ViewHolderContacts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
       View view = layoutInflater.inflate((R.layout.item_contact),parent,false);
       return new ViewHolderContacts(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderContacts holder, int position) {
    holder.display(contacts.get(position));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }


}

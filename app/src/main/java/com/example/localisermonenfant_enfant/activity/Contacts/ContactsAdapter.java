package com.example.localisermonenfant_enfant.activity.Contacts;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.localisermonenfant_enfant.R;
import com.example.localisermonenfant_enfant.activity.Contacts.CallLog.CallLogActivity;
import com.example.localisermonenfant_enfant.activity.Contacts.SMS.SmsActivity;

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

class ViewHolderContacts extends RecyclerView.ViewHolder{

    private TextView tvName;
    private TextView tvNumber;
    private ImageView imagePhoneView;
    private ImageView imageMessageView;

    public ViewHolderContacts(@NonNull final View itemView) {
        super(itemView);

        tvName =  itemView.findViewById(R.id.tv_name);
        tvNumber=  itemView.findViewById(R.id.tv_number);
        imagePhoneView = itemView.findViewById(R.id.imagePhoneView);

    }

    void display(final Contacts contact){
        tvName.setText(contact.getName());
        tvNumber.setText(contact.getNumber());

        imagePhoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemView.getContext(), CallLogActivity.class);
                intent.putExtra("contactID", contact.getId());
                intent.putExtra("contactPhoneNumber", contact.getNumber());
                intent.putExtra("contactName",contact.getName());

                itemView.getContext().startActivity(intent);
            }
        });

        imageMessageView = itemView.findViewById(R.id.imageMessageView);
        imageMessageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemView.getContext(), SmsActivity.class);
                intent.putExtra("contactID", String.valueOf(contact.getId()));
                intent.putExtra("contactPhoneNumber", contact.getNumber());
                intent.putExtra("contactName",contact.getName());
//                        Toast.makeText(getApplicationContext(), "mon message" + contactsArrayList.get(position).getNumber(), Toast.LENGTH_SHORT).show();

                itemView.getContext().startActivity(intent);
            }
        });

    }

}

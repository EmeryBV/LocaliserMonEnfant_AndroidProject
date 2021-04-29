package com.example.localisermonenfant_enfant.activity.Contacts;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.localisermonenfant_enfant.R;
import com.example.localisermonenfant_enfant.activity.SMS.SmsActivity;

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
        imageMessageView = itemView.findViewById(R.id.imageMessageView);
        imageMessageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemView.getContext(), SmsActivity.class);
                intent.putExtra("phoneNumber", tvNumber.getText().toString().replaceAll("\\s+",""));
                intent.putExtra("name",tvName.getText().toString());
//                        Toast.makeText(getApplicationContext(), "mon message" + contactsArrayList.get(position).getNumber(), Toast.LENGTH_SHORT).show();
//                        Log.i(TAG, "tel " + contactsArrayList.get(position).getNumber());

                itemView.getContext().startActivity(intent);
            }
        });
    }

    void display(Contacts contacts){
        tvName.setText(contacts.getName());
        tvNumber.setText(contacts.getNumber());
    }

}

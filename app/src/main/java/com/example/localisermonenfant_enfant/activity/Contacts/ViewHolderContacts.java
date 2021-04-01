package com.example.localisermonenfant_enfant.activity.Contacts;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localisermonenfant_enfant.R;

public class ViewHolderContacts extends RecyclerView.ViewHolder {

    private TextView tvName;
    private TextView tvNumber;

    public ViewHolderContacts(@NonNull View itemView) {
        super(itemView);

        tvName =  itemView.findViewById(R.id.tv_name);
        tvNumber=  itemView.findViewById(R.id.tv_number);

    }

    void display(Contacts contacts){
        tvName.setText(contacts.getName());
        tvNumber.setText(contacts.getNumber());
    }
}

package com.example.localisermonenfant_enfant.activity.ChoiceChild;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localisermonenfant_enfant.R;
import com.example.localisermonenfant_enfant.ServerAPI.Connection;
import com.example.localisermonenfant_enfant.activity.Contacts.CallLog.CallLogActivity;
import com.example.localisermonenfant_enfant.activity.Contacts.Contacts;
import com.example.localisermonenfant_enfant.activity.Contacts.SMS.SmsActivity;
import com.example.localisermonenfant_enfant.activity.MainMenu.MainMenu;

import java.util.ArrayList;

public class ChildAdapter extends RecyclerView.Adapter<ViewHolderChild> {

    ArrayList<Connection.Child> children;

    public ChildAdapter(ArrayList<Connection.Child> children) {
        this.children =children;
    }

    @NonNull
    @Override
    public ViewHolderChild onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
       View view = layoutInflater.inflate((R.layout.item_child),parent,false);
       return new ViewHolderChild(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderChild holder, int position) {
    holder.display(children.get(position));
    }

    @Override
    public int getItemCount() {
        return children.size();
    }
}

class ViewHolderChild extends RecyclerView.ViewHolder{

    private TextView tvName;
    private TextView tvId;
    private LinearLayout LinearLayout;


    public ViewHolderChild(@NonNull final View itemView) {
        super(itemView);

        tvName =  itemView.findViewById(R.id.tv_name);


    }

    void display(final Connection.Child child){
        tvName.setText(child.getName());
        LinearLayout = itemView.findViewById(R.id.linearLayout);
        LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemView.getContext(), MainMenu.class);
                intent.putExtra("childId", String.valueOf(child.getId()));
                intent.putExtra("childName", child.getName());
                itemView.getContext().startActivity(intent);
            }
        });
    }

}

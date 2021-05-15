package com.example.localisermonenfant_enfant.activity.ChoiceChild;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class ChildAdapter extends RecyclerView.Adapter<ViewHolderContacts> {

    ArrayList<Connection.Child> children;

    public ChildAdapter(ArrayList<Connection.Child> children) {
        this.children =children;
    }

    @NonNull
    @Override
    public ViewHolderContacts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
       View view = layoutInflater.inflate((R.layout.item_child),parent,false);
       return new ViewHolderContacts(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderContacts holder, int position) {
    holder.display(children.get(position));
    }

    @Override
    public int getItemCount() {
        return children.size();
    }
}

class ViewHolderContacts extends RecyclerView.ViewHolder{

    private TextView tvName;
    private TextView tvId;
    private RecyclerView recyclerView;


    public ViewHolderContacts(@NonNull final View itemView) {
        super(itemView);

        tvName =  itemView.findViewById(R.id.tv_name);
//        recyclerView = itemView.findViewById(R.id.recycler_view);
//        recyclerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(itemView.getContext(), MainMenu.class);
//                intent.putExtra("child_id", tvId.toString());
//                itemView.getContext().startActivity(intent);
//            }
//        });

    }

    void display(Connection.Child child){
        tvName.setText(child.getName());
    }

}

package com.example.localisermonenfant_enfant.activity.Contacts.CallLog;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localisermonenfant_enfant.R;

import java.util.ArrayList;
import java.util.Collections;

public class CallLogAdapter extends RecyclerView.Adapter<ViewHolderCallLogs> {

    ArrayList<CallLogModel> callLogs;

    public CallLogAdapter(ArrayList<CallLogModel> callLogModels) {
        Collections.reverse(callLogModels);
        this.callLogs =callLogModels;
    }

    @NonNull
    @Override
    public ViewHolderCallLogs onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
       View view = layoutInflater.inflate((R.layout.item_call_log),parent,false);
       return new ViewHolderCallLogs(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCallLogs holder, int position) {
    holder.display(callLogs.get(position));
    }

    @Override
    public int getItemCount() {
        return callLogs.size();
    }
}

class ViewHolderCallLogs extends RecyclerView.ViewHolder{

    private TextView date;
    private TextView phoneNumber;
    private TextView type;
    private TextView duration;

    public ViewHolderCallLogs(@NonNull final View itemView) {
        super(itemView);

        date =  itemView.findViewById(R.id.date);
        phoneNumber =  itemView.findViewById(R.id.phoneNumber);
        type = itemView.findViewById(R.id.type);
        duration = itemView.findViewById(R.id.duration);
    }

    void display(CallLogModel callLogModel){
        date.setText(callLogModel.getCallDayTime());
        phoneNumber.setText(callLogModel.getPhNumber());
        type.setText(callLogModel.dir);
        duration.setText(callLogModel.callDuration);

    }

}

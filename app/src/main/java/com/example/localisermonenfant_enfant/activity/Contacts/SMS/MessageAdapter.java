package com.example.localisermonenfant_enfant.activity.Contacts.SMS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localisermonenfant_enfant.R;

import java.util.ArrayList;
import java.util.Collections;

public class MessageAdapter extends RecyclerView.Adapter{
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    ArrayList<Sms> messageList;

    public MessageAdapter(ArrayList<Sms> message) {
        Collections.reverse(message);
        this.messageList =message;
    }

    @Override
    public int getItemViewType(int position) {
        Sms message = (Sms) messageList.get(position);

        if (message.getType().equals("sent")) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_send, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_receive, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Sms message =  messageList.get(position);

        switch (message.getType()) {
            case "sent":
                ((SentMessageHolder) holder).bind(message);
            break;

            case "receive":
                ((ReceivedMessageHolder) holder).bind(message);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


}

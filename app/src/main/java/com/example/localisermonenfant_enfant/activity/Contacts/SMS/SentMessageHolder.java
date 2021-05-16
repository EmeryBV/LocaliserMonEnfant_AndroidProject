package com.example.localisermonenfant_enfant.activity.Contacts.SMS;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.localisermonenfant_enfant.R;

public class SentMessageHolder extends RecyclerView.ViewHolder{
    TextView messageText, timeText;

    SentMessageHolder(View itemView) {
        super(itemView);

        messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_me);
        timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_me);
    }

    void bind(Sms message) {
        messageText.setText(message.getMessage());

        // Format the stored timestamp into a readable String using method.
        timeText.setText(message.getCreatedAt().toString());
    }

    void display(Sms messages){
        messageText.setText(messages.getMessage());
        timeText.setText(messages.getSender());
    }
}
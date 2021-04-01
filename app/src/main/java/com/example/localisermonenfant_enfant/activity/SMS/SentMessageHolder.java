package com.example.localisermonenfant_enfant.activity.SMS;

import android.app.Application;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.localisermonenfant_enfant.R;
import com.sendbird.android.shadow.okhttp3.internal.Util;

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
        timeText.setText(DateUtils.formatDateTime(itemView.getContext(),Long.parseLong(message.getCreatedAt()),DateUtils.FORMAT_ABBREV_ALL));
    }

    void display(Sms messages){
        messageText.setText(messages.getMessage());
        timeText.setText(messages.getSender());
    }
}
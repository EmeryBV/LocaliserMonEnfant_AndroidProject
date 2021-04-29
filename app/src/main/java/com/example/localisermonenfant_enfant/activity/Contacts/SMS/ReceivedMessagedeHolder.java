package com.example.localisermonenfant_enfant.activity.Contacts.SMS;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.localisermonenfant_enfant.R;

class ReceivedMessageHolder extends RecyclerView.ViewHolder {
    TextView messageText, timeText, nameText;
    ImageView profileImage;
    android.text.format.DateFormat df = new android.text.format.DateFormat();

    ReceivedMessageHolder(View itemView) {
        super(itemView);
        messageText = (TextView) itemView.findViewById(R.id.text_gchat_user_other);
        timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_other);
        nameText = (TextView) itemView.findViewById(R.id.text_gchat_message_other);
    }

    void bind(Sms message) {
        messageText.setText(message.getSender());

        // Format the stored timestamp into a readable String using method.
        timeText.setText(DateUtils.formatDateTime(itemView.getContext(),Long.parseLong(message.getCreatedAt()),DateUtils.FORMAT_ABBREV_ALL));
        nameText.setText(message.getMessage());

        // Insert the profile image from the URL into the ImageView.
//        Utils.displayRoundImageFromUrl(this, message.getSender().getProfileUrl(), profileImage);
    }

}
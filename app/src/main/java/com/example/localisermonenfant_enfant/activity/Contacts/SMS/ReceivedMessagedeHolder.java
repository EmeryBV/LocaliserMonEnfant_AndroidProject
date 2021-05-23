package com.example.localisermonenfant_enfant.activity.Contacts.SMS;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localisermonenfant_enfant.R;

import java.util.Date;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    void bind(Sms message) {
        messageText.setText(message.getSender());

        // Format the stored timestamp into a readable String using method.

        timeText.setText(message.getCreatedAt());
        nameText.setText(message.getMessage());

        // Insert the profile image from the URL into the ImageView.
//        Utils.displayRoundImageFromUrl(this, message.getSender().getProfileUrl(), profileImage);
    }

}
package com.example.ft_hangouts;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;




public class MessageReceiver extends BroadcastReceiver {
    private static final String TAG = MessageReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";

    public MessageReceiver(){

    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String format = bundle.getString("format");
        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            // Check the Android version.
            boolean isVersionM =
                    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // Check Android version and use appropriate createFromPdu.
                if (isVersionM) {
                    // If Android version M or newer:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    // If Android version L or older:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                String senderNum = msgs[i].getOriginatingAddress();
                String senderMessage =msgs[i].getMessageBody();

                Intent broadCastIntent = new Intent(context, MessageActivity.class);

                broadCastIntent.setAction("SMS_RECEIVED_ACTION");
                broadCastIntent.putExtra(MessageActivity.EXTRA_SMS_SENDER, senderNum);
                broadCastIntent.putExtra(MessageActivity.EXTRA_SMS_MESSAGE, senderMessage);

                try {
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, broadCastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    pendingIntent.send();
                    context.startActivity(broadCastIntent);
                }catch (Exception e){
                    Log.d(TAG, "Exception Receiver :" + e);
                }
            }
        }
    }
}

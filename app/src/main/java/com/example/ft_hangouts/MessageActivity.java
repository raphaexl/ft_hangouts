package com.example.ft_hangouts;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MessageActivity extends AppCompatActivity {
    private static final int SMS_PERMISSIONS_REQUEST_SEND_CODE = 100;
    private static final int SMS_PERMISSIONS_REQUEST_RECEIVE_CODE = 101;
    public static final String EXTRA_SMS_SENDER = "extra_sms_sender";
    public static final String EXTRA_SMS_MESSAGE = "extra_sms_message";
    private EditText send_msg_input;
    private String sender_name, sender_surname;
    private TextView sender_info_txt;
    private TextView sender_msg_txt;
    private TextView my_sent_msg;
    private TextView me_txt;
    private String tel;
    private Button send_msg_button;
    private IntentFilter intentFilter;
    private boolean phone_permission_send_granted;
    private boolean phone_permission_receive_granted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_message);
        updateHeaderColor();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(R.string.app_name);
        }

        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        phone_permission_send_granted = false;
        phone_permission_receive_granted = false;

        send_msg_button = (Button)findViewById(R.id.send_msg_button);

        send_msg_input = (EditText)findViewById(R.id.chat_msg_input);

        sender_info_txt = (TextView)findViewById(R.id.sender_info_txt);
        sender_msg_txt = (TextView)findViewById(R.id.sender_msg_txt);
        my_sent_msg = (TextView)findViewById(R.id.my_sent_msg);
        me_txt = (TextView)findViewById(R.id.me_txt);

        send_msg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone_permission_send_granted){
                    sendTheActualMSg();
                }else{
                    Toast.makeText(MessageActivity.this, "Send sms permissions not granted yet :-( ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getAndSetIntentData();
        ActionBar ab = getSupportActionBar();
        if (ab != null){
            if (sender_name == null && sender_surname == null){
                ab.setTitle("Unknown");
            }else{
                ab.setTitle(sender_name + " " + sender_surname);
            }
        }
        checkForPhonePermission();
    }

    void getAndSetIntentData(){
        if (getIntent().hasExtra("name") && getIntent().hasExtra("surname")
                && getIntent().hasExtra("tel")){
            String name = getIntent().getStringExtra("name");
            String surname = getIntent().getStringExtra("surname");

            sender_name = name;
            sender_surname = surname;

            tel = getIntent().getStringExtra("tel");
        }else{
            Toast.makeText(this, "Empty field can't update", Toast.LENGTH_SHORT).show();
        }
    }

    protected void checkForPhonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            phone_permission_send_granted = false;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSIONS_REQUEST_SEND_CODE);
        }else{
            phone_permission_send_granted = true;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            phone_permission_receive_granted = false;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, SMS_PERMISSIONS_REQUEST_RECEIVE_CODE);
        }else{
            phone_permission_receive_granted = true;
        }
    }

    void sendTheActualMSg(){
            String SENT = "Message Sent";
            String DELIVERED = "Message Delivered";

            String smsMsg = send_msg_input.getText().toString();
            PendingIntent sentPI = PendingIntent.getBroadcast(MessageActivity.this, 0, new Intent(SENT), 0);
            PendingIntent deliveredPI = PendingIntent.getBroadcast(MessageActivity.this, 0, new Intent(DELIVERED), 0);

            SmsManager sms = SmsManager.getDefault();
            if (PhoneNumberUtils.isGlobalPhoneNumber(tel)){
                sms.sendTextMessage(tel, null, smsMsg, sentPI, deliveredPI);
                my_sent_msg.setText(smsMsg);
                send_msg_input.setText("");
                me_txt.setVisibility(View.VISIBLE);
                Toast.makeText(MessageActivity.this, "Your message sent successfully !", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(MessageActivity.this, "Invalid phone number :" + tel, Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SMS_PERMISSIONS_REQUEST_SEND_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    phone_permission_send_granted = true;
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }break;
            case SMS_PERMISSIONS_REQUEST_RECEIVE_CODE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    phone_permission_receive_granted = true;
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            break;
            default:break;
        }

    }

    //Cause on resume will be called no matter what
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String smsSender = getIntent().getExtras().getString(EXTRA_SMS_SENDER);
        String smsContent = getIntent().getExtras().getString(EXTRA_SMS_MESSAGE);
        if (smsSender == null || smsContent == null){
        }
        else if (!smsSender.isEmpty() && !smsContent.isEmpty())
        {
            sender_info_txt.setText(smsSender);
            sender_msg_txt.setText(smsContent);
            Toast.makeText(this, smsSender + smsContent, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Seems like shit happens (sender: " + smsSender +") (content: " + smsContent + ")", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    void updateHeaderColor(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            String colorStr = getResources().getString(AppThemeAndHeader.getCurrentHeaderColor());
            ColorDrawable colorDrawable
                    = new ColorDrawable(Color.parseColor(colorStr));//);
            actionBar.setBackgroundDrawable(colorDrawable);
        }
    }

    private void changeLanguage(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(configuration, getApplicationContext().getResources().getDisplayMetrics());
    }

    public void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        changeLanguage(language);
    }
}
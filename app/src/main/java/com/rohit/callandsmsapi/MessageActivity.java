package com.rohit.callandsmsapi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MessageActivity extends AppCompatActivity {


    final int SEND_SMS_PERMISSION_REQUEST_CODE = 111;
    private  Button mSendMessageButton;
    private EditText mMessageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);



        if(!checkPermission(Manifest.permission.RECEIVE_SMS)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 222);
        }

        mSendMessageButton = (Button) findViewById(R.id.button_send_message);
        mMessageEditText = (EditText) findViewById(R.id.edittext_message);

        mSendMessageButton.setEnabled(false);
            if(checkPermission(Manifest.permission.SEND_SMS)) {
                mSendMessageButton.setEnabled(true);
            }else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS},
                        SEND_SMS_PERMISSION_REQUEST_CODE);
            }
        mSendMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mMessage = mMessageEditText.getText().toString(); // Message getting from user
                    String mUserNumbers = "9741373467,9449258651"; // More than one number
                    String mPhoneNumber[] = mUserNumbers.split(", *");
                    if(!TextUtils.isEmpty(mMessage)) {

                        // Checking runtime permission
                        if(checkPermission(Manifest.permission.SEND_SMS)) {
                            SmsManager smsManager = SmsManager.getDefault();
                            // For MultipleNumbers
                            for(String numbers : mPhoneNumber) {
                                smsManager.sendTextMessage(numbers, null, mMessage, null, null);
                            }
                            Toast.makeText(MessageActivity.this, "Message Sent", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(MessageActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });



    }

        private boolean checkPermission(String permission) {
            int checkPermission = ContextCompat.checkSelfPermission(this, permission);
            return (checkPermission == PackageManager.PERMISSION_GRANTED);
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case SEND_SMS_PERMISSION_REQUEST_CODE: {
                if(grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    mSendMessageButton.setEnabled(true);
                }
                return;
            }
        }
    }

}

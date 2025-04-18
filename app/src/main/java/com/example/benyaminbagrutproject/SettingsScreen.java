package com.example.benyaminbagrutproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class SettingsScreen extends AppCompatActivity implements View.OnClickListener {

    protected CheckBox cbNotifications;
    protected EditText etNotificationTime,etName;

    protected Button btnConfirm,btnCancel;

    protected FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        firebaseHelper = FirebaseHelper.getInstance(this);

        cbNotifications = findViewById(R.id.cbNotifications);
        etNotificationTime  =findViewById(R.id.etNotificationTime);
        etName = findViewById(R.id.etName);

        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);

        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        etName.setText(firebaseHelper.getUser().getName());
        cbNotifications.setChecked(firebaseHelper.getUser().beforeMeetNotification);
        etNotificationTime.setText(firebaseHelper.getUser().TimeBeforeMeetNotif + "");

    }

    @Override
    public void onClick(View view) {
        if (view == btnConfirm)
        {
            Intent i = new Intent();

            i.putExtra("name",etName.getText().toString());
            i.putExtra("notifications" ,cbNotifications.isChecked());
            i.putExtra("time before", Integer.parseInt(etNotificationTime.getText().toString()));

            setResult(RESULT_OK,i);
            finish();
        }
        else if(view == btnCancel)
        {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
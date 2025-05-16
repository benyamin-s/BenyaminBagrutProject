package com.example.benyaminbagrutproject.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;

import com.example.benyaminbagrutproject.FirebaseHelper;
import com.example.benyaminbagrutproject.R;

public class SettingsScreen extends AppCompatActivity implements View.OnClickListener {

    protected SwitchCompat scNotifications;
    protected EditText etNotificationTime,etName;

    protected Button btnConfirm,btnCancel;

    protected FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        firebaseHelper = FirebaseHelper.getInstance(this);

        scNotifications = findViewById(R.id.scNotifications);
        etNotificationTime  =findViewById(R.id.etNotificationTime);
        etName = findViewById(R.id.etName);

        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);

        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        etName.setText(firebaseHelper.getUser().getName());
        if (firebaseHelper.getUser().getBeforeMeetNotification() != null)
            scNotifications.setChecked(firebaseHelper.getUser().getBeforeMeetNotification());
        etNotificationTime.setText(firebaseHelper.getUser().getTimeBeforeMeetNotif() + "");

    }

    @Override
    public void onClick(View view) {
        if (view == btnConfirm)
        {
            Intent i = new Intent();

            i.putExtra("name",etName.getText().toString());
            i.putExtra("notifications" ,scNotifications.isChecked());
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
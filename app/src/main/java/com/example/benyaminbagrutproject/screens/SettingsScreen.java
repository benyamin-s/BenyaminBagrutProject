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

/**
 * Activity for managing user settings in the  application.
 * This screen allows guides to configure:
 * - Display name
 * - Meeting notification preferences
 * - Time before meeting for notifications
 * 
 * Changes are saved and returned to the calling activity through an Intent.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class SettingsScreen extends AppCompatActivity implements View.OnClickListener {

    /** Switch to enable/disable meeting notifications */
    protected Switch swNotifications;
    
    /** EditText for setting notification time before meetings */
    protected EditText etNotificationTime;
    
    /** EditText for changing display name */
    protected EditText etName;

    /** Button to confirm and save changes */
    protected Button btnConfirm;
    
    /** Button to cancel changes */
    protected Button btnCancel;

    /** Helper class for Firebase operations */
    protected FirebaseHelper firebaseHelper;

    /**
     * Initializes the activity, sets up UI components and loads current settings.
     * Populates the input fields with the user's current settings from Firebase.
     * 
     * @param savedInstanceState If non-null, this activity is being re-initialized after previously being shut down
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        firebaseHelper = FirebaseHelper.getInstance(this);

        swNotifications = findViewById(R.id.swNotifications);
        etNotificationTime  =findViewById(R.id.etNotificationTime);
        etName = findViewById(R.id.etName);

        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);

        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        etName.setText(firebaseHelper.getUser().getName());
        if (firebaseHelper.getUser().getBeforeMeetNotification() != null)
            swNotifications.setChecked(firebaseHelper.getUser().getBeforeMeetNotification());
        etNotificationTime.setText(firebaseHelper.getUser().getTimeBeforeMeetNotif() + "");

    }

    /**
     * Handles click events for buttons.
     * - Confirm button saves changes and returns results to calling activity
     * - Cancel button discards changes and returns to calling activity
     * 
     * The following data is returned in the result intent:
     * - "name": String - user's display name
     * - "notifications": boolean - notification preference
     * - "time before": int - minutes before meeting for notification
     * 
     * @param view The view that was clicked
     */
    @Override
    public void onClick(View view) {
        if (view == btnConfirm)
        {
            Intent i = new Intent();

            i.putExtra("name",etName.getText().toString());
            i.putExtra("notifications" ,swNotifications.isChecked());
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
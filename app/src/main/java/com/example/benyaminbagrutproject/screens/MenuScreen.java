package com.example.benyaminbagrutproject.screens;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.benyaminbagrutproject.AlarmReciever;
import com.example.benyaminbagrutproject.FirebaseHelper;
import com.example.benyaminbagrutproject.Meet;
import com.example.benyaminbagrutproject.R;
import com.example.benyaminbagrutproject.User;

/**
 * Main menu activity for the youth movement guide application.
 * This screen serves as the central hub for accessing various features like meetings,
 * activity search, requests, and settings. It also displays the user's profile information
 * and handles meeting notifications.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class MenuScreen extends AppCompatActivity implements View.OnClickListener {

    /** TextView displaying user's display name */
    protected TextView tvName;
    
    /** TextView displaying user's email */
    protected TextView tvEmail;
    
    /** Button to access user's meetings */
    protected Button btnMyMeets;
    
    /** Button to search for activities */
    protected Button btnSearchActivities;
    
    /** Button to access requests */
    protected Button btnRequests;
    
    /** Button to access settings */
    protected Button btnSettings;
    
    /** Button to sign out */
    protected Button btnDisconnect;

    /** Helper class for Firebase operations */
    protected FirebaseHelper firebaseHelper;

    /**
     * Activity result launcher for settings screen.
     * Handles the result of settings changes, including user profile updates
     * and notification preferences. Updates meeting alarms based on new settings.
     */
    ActivityResultLauncher<Intent> settingsLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if(o.getResultCode() == RESULT_OK)
                    {
                        firebaseHelper.getUser().setName(o.getData().getStringExtra("name"));
                        firebaseHelper.getUser().setBeforeMeetNotification(o.getData().getBooleanExtra("notifications",false));
                        firebaseHelper.getUser().setTimeBeforeMeetNotif(o.getData().getIntExtra("time before",0));
                        Calendar calendar = Calendar.getInstance();

                        Handler handler = new Handler(new Handler.Callback() {
                            @Override
                            public boolean handleMessage(@NonNull Message msg) {
                                if (msg.arg1 == User.USER_UPDATED)
                                {
                                    tvName.setText(o.getData().getStringExtra("name"));


                                    for (int i = 0; i < firebaseHelper.getUser().getMeetsList().size();i++) {
                                        Meet m = firebaseHelper.getUser().getMeetsList().get(i);

                                        AlarmReciever.cancelAlarm(MenuScreen.this , i);
                                        if (firebaseHelper.getUser().getBeforeMeetNotification() && m.getDate() > calendar.getTimeInMillis() + firebaseHelper.getUser().getTimeBeforeMeetNotif() * 60000)
                                        {
                                            AlarmReciever.ScheduleMeetAlarm(MenuScreen.this,i , m.getDate()-firebaseHelper.getUser().getTimeBeforeMeetNotif() * 60000);
                                        }
                                    }

                                }
                                return true;
                            }
                        });

                        firebaseHelper.UpdateSettings(handler);

                    }
                }
            }
    );

    /**
     * Initializes the activity, sets up UI components and Firebase helper.
     * 
     * @param savedInstanceState If non-null, this activity is being re-initialized after previously being shut down
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_screen);
        Log.d("log debugger", "onCreate: MenuScreen");

        tvEmail = findViewById(R.id.tvEmail);
        tvName = findViewById(R.id.tvName);

        btnDisconnect = findViewById(R.id.btnDisconnect);
        btnRequests  = findViewById(R.id.btnRequests);
        btnSettings = findViewById(R.id.btnSettings);
        btnMyMeets = findViewById(R.id.btnMyMeets);
        btnSearchActivities = findViewById(R.id.btnSearchActivities);


        btnDisconnect.setOnClickListener(this);
        btnSearchActivities.setOnClickListener(this);
        btnRequests.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnMyMeets.setOnClickListener(this);


        Handler handler = new Handler(
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message message) {
                        if (message.arg1 == FirebaseHelper.DONE_RETRIEVE_USER_DATA)
                        {
                            tvName.setText(firebaseHelper.getUser().getName());
                            tvEmail.setText(firebaseHelper.getUser().getEmail());
                        }
                        return true;
                    }
                }
        );


        firebaseHelper = FirebaseHelper.getInstance(this);

        firebaseHelper.retrieveUserData(handler);


        if  (!checkPermission(Manifest.permission.POST_NOTIFICATIONS) )
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.POST_NOTIFICATIONS},0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !this.getSystemService(AlarmManager.class).canScheduleExactAlarms())
        {
            Dialog dialog=new Dialog(this);
            dialog.setContentView(R.layout.permission_dialog);
            dialog.setTitle(" dialog screen");



            dialog.setCancelable(false);

            Button btnAccept = dialog.findViewById(R.id.btnAccept);
            Button btnCancel = dialog.findViewById(R.id.btnCancel);

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    startActivity(intent);
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    finish();
                }
            });

            dialog.show();
        }
    }

    /**
     * Checks if the app has been granted a specific permission.
     * 
     * @param permit The permission to check
     * @return true if permission is granted, false otherwise
     */
    private boolean checkPermission(String permit){
        if (ContextCompat.checkSelfPermission(this,
                permit) != PackageManager.PERMISSION_GRANTED ){
            return false;
        } else {
            return true;
        }
    }


    /**
     * Handles the result of permission requests.
     * If notifications permission is not granted, shows a dialog to guide the user
     * to app settings.
     * 
     * @param requestCode The request code passed to requestPermissions
     * @param permissions The requested permissions
     * @param grantResults The grant results for the corresponding permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
    // if permission denied , the grantResults is empty
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break; }}
            if (allPermissionsGranted)
                Toast.makeText(this, "permissions granted", Toast.LENGTH_SHORT).show();
            else
                createDialog();
        }
    }

    /**
     * Creates and shows a dialog to guide users to app settings for granting permissions.
     * The dialog is non-cancelable and provides options to either go to settings
     * or exit the app.
     */
    public void createDialog(){
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.permission_dialog);
        dialog.setTitle(" dialog screen");

        dialog.setCancelable(false);

        Button btnAccept = dialog.findViewById(R.id.btnAccept);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", MenuScreen.this.getPackageName(), null);
                intent.setData(uri);
                MenuScreen.this.startActivity(intent);
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
    }
    //

    /**
     * Handles click events for all menu buttons.
     * - Disconnect button signs out the user and returns to login screen
     * - My Meets button navigates to meetings screen
     * - Requests button navigates to requests screen
     * - Settings button launches settings screen with result handler
     * - Search Activities button navigates to activity search screen
     * 
     * @param view The view that was clicked
     */
    @Override
    public void onClick(View view) {
        Intent i;
        if(view == btnDisconnect){
            firebaseHelper.SignOut();
            i = new Intent(this, LoginScreen.class);
            startActivity(i);

        }


        else if(view == btnMyMeets){

            i = new Intent(this, MyMeetsScreen.class);
            startActivity(i);

        }

        else if(view == btnRequests){

            i = new Intent(this, RequestsScreen.class);
            startActivity(i);

        }

        else if(view == btnSettings){
            i = new Intent(this, SettingsScreen.class);
            settingsLauncher.launch(i);
        }

        else
        {
            //btnSearchActivities

            i = new Intent(this, SearchActivitiesScreen.class);
            startActivity(i);

        }

    }



}
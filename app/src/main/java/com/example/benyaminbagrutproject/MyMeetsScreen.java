package com.example.benyaminbagrutproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.snapshot.Index;

import java.util.ArrayList;

public class MyMeetsScreen extends AppCompatActivity {
    protected FirebaseHelper firebaseHelper;

    protected MeetsAdapter meetsAdapter;

    protected Button btnNewMeet;

    protected ListView lvMeets;

    protected ArrayList<Meet> meetslist;

    protected int index;


    protected ActivityResultLauncher<Intent> arlEditMeet = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == RESULT_OK)
                    {
                        meetslist.set(index,/*TODO the edited meet*/);
                        firebaseHelper.SaveMeet(index,/*TODO the edited meet */);
                    }

                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meets_screen);

        btnNewMeet = findViewById(R.id.btnNewMeet);
        lvMeets = findViewById(R.id.lvMeets);

        firebaseHelper = FirebaseHelper.getInstance(this);
        meetslist = firebaseHelper.retrieveUserData().getMeetsList();

        meetsAdapter = new MeetsAdapter(this,0,meetslist);
        lvMeets.setAdapter(meetsAdapter);

        Intent intent = new Intent(this, EditMeetScreen.class);
        lvMeets.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        index = i;
                        arlEditMeet.launch(intent);
                    }
                }
        );
    }
}
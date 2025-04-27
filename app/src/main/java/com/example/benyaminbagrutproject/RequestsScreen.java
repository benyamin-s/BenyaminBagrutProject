package com.example.benyaminbagrutproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

public class RequestsScreen extends AppCompatActivity implements View.OnClickListener {
    protected Button btnBack , btnMyRequests , btnAllRequests , btnNewRequest;

    protected ListView lvRequests;
    protected RequestsAdapter requestsAdapter;

    protected FirebaseHelper firebaseHelper;

    protected Handler allRequests,myRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_screen);

        lvRequests = findViewById(R.id.lvRequestList);

        btnBack = findViewById(R.id.btnBack);
        btnAllRequests = findViewById(R.id.btnAllRequest);
        btnMyRequests = findViewById(R.id.btnMyRequests);
        btnNewRequest = findViewById(R.id.btnNewRequest);

        btnBack.setOnClickListener(this);
        btnAllRequests.setOnClickListener(this);
        btnMyRequests.setOnClickListener(this);
        btnNewRequest.setOnClickListener(this);

        firebaseHelper = FirebaseHelper.getInstance(this);


         allRequests = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (firebaseHelper.requestsList != null){
                    requestsAdapter = new RequestsAdapter(RequestsScreen.this,0,firebaseHelper.getRequestsList());
                    lvRequests.setAdapter(requestsAdapter);
                }
                return true;
            }
        });

        myRequests = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                ArrayList<Request> arrayList = new ArrayList<>();
                for (Request r:firebaseHelper.getRequestsList()) {
                    if (r.getRequesterID().equals(firebaseHelper.getUserId()))
                    {
                        arrayList.add(r);
                    }
                }
                requestsAdapter = new RequestsAdapter(RequestsScreen.this,0,arrayList);
                lvRequests.setAdapter(requestsAdapter);
                return true;
            }
        });

        firebaseHelper.retrieveRequests(allRequests);
    }

    @Override
    public void onClick(View view) {

        if (view == btnBack)
        {
            startActivity(new Intent(this,MenuScreen.class));
            firebaseHelper.dbRequestsRef.removeEventListener(firebaseHelper.requestsValueEventListener);
            finish();
        } else if (view == btnAllRequests) {
            firebaseHelper.retrieveRequests(allRequests);

        } else if (view == btnMyRequests) {
            firebaseHelper.retrieveRequests(myRequests);

        } else if (view == btnNewRequest) {
            createRequestDialog();
        }
    }

    public void createRequestDialog(){
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.create_request_dialog);
        dialog.setTitle(" dialog screen");

        dialog.setCancelable(false);

        Button btnRequest = dialog.findViewById(R.id.btnRequest);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        EditText etRequestTitle = dialog.findViewById(R.id.etRequestTitle);
        EditText etRequestContent = dialog.findViewById(R.id.etRequestContent);
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {


                dialog.dismiss();
                return true;
            }
        });

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request request = new Request();
                request.setRequestTitle(etRequestTitle.getText().toString());
                request.setRequesterName(firebaseHelper.getUser().getName());
                request.setRequesterID(firebaseHelper.getUserId());
                request.setRequestContent(etRequestContent.getText().toString());

                Calendar calendar = Calendar.getInstance();
                request.setDate(calendar.getTimeInMillis());

                firebaseHelper.AddRequest(request,handler);

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
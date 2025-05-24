package com.example.benyaminbagrutproject.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.benyaminbagrutproject.FirebaseHelper;
import com.example.benyaminbagrutproject.R;
import com.example.benyaminbagrutproject.Request;
import com.example.benyaminbagrutproject.listviewadapters.RequestsAdapter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Activity for managing activity and meeting content requests in the  application.
 * This screen allows users to:
 * - View all requests from other users
 * - View their own requests
 * - Create new requests for activities or meeting content
 * - Provide answers to other users' requests
 * 
 * The screen provides filtering options to switch between all requests and personal requests.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class RequestsScreen extends AppCompatActivity implements View.OnClickListener {
    /** Button to return to previous screen */
    protected Button btnBack;
    
    /** Button to show user's own requests */
    protected Button btnMyRequests;
    
    /** Button to show all requests */
    protected Button btnAllRequests;
    
    /** Button to create a new request */
    protected Button btnNewRequest;

    /** ListView displaying filtered requests */
    protected ListView lvRequests;
    
    /** Adapter for displaying requests in the ListView */
    protected RequestsAdapter requestsAdapter;

    /** Helper class for Firebase operations */
    protected FirebaseHelper firebaseHelper;

    /** Handler for processing all requests data */
    protected Handler allRequests;
    
    /** Handler for processing user's requests data */
    protected Handler myRequests;

    /**
     * Initializes the activity, sets up UI components and loads initial request data.
     * Sets up handlers for processing different types of request data and displays
     * all requests by default.
     * 
     * @param savedInstanceState If non-null, this activity is being re-initialized after previously being shut down
     */
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
                if (firebaseHelper.getRequestsList() != null){
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

    /**
     * Handles click events for all interactive UI elements.
     * - Back button returns to menu screen and removes Firebase listeners
     * - All Requests button shows all available requests
     * - My Requests button filters to show only user's requests
     * - New Request button shows dialog to create a new request
     * 
     * @param view The view that was clicked
     */
    @Override
    public void onClick(View view) {

        if (view == btnBack)
        {
            startActivity(new Intent(this,MenuScreen.class));
            firebaseHelper.getDbRequestsRef().removeEventListener(firebaseHelper.requestsValueEventListener);
            finish();
        } else if (view == btnAllRequests) {
            firebaseHelper.retrieveRequests(allRequests);

        } else if (view == btnMyRequests) {
            firebaseHelper.retrieveRequests(myRequests);

        } else if (view == btnNewRequest) {
            createRequestDialog();
        }
    }

    /**
     * Creates and displays a dialog for creating a new request.
     * The dialog allows the user to enter:
     * - Request title
     * - Request content/description
     * 
     * When submitted, creates a new Request object with the current timestamp
     * and user information, then saves it to Firebase. The dialog is automatically
     * dismissed after successful submission.
     */
    public void createRequestDialog() {
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
package com.example.benyaminbagrutproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseHelper {

    private static FirebaseHelper instance = null;

    private FirebaseAuth auth;
    protected DatabaseReference dbRootRef,dbUserRef;
    private static Context context;

    public static final int DONE_RETRIEVE_USER_DATA = 11;

    protected User user;




    private FirebaseHelper(){
        auth= FirebaseAuth.getInstance();
        dbRootRef = FirebaseDatabase.getInstance().getReference();
        dbUserRef = FirebaseDatabase.getInstance().getReference("Users/"+auth.getCurrentUser().getUid());

        user = null;
    }


  public static FirebaseHelper getInstance(Context ctx ){
        context = ctx;
        if (instance == null){
            instance = new FirebaseHelper();
        }
        return instance;
    }
    //


    public DatabaseReference getDbRootRef() {
        return dbRootRef;
    }
    public DatabaseReference getDbUserRef() {
        return dbUserRef;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    //
    public void retrieveUserData(Handler handler)
    {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("retrieving info");
        progressDialog.setMessage("please wait");
        progressDialog.show();

        String key  =auth.getCurrentUser().getUid();
        Log.d("user key", "retrieveUserData: "+key);

        dbUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                /* TODO remove when certain the code works without
                ArrayList<Meet> arrayList = new ArrayList<>();

                for (DataSnapshot data:dataSnapshot.child("meetsList").getChildren())
                {
                    arrayList.add(data.getValue(Meet.class));
                }
                user.setMeetsList(arrayList);
                */

                Message message = new Message();
                message.arg1  =DONE_RETRIEVE_USER_DATA;
                handler.sendMessage(message);
                progressDialog.dismiss();
                dbUserRef.removeEventListener(this);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(context,databaseError.getMessage(),Toast.LENGTH_LONG).show();

            }

        }
        );

    }


    public void SaveMeet(int position,Meet meet,int meetType,Handler handler)
    {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("retrieving info");
        progressDialog.setMessage("please wait");
        progressDialog.show();

        if (meetType == Meet.NEW_MEET)
        {
            user.meetsList.add(meet);
            dbUserRef.child("meetsList").setValue(user.meetsList, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error == null)
                    {
                        Message message = new Message();
                        message.arg1 = Meet.MEET_SAVED;
                        handler.sendMessage(message);

                    }
                    else Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG);

                    progressDialog.dismiss();
                }
            });
        }
        else if (meetType == Meet.EDIT_MEET)
        {
            user.meetsList.set(position,meet);
            //TODO check if works
            dbUserRef.child("meetsList").child(""+position).setValue(meet, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error == null)
                    {
                        Message message = new Message();
                        message.arg1 = Meet.MEET_SAVED;
                        handler.sendMessage(message);
                    }
                    else Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG);

                    progressDialog.dismiss();
                }
            });
        }
        else
        {
        }


        for (BasicActivity basicActivity:meet.getActivities())
        {
            //TODO save in firebase
        }

    }


    public void SignOut(){
        auth.signOut();
        instance = null;
    }


}

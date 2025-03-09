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
    protected DatabaseReference dbRootRef,dbUserRef,dbActivitiesRef;
    private static Context context;

    public static final int DONE_RETRIEVE_USER_DATA = 11;

    protected User user;




    private FirebaseHelper(){
        auth= FirebaseAuth.getInstance();
        dbRootRef = FirebaseDatabase.getInstance().getReference();
        dbUserRef = FirebaseDatabase.getInstance().getReference("Users/"+auth.getCurrentUser().getUid());
        dbActivitiesRef = FirebaseDatabase.getInstance().getReference("Activities");
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

    public String getUserId()
    {
        return auth.getCurrentUser().getUid();
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

                ArrayList<Meet> arrayList = new ArrayList<>();

                for (DataSnapshot data:dataSnapshot.child("meets_List").getChildren())
                {
                    arrayList.add(data.getValue(Meet.class));
                }
                user.setMeetsList(arrayList);


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

    private void SaveActivities(int i,Meet meet,Handler handler)
    {
        //TODO check in firebase if works
        DatabaseReference dbActivityRef = dbActivitiesRef.push();
        BasicActivity basicActivity = meet.getActivities().get(i);
        basicActivity.setActivityID(dbActivityRef.getKey());
        dbActivityRef.setValue(basicActivity, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (i < meet.getActivities().size()-1)
                    SaveActivities(i+1,meet,handler);
                else{
                    Message message = new Message();
                    message.arg1 = Meet.MEET_SAVED;
                    handler.sendMessage(message);
                }
            }
        });
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
            user.getMeetsList().add(meet);
            DatabaseReference dbMeetRef = dbUserRef.child("meets_List").push();
            meet.setMeetID(dbMeetRef.getKey());

            for (BasicActivity b: meet.getActivities()) {
                b.setMeetID(meet.getMeetID());
            }

            dbMeetRef.setValue(meet, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    SaveActivities(0,meet,handler);
                }
            });



        }
        else if (meetType == Meet.EDIT_MEET)
        {
            user.meetsList.set(position,meet);
            dbUserRef.child("meets_List").child(meet.getMeetID()).setValue(meet, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error == null)
                    {
                        SaveActivities(0,meet,handler);
                    }
                    else Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG);

                    progressDialog.dismiss();
                }
            });
        }
        else
        {
        }




    }


    public void SignOut(){
        auth.signOut();
        instance = null;
    }


}

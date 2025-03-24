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
        if (user == null) {
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("retrieving info");
            progressDialog.setMessage("please wait");
            progressDialog.show();

            String key = auth.getCurrentUser().getUid();
            Log.d("user key", "retrieveUserData: " + key);

            dbUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(User.class);

                    ArrayList<Meet> arrayList = new ArrayList<>();

                    for (DataSnapshot data : dataSnapshot.child("meets_List").getChildren()) {
                        arrayList.add(data.getValue(Meet.class));
                    }
                    user.setMeetsList(arrayList);


                    Message message = new Message();
                    message.arg1 = DONE_RETRIEVE_USER_DATA;
                    handler.sendMessage(message);
                    progressDialog.dismiss();
                    dbUserRef.removeEventListener(this);
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_LONG).show();

                }

            }
            );
        }
        else {Message message = new Message();
            message.arg1 = DONE_RETRIEVE_USER_DATA;
            handler.sendMessage(message);}
    }

    private void SaveActivities(int i,Meet meet,Handler handler)
    {
        BasicActivity basicActivity = meet.getActivities().get(i);

        if (basicActivity.getActivityID() == null)
        {
            DatabaseReference dbActivityRef = dbActivitiesRef.push();
            basicActivity.setActivityID(dbActivityRef.getKey());

            dbActivityRef.setValue(basicActivity, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (i < meet.getActivities().size()-1)
                        SaveActivities(i+1,meet,handler);
                    else if (error == null){
                        Message message = new Message();
                        message.arg1 = Meet.ACTIVITIES_SAVED;
                        handler.sendMessage(message);
                    }
                    else
                        Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG);
                }
              });
        }
        else
        {
            DatabaseReference dbActivityRef = dbActivitiesRef.child(basicActivity.getActivityID());
            dbActivityRef.setValue(basicActivity, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (i < meet.getActivities().size()-1 && error == null)
                        SaveActivities(i+1,meet,handler);
                    else if (error == null){
                        Message message = new Message();
                        message.arg1 = Meet.ACTIVITIES_SAVED;
                        handler.sendMessage(message);
                    }
                    else
                        Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG);
                }
            });
        }

    }

    public void SaveMeet(int position,Meet meet,int meetType,Handler handler)
    {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("retrieving info");
        progressDialog.setMessage("please wait");
        progressDialog.show();


        DatabaseReference dbMeetRef;

        if (meetType == Meet.NEW_MEET)
        {
            user.getMeetsList().add(meet);
            dbMeetRef = dbUserRef.child("meets_List").push();
            meet.setMeetID(dbMeetRef.getKey());

            for (BasicActivity b: meet.getActivities()) {
                b.setMeetID(meet.getMeetID());
            }

        }
        else if (meetType == Meet.EDIT_MEET)
        {
            user.meetsList.set(position,meet);
            dbMeetRef =  dbUserRef.child("meets_List").child(meet.getMeetID());
        }
        else
        {
            dbMeetRef = null;
            Toast.makeText(context,"ERROR",Toast.LENGTH_LONG);
        }



        Handler activitiesHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                if (message.arg1 == Meet.ACTIVITIES_SAVED)
                {
                    dbMeetRef.setValue(meet, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error ==null){
                                Message message1 = handler.obtainMessage();
                                message1.arg1 = Meet.MEET_SAVED;
                                handler.sendMessage(message1);
                            }
                            else
                                Toast.makeText(context,error.getMessage().toString(),Toast.LENGTH_LONG);
                        }
                    });
                }

                return true;
            }
        });
        SaveActivities(0,meet,activitiesHandler);
    }


    public void FindMeet(String creatorID , String meetID,Handler handler)
    {
     //   String crID = creatorID;
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("retrieving info");
        progressDialog.setMessage("please wait");
        progressDialog.show();
        dbRootRef.child("Users").child(creatorID).child("meets_List").child(meetID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               Meet meet =  snapshot.getValue(Meet.class);
               Message message = handler.obtainMessage();
               //Todo change new Message() to obtainMessage

               message.arg1 = Meet.MEET_OBTAINED;
               message.obj = meet;
               handler.sendMessage(message);
               progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    public void retrieveActivitiesList(Handler handler)
    {
        //TODO add sorting options


        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("retrieving info");
        progressDialog.setMessage("please wait");
        progressDialog.show();

        dbActivitiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<BasicActivity> alist = new ArrayList<>();

                for (DataSnapshot data: snapshot.getChildren()) {
                    alist.add(data.getValue(BasicActivity.class));
                }

                Message message = handler.obtainMessage();
                message.arg1 = DONE_RETRIEVE_USER_DATA;
                message.obj = alist;
                handler.sendMessage(message);
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG);
                progressDialog.dismiss();
            }
        });

    }

    public void SignOut(){
        auth.signOut();
        instance = null;
    }


}

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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class FirebaseHelper {

    private static FirebaseHelper instance = null;

    private FirebaseAuth auth;

    protected FirebaseDatabase firebaseDatabase;
    protected DatabaseReference dbRootRef,dbUserRef,dbActivitiesRef , dbRequestsRef;
    private static Context context;

    public static final int DONE_RETRIEVE_USER_DATA = 11 , DONE_RETRIEVE_REQUESTS = 91 , DONE_SAVE_REQUEST = 191 , DONE_SAVE_ANSWER = 211 , DONE_UPDATE_LIKES = 331;

    protected User user;

    protected String userID;

    protected ArrayList<Request> requestsList;

    public ValueEventListener requestsValueEventListener;




    private FirebaseHelper(){
        auth= FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbRootRef = firebaseDatabase.getReference();
        userID = auth.getCurrentUser().getUid();
        dbUserRef = firebaseDatabase.getReference("Users/"+userID);
        dbActivitiesRef = firebaseDatabase.getReference("Activities");
        dbRequestsRef = firebaseDatabase.getReference("Requests");
        user = null;
        requestsList = new ArrayList<>();
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
    public DatabaseReference getDbRequestsRef() {return dbRequestsRef;}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserId()
    {
        return userID;
    }

    public ArrayList<Request> getRequestsList() {
        return requestsList;
    }

    public void setRequestsList(ArrayList<Request> requestsList) {
        this.requestsList = requestsList;
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

;
            Log.d("user key", "retrieveUserData: " + userID);

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

    public void UpdateSettings(Handler handler)
    {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("saving info");
        progressDialog.setMessage("please wait");
        progressDialog.show();


        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("name",user.getName());
        hashMap.put("timeBeforeMeetNotif",user.getTimeBeforeMeetNotif());
        hashMap.put("beforeMeetNotification",user.getBeforeMeetNotification());

        dbUserRef.updateChildren(hashMap,new DatabaseReference.CompletionListener()
        {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null){
                Message message = handler.obtainMessage();
                message.arg1 = User.USER_UPDATED;
                handler.sendMessage(message);
                progressDialog.dismiss();
                }
            }
        });

    }

    private void SaveActivities(int i,Meet meet,Handler handler)
    {
         if (meet.getActivities().size() == 0) {
            Message message = new Message();
            message.arg1 = Meet.ACTIVITIES_SAVED;
            handler.sendMessage(message);
            return;
        }
        BasicActivity basicActivity = meet.getActivities().get(i);
        basicActivity.setCreator(user.getName());

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

            HashMap<String, Object> hashMap = new HashMap<>();
            ArrayList<String> exclude = new ArrayList<>();
            exclude.add("likes");
            exclude.add("disliked");
            exclude.add("liked");
            exclude.add("types");

            for (Field field : basicActivity.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                try {
                    if (!exclude.contains(field.getName())) { // Exclude specific fields
                        hashMap.put(field.getName(), field.get(basicActivity));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            dbActivityRef.updateChildren(hashMap, new DatabaseReference.CompletionListener() {
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
        progressDialog.setTitle("saving info");
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
            AlarmReciever.cancelAlarm(context,user.getMeetsList().indexOf(meet));
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

                                AlarmReciever.ScheduleMeetAlarm(context,user.getMeetsList().indexOf(meet),meet.getDate() - user.getTimeBeforeMeetNotif()*60000);

                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(meet.getDate() - user.getTimeBeforeMeetNotif()*60000);


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
                dbActivitiesRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG);
                progressDialog.dismiss();
            }
        });

    }

    public void UpdateLikes(BasicActivity basicActivity , String type , Handler handler)
    {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("saving info");
        progressDialog.setMessage("please wait");
        progressDialog.show();

        if (type.equals("liked"))
        {
            if (basicActivity.getLiked().contains(userID))
            {
                basicActivity.getLiked().remove(userID);
                basicActivity.setLikes(basicActivity.getLikes()-1);
            }
            else if (basicActivity.getDisliked().contains(userID))
            {
                basicActivity.getDisliked().remove(userID);
                basicActivity.getLiked().add(userID);
                basicActivity.setLikes(basicActivity.getLikes()+2);
            }
            else
            {
                basicActivity.getLiked().add(userID);
                basicActivity.setLikes(basicActivity.getLikes()+1);
            }
        }
        else if (type.equals("disliked"))
        {
            if (basicActivity.getDisliked().contains(userID))
            {
                basicActivity.getDisliked().remove(userID);
                basicActivity.setLikes(basicActivity.getLikes()+1);

            }
            else if (basicActivity.getLiked().contains(userID))
            {
                basicActivity.getDisliked().add(userID);
                basicActivity.getLiked().remove(userID);
                basicActivity.setLikes(basicActivity.getLikes()-2);

            }
            else
            {
                basicActivity.getDisliked().add(userID);
                basicActivity.setLikes(basicActivity.getLikes()-1);

            }
        }

        dbActivitiesRef.child(basicActivity.getActivityID()).setValue(basicActivity, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null){
                    Message message = handler.obtainMessage();
                    message.arg1 = DONE_UPDATE_LIKES;
                    handler.sendMessage(message);
                    progressDialog.dismiss();
                }
                else {
                    Toast.makeText(context,error.getMessage().toString(),Toast.LENGTH_LONG);
                    Log.d("my_error_debugger", "onComplete: error: " + error.getMessage().toString());
                }
            }
        });

    }

    public void retrieveRequests(Handler handler)
    {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("retrieving info");
        progressDialog.setMessage("please wait");
        progressDialog.show();

        requestsValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestsList = new ArrayList<>();
                for (DataSnapshot requestSnapshot:snapshot.getChildren()) {
                    Request request = new Request(
                            requestSnapshot.child("date").getValue(Long.class),
                            requestSnapshot.child("requesterID").getValue(String.class),
                            requestSnapshot.child("requesterName").getValue(String.class),
                            requestSnapshot.child("requestTitle").getValue(String.class),
                            requestSnapshot.child("requestContent").getValue(String.class),
                            requestSnapshot.child("index").getValue(int.class)
                            );

                    ArrayList<Answer> answers = new ArrayList<>();
                    for (DataSnapshot answerSnapshot    :   requestSnapshot.child("answers").getChildren()) {
                        int type = answerSnapshot.child("type").getValue(int.class);
                        switch (type)
                        {
                            case   Answer.TYPE_ACTIVITY:
                                ActivityAnswer activityAnswer = answerSnapshot.getValue(ActivityAnswer.class);
                                answers.add(activityAnswer);
                                break;
                            case Answer.TYPE_TEXT:
                                TextAnswer textAnswer = answerSnapshot.getValue(TextAnswer.class);
                                answers.add(textAnswer);
                                break;

                            case Answer.TYPE_MEET:
                                MeetAnswer meetAnswer = answerSnapshot.getValue(MeetAnswer.class);
                                answers.add(meetAnswer);
                                break;
                        }
                    }

                    request.setAnswers(answers);
                    requestsList.add(request);
                }
                Message message = handler.obtainMessage();
                message.arg1 = DONE_RETRIEVE_REQUESTS;

                handler.sendMessage(message);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG);
                progressDialog.dismiss();
            }
        };
        dbRequestsRef.addValueEventListener(requestsValueEventListener);
    }

    public void AddRequest(Request request,Handler handler)
    {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("saving info");
        progressDialog.setMessage("please wait");
        progressDialog.show();

        if (requestsList != null)
            request.setIndex(requestsList.size());
        else request.setIndex(0);
        requestsList.add(request);

        dbRequestsRef.setValue(requestsList, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null)
                {
                    Message message = handler.obtainMessage();
                    message.arg1 = DONE_SAVE_REQUEST;
                    handler.sendMessage(message);
                }
                progressDialog.dismiss();
            }
        });

    }

    public void SaveAnswer(Request request , Answer answer , Handler handler)
    {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("saving info");
        progressDialog.setMessage("please wait");
        progressDialog.show();

        request.getAnswers().add(answer);
        dbRequestsRef.child(request.getIndex()+"").child("answers").setValue(request.getAnswers(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null)
                {
                    Message message = handler.obtainMessage();
                    message.arg1 = DONE_SAVE_ANSWER;
                    handler.sendMessage(message);
                }
                progressDialog.dismiss();
            }
        });
    }

    public void SignOut(){
        auth.signOut();
        instance = null;
    }


}

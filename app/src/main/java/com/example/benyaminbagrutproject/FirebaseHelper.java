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

/**
 * Helper class for managing Firebase Realtime Database operations in the youth movement guide application.
 * This class implements the Singleton pattern to ensure a single instance manages all Firebase interactions.
 * 
 * Key responsibilities:
 * - User data synchronization and settings management
 * - Activity creation, storage, and retrieval
 * - Meeting management (saving, retrieving, updating)
 * - Request and answer handling between guides
 * - Like/dislike system for activities
 * 
 * The class uses Android's Handler system for asynchronous operations and maintains
 * consistency between local data and Firebase.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class FirebaseHelper {
    /** Singleton instance of FirebaseHelper */
    private static FirebaseHelper instance = null;

    /** Firebase Authentication instance for user management */
    private FirebaseAuth auth;

    /** Main Firebase Database instance */
    protected FirebaseDatabase firebaseDatabase;
    
    /** Database references for different paths in Firebase */
    protected DatabaseReference dbRootRef, /** Root reference */
                              dbUserRef,   /** User-specific reference */
                              dbActivitiesRef, /** Activities reference */
                              dbRequestsRef;   /** Requests reference */

    /** Application context for UI operations */
    private static Context context;

    /** Status codes for different Firebase operations */
    public static final int DONE_RETRIEVE_USER_DATA = 11,  /** User data retrieved successfully */
                          DONE_RETRIEVE_REQUESTS = 91,    /** Requests retrieved successfully */
                          DONE_SAVE_REQUEST = 191,        /** Request saved successfully */
                          DONE_SAVE_ANSWER = 211,         /** Answer saved successfully */
                          DONE_UPDATE_LIKES = 331;        /** Likes updated successfully */

    /** Current user object containing profile and settings */
    protected User user;

    /** Current user's Firebase UID */
    protected String userID;

    /** List of requests in the system */
    protected ArrayList<Request> requestsList;

    /** Event listener for requests updates */
    public ValueEventListener requestsValueEventListener;

    /**
     * Private constructor for Singleton pattern.
     * Initializes Firebase instances and references.
     */
    private FirebaseHelper() {
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

    /**
     * Gets or creates the singleton instance of FirebaseHelper.
     * 
     * @param ctx Application context for UI operations
     * @return The singleton instance of FirebaseHelper
     */
    public static FirebaseHelper getInstance(Context ctx ){
        context = ctx;
        if (instance == null){
            instance = new FirebaseHelper();
        }
        return instance;
    }

    /**
     * Gets the root database reference.
     * 
     * @return Root DatabaseReference for Firebase operations
     */
    public DatabaseReference getDbRootRef() {
        return dbRootRef;
    }

    /**
     * Gets the user-specific database reference.
     * 
     * @return DatabaseReference for current user's data
     */
    public DatabaseReference getDbUserRef() {
        return dbUserRef;
    }

    /**
     * Gets the requests database reference.
     * 
     * @return DatabaseReference for all requests
     */
    public DatabaseReference getDbRequestsRef() {return dbRequestsRef;}

    /**
     * Gets the current user object.
     * 
     * @return User object containing profile and settings
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the current user object.
     * 
     * @param user New User object to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the current user's Firebase UID.
     * 
     * @return String containing the user's unique identifier
     */
    public String getUserId()
    {
        return userID;
    }

    /**
     * Gets the list of all requests in the system.
     * 
     * @return ArrayList of Request objects
     */
    public ArrayList<Request> getRequestsList() {
        return requestsList;
    }

    /**
     * Sets the list of requests in the system.
     * 
     * @param requestsList New ArrayList of Request objects
     */
    public void setRequestsList(ArrayList<Request> requestsList) {
        this.requestsList = requestsList;
    }

    /**
     * Retrieves the current user's data from Firebase.
     * Updates user information and settings locally. Shows a progress dialog
     * during the operation and handles both success and error cases.
     * 
     * If user data is already loaded, immediately returns success.
     * 
     * @param handler Handler to process database operation results with DONE_RETRIEVE_USER_DATA
     */
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

    /**
     * Updates the user's notification settings in Firebase.
     * Saves the following settings:
     * - User's display name
     * - Notification timing preference
     * - Notification enabled/disabled state
     * 
     * Shows a progress dialog during the operation.
     * 
     * @param handler Handler to process the update operation result with USER_UPDATED
     */
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

    /**
     * Saves activities in a meet to Firebase Database.
     * Each activity is saved with its relationship to the meet.
     * Handles both new activities (creates new IDs) and existing ones (updates).
     * 
     * For existing activities, excludes certain fields (likes, dislikes) from updates.
     * Uses reflection to handle field updates efficiently.
     * 
     * @param i Current activity index in the meet
     * @param meet Meet containing the activities
     * @param handler Handler to process save operation results with ACTIVITIES_SAVED
     */
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

    /**
     * Saves or updates a meet in Firebase Database.
     * Creates new meet or updates existing one based on meetType.
     * 
     * For new meets:
     * - Generates new meet ID
     * - Links activities to the meet
     * - Adds to user's meet list
     * 
     * For existing meets:
     * - Updates meet data
     * - Handles alarm rescheduling
     * 
     * @param position Position of the meet in the list (if updating)
     * @param meet Meet object to save
     * @param meetType Type of operation (NEW_MEET or EDIT_MEET)
     * @param handler Handler to process save operation results with MEET_SAVED
     */
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

    /**
     * Finds and retrieves a specific meet from Firebase.
     * Navigates the database path: Users/{creatorID}/meets_List/{meetID}
     * Shows a progress dialog during retrieval.
     * 
     * @param creatorID ID of the meet creator
     * @param meetID Unique identifier of the meet
     * @param handler Handler to process meet retrieval results with MEET_OBTAINED
     */
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

    /**
     * Retrieves the list of all activities from Firebase.
     * Shows a progress dialog during retrieval.
     * Converts Firebase data snapshots to BasicActivity objects.
     * 
     * @param handler Handler to process activity list retrieval results with DONE_RETRIEVE_USER_DATA
     */
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

    /**
     * Updates the likes/dislikes for an activity.
     * Handles multiple scenarios:
     * - Adding/removing likes
     * - Adding/removing dislikes
     * - Switching between like/dislike
     * - Updating like count
     * 
     * Shows a progress dialog during the update.
     * 
     * @param basicActivity Activity to update
     * @param type Type of update ("liked" or "disliked")
     * @param handler Handler to process update results with DONE_UPDATE_LIKES
     */
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

    /**
     * Retrieves all requests from Firebase.
     * Sets up a persistent ValueEventListener to track request changes.
     * Shows a progress dialog during initial retrieval.
     * 
     * @param handler Handler to process request list retrieval results with DONE_RETRIEVE_REQUESTS
     */
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

    /**
     * Adds a new request to Firebase Database.
     * Automatically assigns an index based on current request count.
     * Shows a progress dialog during the save operation.
     * 
     * @param request Request object to add
     * @param handler Handler to process add operation results with DONE_SAVE_REQUEST
     */
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

    /**
     * Saves an answer to a request in Firebase Database.
     * Updates the answers list in the specific request path.
     * Shows a progress dialog during the save operation.
     * 
     * @param request Request being answered
     * @param answer Answer to save (can be TextAnswer, ActivityAnswer, or MeetAnswer)
     * @param handler Handler to process save operation results with DONE_SAVE_ANSWER
     */
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

    /**
     * Signs out the current user from Firebase Authentication.
     * Cleans up the singleton instance for proper reset.
     */
    public void SignOut(){
        auth.signOut();
        instance = null;
    }


}

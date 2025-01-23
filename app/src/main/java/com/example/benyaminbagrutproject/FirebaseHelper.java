package com.example.benyaminbagrutproject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseHelper {

    private static FirebaseHelper instance = null;

    private FirebaseAuth auth;
    protected DatabaseReference dbRef;
    private Context context;

    protected User user;




    private FirebaseHelper(){
        auth= FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();
    }



    public static FirebaseHelper getInstance(Context context){
        if (instance == null){
            instance = new FirebaseHelper();
        }
        return instance;
    }

    public User retrieveUserData()
    {
        String key  =auth.getCurrentUser().getUid();
        Log.d("user key", "retrieveUserData: "+key);
        DatabaseReference  dbUsersRef = FirebaseDatabase.getInstance().getReference("Users/"+key);

        dbUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context,databaseError.getMessage(),Toast.LENGTH_LONG);
            }

        }
        );

        return user;
    }

    public void SignOut(){
        auth.signOut();
    }


}

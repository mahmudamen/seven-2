package com.newaswan.seven;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;

public class ChatActivity extends AppCompatActivity {
    LinearLayout layout;
    ImageView sendButton;
    FloatingActionButton fabuser;
    EditText messageArea;
    int badgeCount;
    ScrollView scrollView;
    Firebase reference1;
    Firebase reference2;
    Firebase reference3;
    DatabaseReference reference4;
    private FirebaseAuth firebaseAuth;
    String chatID = "chatread";
    static boolean isInitialized = false;
    private static String TAG = "ChatActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            if(!isInitialized){
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                isInitialized = true;
            }else {
                Log.d(TAG,"Already Initialized");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        setContentView(R.layout.activity_chat);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo currentNetworkInfo = connectivityManager.getActiveNetworkInfo();
        conncet(currentNetworkInfo);
        Firebase.setAndroidContext(this);
        final Intent i = new Intent(this,Users.class);
        fabuser = (FloatingActionButton)findViewById(R.id.fablist);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), SinginActivity.class));

        }else  {
            conncet(currentNetworkInfo);
            final FirebaseUser user = firebaseAuth.getCurrentUser();
            List<String> s = Arrays.asList("hany230000@gmail.com", "mahmudamen@gmail.com");
            if ( s.contains(user.getEmail()))  {
                fabuser.setVisibility(View.VISIBLE);

            }else{
                fabuser.setVisibility(View.INVISIBLE);
                UserDetails.username = usernameFromEmail(user.getEmail());
                UserDetails.chatWith = "hany230000";
            }
            fabuser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(i);
                }
            });
            reference1 = new Firebase("https://seven-1810b.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
            reference2 = new Firebase("https://seven-1810b.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);
            reference3 = new Firebase("https://seven-1810b.firebaseio.com/messages/" + "mahmudamen" + "_" + UserDetails.username);

            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            reference4  = mDatabase.child("users").child(UserDetails.username).child("chat");
            layout = (LinearLayout)findViewById(R.id.layout1);
            sendButton = (ImageView)findViewById(R.id.sendButton);
            messageArea = (EditText)findViewById(R.id.messageArea);
            scrollView = (ScrollView)findViewById(R.id.scrollView);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conncet(currentNetworkInfo);
                String messageText = messageArea.getText().toString();
                SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                String format = s.format(new Date());
                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    map.put("id",user.getUid());
                    map.put("email",user.getEmail());
                    map.put("date",format);
                    map.put("chatread","0");
                    conncet(currentNetworkInfo);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    reference3.push().setValue(map);
                    reference4.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            Long value = mutableData.getValue(Long.class);
                            if (value == null) {
                                mutableData.setValue(0);
                            }
                            else {
                                mutableData.setValue(value + 1);
                            }

                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, com.google.firebase.database.DataSnapshot dataSnapshot) {
                            // Transaction completed
                            Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                        }
                    });
                }
            }
        });
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String UID = currentUser.getUid();
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            Query query = rootRef.child("messages").child(UserDetails.username + "_" + UserDetails.chatWith).orderByChild(UID).equalTo(false);
            query.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
                @Override
                public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                String CreateDate = map.get("date").toString();



              //  mDatabase.child("users").child(userName).child("chat").setValue(1);

                if(userName.equals(UserDetails.username)){
                    conncet(currentNetworkInfo);
                    addMessageBox(user.getEmail() + ":-\n" + message +"\n"+ CreateDate , 1);
                }
                else{
                    conncet(currentNetworkInfo);
                    addMessageBox(UserDetails.chatWith + ":-\n" + message +"\n"+ CreateDate , 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
    public void addMessageBox(String message, int type){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo currentNetworkInfo = connectivityManager.getActiveNetworkInfo();
        conncet(currentNetworkInfo);
        TextView textView = new TextView(ChatActivity.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);
        textView.setLayoutParams(lp);
        if(type == 1) {
            textView.setBackgroundResource(R.drawable.rounded_corner1);
        }
        else{
            textView.setBackgroundResource(R.drawable.rounded_corner2);
        }
        EditText editText = (EditText)findViewById(R.id.messageArea);
        editText.setText("");
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
    public void conncet(NetworkInfo currentNetworkInfo){
        final Toast j = Toast.makeText(this,"تأكد من الاتصال بالانترنت ",Toast.LENGTH_SHORT);
        if(currentNetworkInfo == null ){
            j.show();
        }
    }
    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
    public void newMessageBox(){
        String chatID = "chatread";
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null){
            String UID = currentUser.getUid();
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            Query query = rootRef.child("messages").child(UID).orderByChild(chatID).equalTo(false);
            query.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
                @Override
                public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                    boolean success = ShortcutBadger.applyCount(ChatActivity.this,badgeCount );
                    startService(
                            new Intent(ChatActivity.this, BadgeIntentService.class).putExtra("badgeCount", badgeCount)
                    );
                }

                @Override
                public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
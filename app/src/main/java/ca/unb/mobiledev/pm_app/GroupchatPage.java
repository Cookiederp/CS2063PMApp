package ca.unb.mobiledev.pm_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ca.unb.mobiledev.pm_app.Adapter.MessageAdapter;
import ca.unb.mobiledev.pm_app.Model.Chat;
import ca.unb.mobiledev.pm_app.Model.Users;

public class GroupchatPage extends AppCompatActivity {

    String userId;
    String projectId;
    String senderName;
    private String imageURL;

    private EditText messageEditText;
    private ImageButton sendBtn;

    private RecyclerView recyclerView;

    DatabaseReference reference;

    private MessageAdapter messageAdapter;
    private List<Chat> mChat;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchatpage);

        messageEditText = findViewById(R.id.text_send);
        sendBtn = findViewById(R.id.btn_send);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);



        FirebaseAuth auth = FirebaseAuth.getInstance();
        userId = auth.getUid();

        Intent intent = getIntent();
        projectId = intent.getStringExtra("projectId");


        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        //retrieve the data of the signed in user
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                imageURL = users.getProfilePicURL();
                senderName = users.getFirstName() + " " + users.getLastName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        readMessages();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = messageEditText.getText().toString();
                if(!msg.equals("")){
                    sendMessage(userId, msg, imageURL, senderName);
                }
                else{
                    //message empty
                }

                messageEditText.setText("");
            }
        });
    }

    private void sendMessage(String sender, String message, String imageURL, String senderName){
        reference = FirebaseDatabase.getInstance().getReference("Groupchats");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("imageURL", imageURL);
        hashMap.put("message", message);
        hashMap.put("projectId", projectId);
        hashMap.put("senderName",senderName);
        hashMap.put("timestamp", ServerValue.TIMESTAMP);

        reference.child(projectId).push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //message sent
                }
            }
        });


    }

    private void readMessages(){
        mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Groupchats").child(projectId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChat.clear();
                for(DataSnapshot child : snapshot.getChildren()){

                    Chat chat = child.getValue(Chat.class);

                    mChat.add(chat);
                }

                messageAdapter = new MessageAdapter(GroupchatPage.this, mChat);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

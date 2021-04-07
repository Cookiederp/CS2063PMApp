package ca.unb.mobiledev.pm_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class MessageDetail extends AppCompatActivity {

    private TextView timeSentText;
    private TextView daySentText;
    private TextView senderAndMessageText;
    private Button deleteBtn;

    DatabaseReference ref;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagedetail);
        getSupportActionBar().setTitle("Message Detail");
        timeSentText = findViewById(R.id.tv_timesentdetail);
        daySentText = findViewById(R.id.tv_daysentdetail);
        senderAndMessageText = findViewById(R.id.tv_senderandmessage);
        deleteBtn = findViewById(R.id.btn_deletemessage);

        //getting info from clicked task in previous activity
        Intent intent = getIntent();
        timeSentText.setText(intent.getStringExtra("timeSent"));
        daySentText.setText(intent.getStringExtra("daySent"));
        String message = intent.getStringExtra("message");
        String sender = intent.getStringExtra("sender");
        String senderId = intent.getStringExtra("senderId");
        String projectId = intent.getStringExtra("projectId");
        String messageId = intent.getStringExtra("messageId");

        ref = FirebaseDatabase.getInstance().getReference("Groupchats").child(projectId).child(messageId);

        if(message.length() > 10){
            String m = message.substring(0, 10);
            senderAndMessageText.setText(sender + ": " + m + "...");
        }
        else{
            senderAndMessageText.setText(sender + ": " + message);
        }

        if(!senderId.equals(FirebaseAuth.getInstance().getUid())){
            deleteBtn.setVisibility(View.GONE);
        }

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMessage();
            }
        });

    }

    private void deleteMessage(){
        ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    finish();
                }
                else{
                    Toast.makeText(MessageDetail.this, "Error Deleting the Message.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

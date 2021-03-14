package ca.unb.mobiledev.pm_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class CreateTask extends AppCompatActivity {

    private Button createTaskButton;
    private TextView nameET;
    private TextView descriptionET;

    private String taskName;
    private String taskDesc;

    //Firebase
    FirebaseAuth auth;
    DatabaseReference myRef;
    FirebaseUser firebaseUser;
    private String firebaseUserId;
    private String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createtask);

        createTaskButton = findViewById(R.id.btn_createtask2);
        nameET = findViewById(R.id.et_taskname);
        descriptionET = findViewById(R.id.et_taskdesc);

        Intent intent = getIntent();
        projectId = intent.getStringExtra("projectId");

        myRef = FirebaseDatabase.getInstance().getReference("Tasks").child(projectId).push();


        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                taskName = nameET.getText().toString();
                taskDesc = descriptionET.getText().toString();
                //Not all fields filled, show error message to user
                if (TextUtils.isEmpty(taskName) || TextUtils.isEmpty(taskDesc)) {
                    Toast.makeText(CreateTask.this, "Error. Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                //All fields filled, send fields to database to make a task.
                else {
                    createTask();
                }
            }
        });
    }

    private void createTask() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Id", myRef.getKey());
        hashMap.put("projectId", projectId);
        hashMap.put("title", taskName);
        hashMap.put("description", taskDesc);
        hashMap.put("deadline", "ToBeAdded");

        myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    finish();
                }
            }
        });



    }

}
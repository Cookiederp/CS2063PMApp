package ca.unb.mobiledev.pm_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import ca.unb.mobiledev.pm_app.Model.Tasks;

public class TaskDetail extends AppCompatActivity {


    private Button deleteTaskButton;
    private TextView taskNameText;
    private TextView taskDescriptionText;

    private String projectId;
    private String taskId;

    //get logged in user
    private FirebaseAuth auth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskdetail);


        taskNameText = findViewById(R.id.tv_taskname);
        taskDescriptionText = findViewById(R.id.tv_taskdesc);
        deleteTaskButton = findViewById(R.id.btn_deletetask);

        //getting info from clicked task in previous activity
        Intent intent = getIntent();
        projectId = intent.getStringExtra("projectId");
        taskId = intent.getStringExtra("taskId");
        String taskName = intent.getStringExtra("taskName");
        String taskDescription = intent.getStringExtra("taskDescription");

        taskNameText.setText(taskName);
        taskDescriptionText.setText(taskDescription);

        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTask();
            }
        });



    }


    private void deleteTask(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Tasks").child(projectId).child(taskId);

        myRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    finish();
                }
                else{
                    Toast.makeText(TaskDetail.this, "Error Deleting the Task.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

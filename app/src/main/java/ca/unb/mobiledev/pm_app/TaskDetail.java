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
import com.google.firebase.database.ServerValue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import ca.unb.mobiledev.pm_app.Model.Tasks;

public class TaskDetail extends AppCompatActivity {


    private Button deleteTaskButton;
    private TextView taskNameText;
    private TextView taskDescriptionText;
    private TextView taskDeadlineText;

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
        taskDeadlineText = findViewById(R.id.tv_deadline);


        //getting info from clicked task in previous activity
        Intent intent = getIntent();
        projectId = intent.getStringExtra("projectId");
        taskId = intent.getStringExtra("taskId");
        String taskName = intent.getStringExtra("taskName");
        String taskDescription = intent.getStringExtra("taskDescription");
        long taskDeadline = intent.getLongExtra("taskDeadline", 0);


        Date d = new Date(taskDeadline);
        /*
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(d);
        StringBuilder dayString = new StringBuilder();
        dayString.append(calendar.get(Calendar.DAY_OF_MONTH)).append("/");
        dayString.append(calendar.get(Calendar.MONTH)+1).append("/");
        dayString.append(calendar.get(Calendar.YEAR));

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date d = null;
        try {
            d = formatter.parse(taskDeadline);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        */
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        taskDeadlineText.setText("Deadline: " + dateFormat.format(d));

        //ServerValue.TIMESTAMP
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

package ca.unb.mobiledev.pm_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class CreateTask extends AppCompatActivity {

    private Button createTaskButton;
    private TextView nameET;
    private TextView descriptionET;
    private CalendarView calendarView;
    private Long timestampDeadline;

    private String taskName;
    private String taskDesc;
    private String taskDeadline;

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
        calendarView = findViewById(R.id.calendarView);

        Intent intent = getIntent();
        projectId = intent.getStringExtra("projectId");

        myRef = FirebaseDatabase.getInstance().getReference("Tasks").child(projectId).push();

        taskDeadline = "none";
        getSupportActionBar().setTitle("Create Task");
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


        timestampDeadline = calendarView.getDate();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {


                //taskDeadline = i2 + "-" + (i1+1) + "-" + i;
                Date date = new GregorianCalendar(year, month, day).getTime();
                timestampDeadline = new Timestamp(date.getTime()).getTime();
            }
        });
    }

    private void createTask() {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Id", myRef.getKey());
        hashMap.put("projectId", projectId);
        hashMap.put("title", taskName);
        hashMap.put("description", taskDesc);
        hashMap.put("deadline", timestampDeadline);

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
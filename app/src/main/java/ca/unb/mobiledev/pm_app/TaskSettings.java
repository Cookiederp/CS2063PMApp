package ca.unb.mobiledev.pm_app;

import android.app.Activity;
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

import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class TaskSettings extends AppCompatActivity {

    private Button saveTaskChangeButton;
    private Button deleteTaskButton;
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
    private String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasksettings);

        saveTaskChangeButton = findViewById(R.id.btn_savetaskchange);
        deleteTaskButton = findViewById(R.id.btn_taskdelete);
        nameET = findViewById(R.id.et_changetasktitle);
        descriptionET = findViewById(R.id.et_changetaskdesc);
        calendarView = findViewById(R.id.cv_changedeadline);


        Intent intent = getIntent();
        projectId = intent.getStringExtra("projectId");
        taskId = intent.getStringExtra("taskId");
        nameET.setText(intent.getStringExtra("taskTitle"));
        descriptionET.setText(intent.getStringExtra("taskDesc"));


        myRef = FirebaseDatabase.getInstance().getReference("Tasks").child(projectId).child(taskId);

        taskDeadline = "none";
        getSupportActionBar().setTitle("Edit Task");

        timestampDeadline = intent.getLongExtra("taskDeadline", 0);
        calendarView.setDate(timestampDeadline);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {


                //taskDeadline = i2 + "-" + (i1+1) + "-" + i;
                Date date = new GregorianCalendar(year, month, day).getTime();
                timestampDeadline = new Timestamp(date.getTime()).getTime();
            }
        });

        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTask();
            }
        });


        saveTaskChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date deadlineDate = new Date(timestampDeadline);
                Date currentDate = new Date();
                long difference_In_Time = deadlineDate.getTime() - currentDate.getTime();
                long difference_In_Days = ((difference_In_Time / (1000 * 60 * 60 * 24)));

                taskName = nameET.getText().toString();
                taskDesc = descriptionET.getText().toString();
                //Not all fields filled, show error message to user
                if (TextUtils.isEmpty(taskName) || TextUtils.isEmpty(taskDesc)) {
                    Toast.makeText(TaskSettings.this, "Error. Please fill all fields.", Toast.LENGTH_SHORT).show();
                }
                else if(difference_In_Days < 0){
                    Toast.makeText(TaskSettings.this, "Error. Deadline entered is invalid.", Toast.LENGTH_SHORT).show();
                }
                //All fields filled, send fields to database to make a task.
                else {
                    updateTask();
                    myRef = FirebaseDatabase.getInstance().getReference("Tasks").child(projectId).child(taskId);

                }
            }
        });






    }

    private void updateTask() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("title", nameET.getText().toString());
        map.put("description", descriptionET.getText().toString());
        map.put("deadline", timestampDeadline);
        myRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(TaskSettings.this, "Saved.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent();
                    i.putExtra("taskTitle", taskName);
                    i.putExtra("taskDesc", taskDesc);
                    i.putExtra("taskDeadline", timestampDeadline);
                    setResult(Activity.RESULT_OK, i);
                    finish();
                }
                else{
                    Toast.makeText(TaskSettings.this, "Error.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void deleteTask(){
        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tasks").child(projectId).child(taskId);

        myRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    setResult(5);
                    finish();
                }
                else{
                    Toast.makeText(TaskSettings.this, "Error Deleting the Task.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}

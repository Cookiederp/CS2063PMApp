package ca.unb.mobiledev.pm_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.preference.PreferenceManager;
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

import java.text.DateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class TaskDetail extends AppCompatActivity {


    private Button settingsButton;
    private Button closeTaskButton;
    private TextView taskNameText;
    private TextView taskDescriptionText;

    private TextView taskDeadlineText;
    String taskName;
    String taskDescription;
    long taskDeadline;
    private String projectId;
    private String taskId;

    //get logged in user
    private FirebaseAuth auth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskdetail);
        getSupportActionBar().setTitle("Task Details");

        taskNameText = findViewById(R.id.tv_taskname);
        taskDescriptionText = findViewById(R.id.tv_taskdesc);
        //taskDeadline = findViewById(R.id.tv_taskduedate);
        settingsButton = findViewById(R.id.btn_tasksettings);
        taskDeadlineText = findViewById(R.id.tv_deadline);
        closeTaskButton = findViewById(R.id.btn_closetask);


        //getting info from clicked task in previous activity
        Intent intent = getIntent();
        projectId = intent.getStringExtra("projectId");
        taskId = intent.getStringExtra("taskId");
        taskName = intent.getStringExtra("taskName");
        taskDescription = intent.getStringExtra("taskDescription");
        //String taskDueDate = intent.getStringExtra("taskDueDate");
        //String timeToDue;


        taskDeadline = intent.getLongExtra("taskDeadline", 0);


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

        //ServerValue.TIMESTAMP
        taskNameText.setText(taskName);
        taskDescriptionText.setText(taskDescription);
        taskDeadlineText.setText("Deadline: " + dateFormat.format(d));
        //taskDeadlineText.setText(taskDueDate);


        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskDetail.this, TaskSettings.class);
                intent.putExtra("projectId", projectId);
                intent.putExtra("taskId", taskId);
                intent.putExtra("taskTitle", taskName);
                intent.putExtra("taskDesc", taskDescription);
                intent.putExtra("taskDeadline", taskDeadline);
                startActivityForResult(intent, 1);
            }
        });

        closeTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            taskName = data.getStringExtra("taskTitle");
            taskDescription = data.getStringExtra("taskDesc");

            taskNameText.setText(taskName);
            taskDescriptionText.setText(taskDescription);

            taskDeadline = data.getLongExtra("taskDeadline", 0);
            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
            Date d = new Date(taskDeadline);
            taskDeadlineText.setText("Deadline: " + dateFormat.format(d));
        }
        else if(resultCode == 5){
            Log.d("123123", "onActivityResult: ");
            finish();
        }
    }
}

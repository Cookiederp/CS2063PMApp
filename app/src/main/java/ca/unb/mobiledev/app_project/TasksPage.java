package ca.unb.mobiledev.app_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class TasksPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskspage_activity);

        getSupportActionBar().setTitle("Task 1");
    }

}

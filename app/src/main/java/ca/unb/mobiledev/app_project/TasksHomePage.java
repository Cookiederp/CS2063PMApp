package ca.unb.mobiledev.app_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TasksHomePage extends AppCompatActivity {
    TextView t1;
    FloatingActionButton popUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskshome_activity);

        getSupportActionBar().setTitle("Task");


        t1 = findViewById(R.id.tasksview);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(TasksHomePage.this, TasksPage.class);
                startActivity(intent);
            }
        });

        popUp = findViewById(R.id.addtasks);
        popUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(TasksHomePage.this, PopForm.class);
                startActivity(intent);
            }
        });
    }
}

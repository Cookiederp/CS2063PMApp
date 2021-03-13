package ca.unb.mobiledev.pm_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ca.unb.mobiledev.pm_app.Model.Projects;

public class ProjectPage extends AppCompatActivity {

    private TextView teamPage;
    private TextView tasks;
    private TextView chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectpage);


        //click listener to view tasks list for specific project
        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tasksIntent = new Intent(ProjectPage.this, TasksList.class);
                startActivity(tasksIntent);
            }
        });

    }
}

//
//       projectNameTextView = findViewById(R.id.tv_projectname);
//
//               Intent intent = getIntent();
//               String projectId = intent.getStringExtra("projectId");
//               String projectName = intent.getStringExtra("projectName");
//               projectNameTextView.setText(projectName);

package ca.unb.mobiledev.app_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProjectPage extends AppCompatActivity {
    TextView team;
    TextView chat;
    TextView tasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projectpage_activity);
        team = findViewById(R.id.team_view);
        chat = findViewById(R.id.chat_view);
        tasks = findViewById(R.id.task_view);

        team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ProjectPage.this, TeamPage.class);
                startActivity(intent);
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ProjectPage.this, ChatPage.class);
                startActivity(intent);
            }
        });

        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ProjectPage.this, TasksHomePage.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().setTitle("App Dev");
    }



}
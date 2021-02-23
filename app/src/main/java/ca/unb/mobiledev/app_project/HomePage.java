package ca.unb.mobiledev.app_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomePage extends AppCompatActivity {
    TextView project;
    FloatingActionButton popUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_activity);
        project = findViewById(R.id.p1_view);
        project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HomePage.this, ProjectPage.class);
                startActivity(intent);
            }
        });

        popUp = findViewById(R.id.addproject_button);
        popUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HomePage.this, PopForm.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().setTitle("Projects");
    }
}

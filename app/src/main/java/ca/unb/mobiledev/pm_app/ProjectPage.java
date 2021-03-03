package ca.unb.mobiledev.pm_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProjectPage extends AppCompatActivity {

    private TextView projectNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectpage);

        projectNameTextView = findViewById(R.id.tv_projectname);

        Intent intent = getIntent();
        String projectId = intent.getStringExtra("projectId");
        String projectName = intent.getStringExtra("projectName");
        projectNameTextView.setText(projectName);

    }
}

package ca.unb.mobiledev.pm_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import ca.unb.mobiledev.pm_app.Model.Users;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountHomePage extends AppCompatActivity {

    private Button logoutBtn;
    private Button createProjectBtn;
    private Button projectsBtn;
    TextView register;



    FirebaseUser firebaseUser;
    DatabaseReference usersRef;
    private String currentUserId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounthompage);

        logoutBtn = findViewById(R.id.btn_logout);
        createProjectBtn = findViewById(R.id.btn_createproj);
        projectsBtn = findViewById(R.id.btn_projects);
        //firebase
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = firebaseUser.getUid();
        usersRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId);


        //retrieve the data of the signed in user, might be useful later, right now it is useless
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                Toast.makeText(AccountHomePage.this, "User First Name: " + users.getFirstName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AccountHomePage.this, MainActivity.class));
                finish();
            }
        });


        createProjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountHomePage.this, CreateProject.class));
            }
        });


        projectsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountHomePage.this, ProjectsList.class));
            }
        });

    }






}


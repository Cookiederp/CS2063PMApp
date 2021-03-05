package ca.unb.mobiledev.pm_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import DataModels.UserModel;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import ca.unb.mobiledev.pm_app.Model.Projects;

public class CreateTeam extends AppCompatActivity {
    private Button createTeamBtn;
    private EditText et_tName;

    //Firebase
    FirebaseAuth auth;
    DatabaseReference myRef;
    FirebaseUser firebaseUser;
    private String firebaseUserId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createteam);

        //instantiate all layout elements
        createTeamBtn = findViewById(R.id.btn_teamcreate);
        et_tName = findViewById(R.id.et_tName);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        //removes the need to call .getUid later
        firebaseUserId = firebaseUser.getUid();

        //listeners for each layout element
        createTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String TeamName_text = et_tName.getText().toString();

                //Not all fields filled, show error message to user
                if(TextUtils.isEmpty(TeamName_text)) {
                    Toast.makeText(CreateTeam.this, "Error. Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                //All fields filled, send fields to database to make a project.
                else {
                    createTeam(TeamName_text);
                }
            }
        });

    }

    private void createTeam(String tName){

        //push new project to Projects in firebase
        myRef = FirebaseDatabase.getInstance().getReference("Projects").push();
        String projectId = myRef.getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("projectName", tName);
        hashMap.put("id", projectId); //generate key from the push to assign to project. Each key is unique and it will be how projects are found

        myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    HashMap<String, String> hashMap1 = new HashMap<>();
                    hashMap1.put("userId", firebaseUserId);
                    hashMap1.put("role", "owner");

                    myRef.child("Members").child(firebaseUserId).setValue(hashMap1)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        assignProjectToCurrentUser(projectId);
                                        Intent intent = new Intent(CreateProject.this, AccountHomePage.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }
}

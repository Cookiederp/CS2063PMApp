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

public class CreateProject extends AppCompatActivity {

    private Button createProjectBtn;
    private EditText et_pname;

    //Firebase
    FirebaseAuth auth;
    DatabaseReference myRef;
    FirebaseUser firebaseUser;
    private String firebaseUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createproject);

        //instantiate all layout elements
        createProjectBtn = findViewById(R.id.btn_create);
        et_pname = findViewById(R.id.et_pname);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        //removes the need to call .getUid later
        firebaseUserId = firebaseUser.getUid();

        //listeners for each layout element
        createProjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String projectName_text = et_pname.getText().toString();

                //Not all fields filled, show error message to user
                if(TextUtils.isEmpty(projectName_text)) {
                    Toast.makeText(CreateProject.this, "Error. Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                //All fields filled, send fields to database to make a project.
                else {
                    createProject(projectName_text);
                }
            }
        });

    }


    private void createProject(String pName){

        //push new project to Projects in firebase
        myRef = FirebaseDatabase.getInstance().getReference("Projects").push();
        String projectId = myRef.getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("projectName", pName);
        hashMap.put("iconPicURL", "default");
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

    private void assignProjectToCurrentUser(String projectId){

        ArrayList<String> temp = new ArrayList<String>();

        //get to users/projectIds, to store a new projectId (projectId is all the project the user is in)
        DatabaseReference myRefUser = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUserId).child("projectIds");
        myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                ArrayList<String> pIdList = snapshot.getValue(t);

                //if no projects, including no folder, create list, add projectId, update database
                if(pIdList == null || pIdList.size() == 0) {
                    temp.add(projectId);
                    myRefUser.setValue(temp);
                }
                //more than 1 project in. add projectId to pIdList and update in database
                else{
                    pIdList.add(projectId);
                    myRefUser.setValue(pIdList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

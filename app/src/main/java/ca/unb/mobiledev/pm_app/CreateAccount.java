package ca.unb.mobiledev.pm_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import DataModels.UserModel;
import Database.DBHelper;

public class CreateAccount extends AppCompatActivity {
    //references to buttons and controls on layout
    private Button createAcctBtn;
    private EditText et_fname;
    private EditText et_lname;
    private EditText et_email;
    private EditText et_pword;

    //Firebase
    FirebaseAuth auth;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);

        //instantiate all layout elements
        createAcctBtn = findViewById(R.id.btn_createacct);
        et_fname = findViewById(R.id.et_fname);
        et_lname = findViewById(R.id.et_lname);
        et_email = findViewById(R.id.et_email);
        et_pword = findViewById(R.id.et_pword);

        auth = FirebaseAuth.getInstance();


        //listeners for each layout element
        createAcctBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserModel user;
                String firstName_text = et_fname.getText().toString();
                String lastName_text = et_lname.getText().toString();
                String email_text = et_email.getText().toString();
                String password_text = et_pword.getText().toString();

                //Not all fields filled, show error message to user
                if(TextUtils.isEmpty(firstName_text)|| TextUtils.isEmpty(lastName_text)|| TextUtils.isEmpty(email_text) || TextUtils.isEmpty(password_text)) {
                    Toast.makeText(CreateAccount.this, "Error. Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                //All fields filled, send fields to database to make an account.
                else {
                    registerAccount(firstName_text, lastName_text, email_text, password_text);
                }
            }
        });

    }

    private void registerAccount(final String firstName, final String lastName, String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //User register successfully
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            myRef = FirebaseDatabase.getInstance().getReference("users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("firstName", firstName);
                            hashMap.put("lastName", lastName);
                            hashMap.put("profilePicURL", "default");

                            //Proceed to inside the app after user account is successfully registered.
                            myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(CreateAccount.this, AccountHomePage.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            });
                        }
                        //User register unsuccessfully
                        else{
                            Toast.makeText(CreateAccount.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}


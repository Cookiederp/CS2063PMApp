package ca.unb.mobiledev.pm_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    //layout elements
    private EditText et_email;
    private EditText et_pword;
    private Button signIn;
    private TextView register;

    //Firebase
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Checking for user state (keep users logged in)
        if (firebaseUser != null){
            Intent intent = new Intent(MainActivity.this, AccountHomePage.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_email = findViewById(R.id.et_email);
        et_pword = findViewById(R.id.et_pword);

        //firebase Auth
        auth = FirebaseAuth.getInstance();


        //create listener to send to account home page
        signIn = findViewById(R.id.but_sigin);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String email_text = et_email.getText().toString();
                String password_text = et_pword.getText().toString();

                //if a login field is empty
                if(TextUtils.isEmpty(email_text) || TextUtils.isEmpty(password_text)){
                    Toast.makeText(MainActivity.this, "Please Fill all the Fields", Toast.LENGTH_SHORT).show();
                }
                //all fields are filled, check if email and password is correct in the database, login user that it matches
                else{
                    auth.signInWithEmailAndPassword(email_text, password_text)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //login successful, go to homepage
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(MainActivity.this, AccountHomePage.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                    //login unsuccessful, wrong password/email
                                    else{
                                        Toast.makeText(MainActivity.this, "Login Failed: Wrong Password or Email!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        //create listener to send to create account form
        register = findViewById(R.id.tv_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, CreateAccount.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().setTitle("Sign In/Register");
    }
}
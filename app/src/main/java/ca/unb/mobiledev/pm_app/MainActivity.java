package ca.unb.mobiledev.pm_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //layout elements
    private EditText email;
    private EditText pword;
    private Button signIn;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create listener to send to account home page
        signIn = findViewById(R.id.but_sigin);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, AccountHomePage.class);
                startActivity(intent);
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
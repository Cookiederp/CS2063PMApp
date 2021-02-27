package ca.unb.mobiledev.pm_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        //listeners for each layout element
        createAcctBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserModel user;

                if(et_fname.getText().toString().matches("")|| et_lname.getText().toString().matches("")|| et_email.getText().toString().matches("") || et_pword.getText().toString().matches("")) {
                    Toast.makeText(CreateAccount.this, "Error. Please fill all fields", Toast.LENGTH_SHORT).show();
                    user = new UserModel(-1, "error", "error", "error", "error");
                }else {
                    user = new UserModel(-1, et_fname.getText().toString(), et_lname.getText().toString(), et_email.getText().toString(), et_pword.getText().toString());
                    Toast.makeText(CreateAccount.this, user.toString(), Toast.LENGTH_SHORT).show();
                }

                DBHelper dbHelper = new DBHelper(CreateAccount.this);
                boolean success = dbHelper.createUser(user);
                Toast.makeText(CreateAccount.this, "Success = " + success, Toast.LENGTH_SHORT).show();
            }
        });

    }
}


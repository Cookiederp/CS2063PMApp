package ca.unb.mobiledev.pm_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;

public class MemberPage extends AppCompatActivity {
    private TextView nameTV;
    private TextView roleTV;
    private ImageView img;
    private Button rmb;
    String projectID;
    private ImageView userProfilePicIV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberpage);

        Intent intent = getIntent();
        String userFName = intent.getStringExtra("firstName");
        String userLName = intent.getStringExtra("lastName");
        String userRole = intent.getStringExtra("userRole");
        String userId = intent.getStringExtra("userID");
        String picURL = intent.getStringExtra("userPicture");
        projectID = intent.getStringExtra("projID");

        nameTV = findViewById(R.id.userName_tv);
        roleTV = findViewById(R.id.userRole_tv);
        img = (ImageView)findViewById(R.id.profilePicture);
        rmb = findViewById(R.id.removeMemberBtn);
        rmb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeMember(userId, projectID);
            }
        });
      
        String userProfilePicURL = intent.getStringExtra("userPicture");

        nameTV = findViewById(R.id.userName_tv);
        roleTV = findViewById(R.id.userRole_tv);
        userProfilePicIV = findViewById(R.id.profilePicture);

        nameTV.setText(userFName + " " + userLName);
        roleTV.setText(userRole);

        //might use later
        //new GetImageFromURL(img).execute(picURL);

        if(userProfilePicURL.equals("default")){
            userProfilePicIV.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            Glide.with(MemberPage.this).load(userProfilePicURL).into(userProfilePicIV);
        }
    }
    //might use later
    /*
    public class GetImageFromURL extends AsyncTask<String, Void, Bitmap> {
        ImageView imgView;
        Bitmap bmap;
        public GetImageFromURL(ImageView imgV){
            this.imgView = imgV;
        }
        @Override
        protected Bitmap doInBackground(String... url){
            String urldisplay = url[0];
            bmap = null;
            try{
                InputStream srt = new java.net.URL(urldisplay).openStream();
                bmap = BitmapFactory.decodeStream(srt);
            }catch(Exception e){
                e.printStackTrace();
            }
            return bmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);
            imgView.setImageBitmap(bitmap);
        }
    }
    private void removeMember(String userId, String projectID){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectID).child("Members");
        ref.child(userId).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(MemberPage.this,"User Removed", Toast.LENGTH_LONG).show();
                Intent ret = new Intent();
                ret.putExtra("removed", true);
                setResult(1,ret);
                MemberPage.this.finish();
            }
        });

    }

}

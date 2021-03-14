package ca.unb.mobiledev.pm_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;

public class MemberPage extends AppCompatActivity {
    private TextView nameTV;
    private TextView roleTV;
    private ImageView img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberpage);

        Intent intent = getIntent();
        String userFName = intent.getStringExtra("firstName");
        String userLName = intent.getStringExtra("lastName");
        String userRole = intent.getStringExtra("userRole");
        String picURL = intent.getStringExtra("userPicture");

        nameTV = findViewById(R.id.userName_tv);
        roleTV = findViewById(R.id.userRole_tv);
        img = (ImageView)findViewById(R.id.profilePicture);

        nameTV.setText(userFName + " " + userLName);
        roleTV.setText(userRole);
        new GetImageFromURL(img).execute(picURL);
    }
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
}

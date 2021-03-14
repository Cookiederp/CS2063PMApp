package ca.unb.mobiledev.pm_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import ca.unb.mobiledev.pm_app.Model.Users;

public class AddMember extends AppCompatActivity {

    private EditText userIn;
    private RecyclerView recyclerView;
    private String projID;
    private String projName;
    DatabaseReference myRef;
    DatabaseReference userRef;

    private ArrayList<Users> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmember);
        Intent trigger = getIntent();
        projID = trigger.getStringExtra("projectID");
        projName = trigger.getStringExtra("projectname");
        userIn = findViewById(R.id.search_user);
        recyclerView = (RecyclerView)findViewById(R.id.rv_searchmembers);

        userIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void PushMember(String userID, String projectID){
        HashMap<String, Object > updateIDs = new HashMap<>();
        myRef = FirebaseDatabase.getInstance().getReference("Projects").child(projectID).child("Members").child(userID);
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        updateIDs.put("id", userID);
        updateIDs.put("role", "member");
        myRef.updateChildren(updateIDs);
        userRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                user.addProjectId(projectID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Toast.makeText(this,"User Added", Toast.LENGTH_LONG).show();
    }
    private void SearchUsers(String user){
       userList = new ArrayList<>();
        FirebaseUser dbUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("firstName")
                .startAt(user)
                .endAt(user+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Users user = child.getValue(Users.class);

                    if (!user.getId().equals(dbUser.getUid())) {
                        userList.add(user);
                    }
                }
                MyAdapter adapter = new MyAdapter(userList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

        private Context context;
        private ArrayList<Users> userList;

        public MyAdapter(ArrayList<Users> userList){
            this.userList = userList;
        }

        @NonNull
        @Override
        public MyAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_member, parent, false);
            return new MyAdapter.MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            Users user = userList.get(position);

            //can get any data from a project here
            String userId = user.getId();
            //To be added later - > String projectIcon = user.getIcon();
            String userFirstName = user.getFirstName();
            String userLastName = user.getLastName();
            String userPic = user.getProfilePicURL();

            //display name on the card
            StringBuilder sb = new StringBuilder();
            sb.append(userFirstName);
            sb.append(" ");
            sb.append(userLastName);
            holder.userNameTV.setText(sb);
            new GetImageFromURL(holder.userIconImageView).execute(userPic);

            //when a member is clicked, show more detail and options (TO BE ADDED)
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    PushMember(userId, projID);
                }

            });

        }


        @Override
        public int getItemCount() {
            return userList.size();
        }

        class MyHolder extends RecyclerView.ViewHolder{

            private ImageView userIconImageView;
            private TextView userNameTV;


            public MyHolder(@NonNull View itemView){
                super(itemView);

                userIconImageView = itemView.findViewById(R.id.icon_user);
                userNameTV = itemView.findViewById(R.id.tv_addMemname);
            }


        }

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

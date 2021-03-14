package ca.unb.mobiledev.pm_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import ca.unb.mobiledev.pm_app.Model.Users;

public class MembersList extends AppCompatActivity {


    private RecyclerView recyclerView ;


    //get logged in user
    private FirebaseAuth auth;
    DatabaseReference myRef;
    DatabaseReference userRef;

    private ArrayList<Users> membersList;
    private Button addMembersBtn;

    public MembersList(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberslist);

        Intent intent = getIntent();
        String projectId = intent.getStringExtra("projectId");
        String projectName = intent.getStringExtra("projectName");

        //View view = inflater.inflate(R.layout.fragment_projects, container, false);

        recyclerView = (RecyclerView) findViewById(R.id.rv_members);

        auth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("Projects").child(projectId).child("Members");
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        getUsersInProject();
        FirebaseUser dbUser = FirebaseAuth.getInstance().getCurrentUser();

        addMembersBtn = findViewById(R.id.addmember_button);
        addMembersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MembersList.this, AddMember.class);
                intent.putExtra("projectID", projectId);
                intent.putExtra("projectname", projectName);
                startActivity(intent);
            }
        });
    }


    private void getUsersInProject(){
        membersList = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                membersList.clear();


                for (DataSnapshot child : snapshot.getChildren()) {
                    String userId = child.getKey();
                    String userRole = child.child("role").getValue(String.class);

                    userRef.child(userId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            MyAdapter myAdapter;
                            Users user = snapshot.getValue(Users.class);
                            user.setRole(userRole);
                            membersList.add(user);
                            myAdapter = new MyAdapter(membersList);
                            recyclerView.setAdapter(myAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

        private Context context;
        private ArrayList<Users> membersList;

        public MyAdapter(ArrayList<Users> membersList){
            this.membersList = membersList;
        }

        @NonNull
        @Override
        public MyAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_members_list, parent, false);
            return new MyAdapter.MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyHolder holder, int position) {


            Users user = membersList.get(position);

            //can get any data from a project here
            String userId = user.getId();
            //To be added later - > String projectIcon = user.getIcon();
            String userFirstName = user.getFirstName();
            String userLastName = user.getLastName();
            String userRole = user.getRole();
          // alex
            String userPic = user.getProfilePicURL();
          // dev
            String userProfilePicURL = user.getProfilePicURL();

            //display name on the card
            StringBuilder sb = new StringBuilder();
            sb.append(userFirstName);
            sb.append(" ");
            sb.append(userLastName);
            holder.userNameTV.setText(sb);

            //display user role on the card
            sb = new StringBuilder();
            sb.append("Role: ");
            sb.append(userRole);
            holder.userRoleTV.setText(sb);

            if(userProfilePicURL.equals("default")){
                holder.userProfilePicIV.setImageResource(R.mipmap.ic_launcher);
            }
            else{
                Glide.with(MembersList.this).load(userProfilePicURL).into(holder.userProfilePicIV);
            }

            //try{
            //something with picasso to cache icon in the future.
            //}
            //catch (){
            //more here
            //}

            //when a member is clicked, show more detail and options (TO BE ADDED)
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(MembersList.this, MemberPage.class);
                    intent.putExtra("firstName", userFirstName);
                    intent.putExtra("lastName", userLastName);
                    intent.putExtra("userRole", userRole);
                    intent.putExtra("userPicture", user.getProfilePicURL());
                    startActivity(intent);
                }

            });

        }

        @Override
        public int getItemCount() {
            return membersList.size();
        }

        class MyHolder extends RecyclerView.ViewHolder{

            private ImageView userProfilePicIV;
            private TextView userNameTV;
            private TextView userRoleTV;

            public MyHolder(@NonNull View itemView){
                super(itemView);


                userNameTV = itemView.findViewById(R.id.tv_name);
                userRoleTV = itemView.findViewById(R.id.tv_role);
                userProfilePicIV = itemView.findViewById(R.id.icon_user);

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


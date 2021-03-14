package ca.unb.mobiledev.pm_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import ca.unb.mobiledev.pm_app.Model.Projects;

public class ProjectsList extends AppCompatActivity {

    private RecyclerView projectsRecyclerView ;


    //get logged in user
    private FirebaseAuth auth;
    DatabaseReference projectsRef;

    private ArrayList<Projects> projectsList;

    public ProjectsList(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectslist);

        //View view = inflater.inflate(R.layout.fragment_projects, container, false);

        projectsRecyclerView = (RecyclerView) findViewById(R.id.rv_projects);

        auth = FirebaseAuth.getInstance();
        projectsRef = FirebaseDatabase.getInstance().getReference("Projects");
        getUserProjectsList();
    }


    //from the logged in userId, find all the projects containing the logged in userId in their Members ids list.
    private void getUserProjectsList(){
        projectsList = new ArrayList<>();

        projectsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                projectsList.clear();
                AdapterProjectList adapterProjectList;
                //search project for current signed in User, if userId is found in a project, add to view
                for (DataSnapshot child : snapshot.getChildren()) {
                    if(child.child("Members").child(auth.getUid()).exists()){
                        Projects project = child.getValue(Projects.class);
                        projectsList.add(project);
                    }
                }

                Log.d("123123", "onDataChange: " + projectsList.size());
                adapterProjectList = new AdapterProjectList(projectsList);
                projectsRecyclerView.setAdapter(adapterProjectList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public class AdapterProjectList extends RecyclerView.Adapter<AdapterProjectList.HolderProjectList> {

        private Context context;
        private ArrayList<Projects> projectsList;

        public AdapterProjectList(ArrayList<Projects> projectList){
            this.projectsList = projectList;
        }

        @NonNull
        @Override
        public AdapterProjectList.HolderProjectList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_projects_list, parent, false);
            return new AdapterProjectList.HolderProjectList(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterProjectList.HolderProjectList holder, int position) {


            Projects project = projectsList.get(position);

            //can get any data from a project here
            String projectId = project.getId();
            //To be added later - > String projectIcon = project.getIcon();
            String projectName = project.getProjectName();
            String iconPicURL = project.getIconPicURL();

            holder.projectTitleText.setText(projectName);

            if(iconPicURL == null || iconPicURL.equals("default")){
                holder.projectIconIV.setImageResource(R.mipmap.ic_launcher);
            }
            else{
                Glide.with(ProjectsList.this).load(iconPicURL).into(holder.projectIconIV);
            }

            //try{
            //something with picasso to cache icon in the future.
            //}
            //catch (){
            //more here
            //}

            //when a project is clicked, go to that project's page
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(ProjectsList.this, ProjectPage.class);
                    intent.putExtra("projectId", projectId);
                    intent.putExtra("projectName", projectName);
                    intent.putExtra("projectIconURL", iconPicURL);
                    startActivity(intent);
                }

            });

        }

        @Override
        public int getItemCount() {
            return projectsList.size();
        }

        class HolderProjectList extends RecyclerView.ViewHolder{

            private ImageView projectIconIV;
            private TextView projectTitleText;

            public HolderProjectList(@NonNull View itemView){
                super(itemView);

                projectIconIV = itemView.findViewById(R.id.icon_project);
                projectTitleText = itemView.findViewById(R.id.tv_title);
            }


        }

    }
}




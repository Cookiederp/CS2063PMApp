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
import ca.unb.mobiledev.pm_app.Model.Tasks;
import ca.unb.mobiledev.pm_app.Model.Users;

public class TasksList extends AppCompatActivity {

    private RecyclerView tasksRecyclerView ;


    //get logged in user
    private FirebaseAuth auth;
    DatabaseReference tasksRef;

    //private ArrayList<Projects> projectsList;
    //project specific data
    private ArrayList<Tasks> tasksList;

    //empty constructor
    public TasksList(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskslist);

        //View view = inflater.inflate(R.layout.fragment_projects, container, false);

        tasksRecyclerView = (RecyclerView) findViewById(R.id.rv_tasks);

        auth = FirebaseAuth.getInstance();
        tasksRef = FirebaseDatabase.getInstance().getReference("Tasks");
        getProjectsTaskList();
    }


    //from the project id get all tasks created for that project
    private void getProjectsTaskList(){
        tasksList = new ArrayList<>();

        tasksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tasksList.size();
                AdapterProjectList adapterProjectList;
                //search project for current signed in User, if userId is found in a project, add to view
                for (DataSnapshot child : snapshot.getChildren()) {
                    if(child.child("Tasks").child(auth.getUid()).exists()){
                        Tasks project = child.getValue(Tasks.class);
                        tasksList.add(project);
                    }
                }

                //Log.d("123123", "onDataChange: " + tasksList.size());
                adapterProjectList = new AdapterProjectList(tasksList);
                tasksRecyclerView.setAdapter(adapterProjectList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public class AdapterProjectList extends RecyclerView.Adapter<AdapterProjectList.HolderProjectList> {

        private Context context;
        private ArrayList<Tasks> tasksList;

        public AdapterProjectList(ArrayList<Tasks> taskList){
            this.tasksList = taskList;
        }

        @NonNull
        @Override
        public AdapterProjectList.HolderProjectList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tasks_list, parent, false);
            return new AdapterProjectList.HolderProjectList(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterProjectList.HolderProjectList holder, int position) {


            Tasks task = tasksList.get(position);

            //can get any data from a project here
            String taskId = task.getId();
            String taskName = task.getTitle();
            String taskDueDate = task.getDeadline();

            holder.taskTitleText.setText(taskName);

            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(TasksList.this, TasksPage.class);
                    intent.putExtra("taskId", taskId);
                    intent.putExtra("taskName", taskName);
                    intent.putExtra("taskDueDate", taskDueDate);
                    startActivity(intent);
                    finish();
                }

            });

        }

        @Override
        public int getItemCount() {
            return tasksList.size();
        }

        class HolderProjectList extends RecyclerView.ViewHolder{

            //private ImageView projectIconImageView;
            private TextView taskTitleText;

            public HolderProjectList(@NonNull View itemView){
                super(itemView);

                //projectIconImageView = itemView.findViewById(R.id.icon_project);
                taskTitleText = itemView.findViewById(R.id.tv_tasktitle);
            }


        }

    }
}




package ca.unb.mobiledev.pm_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import ca.unb.mobiledev.pm_app.Model.Tasks;

public class TasksPage extends AppCompatActivity {


    private RecyclerView recyclerView ;
    private Button createTaskButton;

    private String projectId;

    //get logged in user
    private FirebaseAuth auth;
    DatabaseReference myRef;
    DatabaseReference userRef;

    private ArrayList<Tasks> tasksList;

    public TasksPage(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskspage);

        Intent intent = getIntent();
        projectId = intent.getStringExtra("projectId");
        //String projectName = intent.getStringExtra("projectName");

        //View view = inflater.inflate(R.layout.fragment_projects, container, false);

        recyclerView = findViewById(R.id.rv_tasks);
        createTaskButton = findViewById(R.id.btn_createtask);

        auth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("Tasks").child(projectId);

        getTasksInProject();

        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TasksPage.this, CreateTask.class);
                intent.putExtra("projectId", projectId);
                startActivity(intent);
            }
        });


    }


    private void getTasksInProject(){
        tasksList = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tasksList.clear();
                MyAdapter myAdapter;
                //search project for current signed in User, if userId is found in a project, add to view
                for (DataSnapshot child : snapshot.getChildren()) {
                    Tasks tasks = child.getValue(Tasks.class);
                    tasksList.add(tasks);
                }
                myAdapter = new MyAdapter(tasksList);
                recyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

        private Context context;
        private ArrayList<Tasks> tasksList;

        public MyAdapter(ArrayList<Tasks> tasksList){
            this.tasksList = tasksList;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tasks_list, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyHolder holder, int position) {

            Tasks task = tasksList.get(position);

            //can get any data from a project here
            String taskId = task.getId();
            String taskName = task.getTitle();
            String taskDesc = task.getDescription();
            //To be added later - > String projectIcon = user.getIcon();

            //display name on the card
            holder.taskNameTextView.setText(taskName);

            //display user role on the card

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
                    Intent intent = new Intent(TasksPage.this, TaskDetail.class);
                    intent.putExtra("projectId", projectId);
                    intent.putExtra("taskId", taskId);
                    intent.putExtra("taskName", taskName);
                    intent.putExtra("taskDescription", taskDesc);
                    startActivity(intent);
                }

            });

        }

        @Override
        public int getItemCount() {
            return tasksList.size();
        }

        class MyHolder extends RecyclerView.ViewHolder{

            private TextView taskNameTextView;

            public MyHolder(@NonNull View itemView){
                super(itemView);
                taskNameTextView = itemView.findViewById(R.id.tv_tasktitle);
            }


        }

    }

}


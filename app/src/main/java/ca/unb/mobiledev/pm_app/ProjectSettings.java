package ca.unb.mobiledev.pm_app;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProjectSettings extends AppCompatActivity {


    private Button confirmNameButton;
    private Button changeIconButton;
    private EditText projectNameEditText;
    private ImageView projectIconIV;

    private String projectId;
    private String projectIconURL;

    DatabaseReference reference;
    StorageReference storageReference;
    StorageReference photoRef;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectsettings);

        Intent intent = getIntent();
        projectId = intent.getStringExtra("projectId");
        String projectName = intent.getStringExtra("projectName");
        projectIconURL = intent.getStringExtra("projectIconURL");

        confirmNameButton = findViewById(R.id.btn_confirmprojectname);
        changeIconButton = findViewById(R.id.btn_changeprojecticon);
        projectNameEditText = findViewById(R.id.et_changeprojectname);
        projectIconIV = findViewById(R.id.icon_projectsettings);

        projectNameEditText.setText(projectName);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        if(projectIconURL == null || projectIconURL.equals("default")){
            projectIconIV.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            Glide.with(ProjectSettings.this).load(projectIconURL).into(projectIconIV);
        }


        confirmNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference("Projects").child(projectId);
                HashMap<String, Object> map = new HashMap<>();
                map.put("projectName", projectNameEditText.getText().toString());
                reference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ProjectSettings.this, "Name Successfully Changed.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(ProjectSettings.this, "Error Changing Name.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        changeIconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });



    }


    private void SelectImage(){

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, IMAGE_REQUEST);

    }

    private String getFileExtension(Uri uri){

        ContentResolver contentResolver = ProjectSettings.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void UploadMyImage(){
        final ProgressDialog progressDialog = new ProgressDialog(ProjectSettings.this);
        progressDialog.setMessage("Uploading");
        progressDialog.show();

        if(imageUri != null){
            final StorageReference fileReference = storageReference.child(projectId + "." + getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){

                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("Projects").child(projectId);

                        HashMap<String, Object> map = new HashMap<>();

                        map.put("iconPicURL", mUri);
                        reference.updateChildren(map);

                        progressDialog.dismiss();

                        Glide.with(ProjectSettings.this).load(mUri).into(projectIconIV);
                    }
                    else{
                        Toast.makeText(ProjectSettings.this, "Changes failed to save.", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProjectSettings.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
        else{
            Toast.makeText(ProjectSettings.this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }

    }

    private  void deleteOldIcon(){

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){

            imageUri = data.getData();

            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(ProjectSettings.this, "Uploading...", Toast.LENGTH_SHORT).show();
            }
            else{
                UploadMyImage();
            }
        }
    }

}

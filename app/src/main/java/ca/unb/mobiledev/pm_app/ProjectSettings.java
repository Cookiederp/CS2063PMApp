package ca.unb.mobiledev.pm_app;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;

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
        getSupportActionBar().setTitle("Project Settings");


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

                if (TextUtils.isEmpty(projectNameEditText.getText())) {
                    Toast.makeText(ProjectSettings.this, "Error. Please fill all fields.", Toast.LENGTH_SHORT).show();
                }
                else{
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

    private void UploadMyImage() throws IOException {
        final ProgressDialog progressDialog = new ProgressDialog(ProjectSettings.this);
        progressDialog.setMessage("Uploading");
        progressDialog.show();

        if(imageUri != null){
            final StorageReference fileReference = storageReference.child(projectId + "." + getFileExtension(imageUri));



            //resize, rotating, compressing image before it is sent to firebase storage
            Bitmap imageBitmap = decodeUri(ProjectSettings.this, imageUri, 256);
            ExifInterface ei = new ExifInterface( getRealPathFromURI(ProjectSettings.this, imageUri));
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            Bitmap rotatedBitmap = null;
            switch(orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(imageBitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(imageBitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(imageBitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = imageBitmap;
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] data = baos.toByteArray();
            uploadTask = fileReference.putBytes(data);
            //end

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

    public Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while(true) {
            if(width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize){
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }

    public String getRealPathFromURI(Context context, Uri uri) {
        Uri queryUri = MediaStore.Files.getContentUri("external");
        String columnData = MediaStore.Files.FileColumns.DATA;
        String columnSize = MediaStore.Files.FileColumns.SIZE;

        String[] projectionData = {MediaStore.Files.FileColumns.DATA};

        String name = null;
        String size = null;

        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);

            cursor.moveToFirst();

            name = cursor.getString(nameIndex);
            size = cursor.getString(sizeIndex);

            cursor.close();
        }

        String imagePath = "";
        if ((name != null) && (size != null)) {
            String selectionNS = columnData + " LIKE '%" + name + "' AND " + columnSize + "='" + size + "'";

            Cursor cursorLike = context.getContentResolver().query(queryUri, projectionData, selectionNS, null, null);

            if ((cursorLike != null) && (cursorLike.getCount() > 0)) {
                cursorLike.moveToFirst();
                int indexData = cursorLike.getColumnIndex(columnData);
                if (cursorLike.getString(indexData) != null) {
                    imagePath = cursorLike.getString(indexData);
                }
                cursorLike.close();
            }
        }

        return imagePath;
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
                try {
                    UploadMyImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

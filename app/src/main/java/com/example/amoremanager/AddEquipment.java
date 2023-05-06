package com.example.amoremanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddEquipment extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE = 101;
    private ImageView imageViewAdd;
    private EditText txtImageName;
    private TextView textViewProgress;
    private Button btnUpload;
    private ProgressBar progressBar;
    private String currentUser;

    Uri imageUrl;
    boolean isImageAdded = false;

    FirebaseAuth firebaseAuth;
    DatabaseReference DataRef;
    StorageReference StorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_equipment);

        imageViewAdd = findViewById(R.id.imageViewAdd);
        txtImageName = findViewById(R.id.txtImageName);
        btnUpload = findViewById(R.id.btnUpload);
        textViewProgress = findViewById(R.id.textViewProgress);
        progressBar = findViewById(R.id.progressBar_img);

        textViewProgress.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        //currentUser = firebaseAuth.getCurrentUser().getUid();
        DataRef = FirebaseDatabase.getInstance().getReference().child("Equipment");
        StorageRef = FirebaseStorage.getInstance().getReference().child("EquipmentImage");

        imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent,REQUEST_CODE_IMAGE);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String imageName = txtImageName.getText().toString();
                //check if text field empty
                if(TextUtils.isEmpty(imageName)){
                    txtImageName.setError("Name is required!");
                    return;
                }
                if(isImageAdded!=false && imageName!=null){
                    uploadImage(imageName);
                }
            }
        });
    }

    private void uploadImage(final String imageName) {
        textViewProgress.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        final String key = DataRef.push().getKey();
        //put file into the file storage
        StorageRef.child(key +".jpg").putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageRef.child(key+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //assign data to corresponding data fields
                        HashMap hashMap = new HashMap();
                        //hashMap.put("UserId",currentUser);
                        hashMap.put("EquipmentName",imageName);
                        hashMap.put("ImageUrl",uri.toString());

                        //insert data into the database
                        DataRef.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddEquipment.this, "Data successfully uploaded.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddEquipment.this, InventoryManager.class));
                                finish();
                            }
                        });
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (snapshot.getBytesTransferred()*100)/snapshot.getTotalByteCount();
                progressBar.setProgress((int)progress);
                textViewProgress.setText(progress+"%");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_IMAGE && data!=null){
            imageUrl = data.getData();
            isImageAdded = true;
            imageViewAdd.setImageURI(imageUrl);
        }
    }
}
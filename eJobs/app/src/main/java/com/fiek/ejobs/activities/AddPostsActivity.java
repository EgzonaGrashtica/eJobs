package com.fiek.ejobs.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fiek.ejobs.R;
import com.fiek.ejobs.utils.UploadPhoto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddPostsActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    private Bitmap bitmap;
    EditText etAddCompanyName,etAddLocation,etAddFreeSpots,etAddDescription,etAddPosition,etAddExpirationDate,etInvisible;
    ImageView ivAddPhoto;
    Button btnAddSave;
    private CollectionReference mDatabase;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_posts);

        db= FirebaseFirestore.getInstance();
        etAddCompanyName=findViewById(R.id.etAddCompanyName);
        etAddLocation=findViewById(R.id.etAddLocation);
        etAddFreeSpots=findViewById(R.id.etAddFreeSpots);
        etAddDescription=findViewById(R.id.etAddDescription);
        etAddPosition=findViewById(R.id.etAddPosition);
        etAddExpirationDate=findViewById(R.id.etAddExpirationDate);
        etInvisible=findViewById(R.id.etInvisible);
        ivAddPhoto=findViewById(R.id.ivAddPhoto);
        btnAddSave=findViewById(R.id.btnAddSave);
        ivAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProfilePic(v);

            }
        });
        etAddExpirationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddPostsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //formatimi i ores (+1 per arsye se DatePickerDialog e shfaq muajin per 1 ma pak !?)
                                if(dayOfMonth <10 && monthOfYear<9) {
                                    etAddExpirationDate.setText("0" + dayOfMonth + "/0" + (monthOfYear + 1) + "/" + year);
                                }else if(dayOfMonth<10){
                                    etAddExpirationDate.setText("0"+dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                }else if(monthOfYear<9){
                                    etAddExpirationDate.setText(dayOfMonth + "/0" + (monthOfYear + 1) + "/" + year);
                                }else{
                                    etAddExpirationDate.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btnAddSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddPostFunc();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth mAuth) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null){
                    Intent intent=new Intent(AddPostsActivity.this, LogInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else {
                }
            }
        };
        mAuth.addAuthStateListener(authStateListener);

    }
    private void btnAddPostFunc(){
        //Uri uri=null;
        String companyName = etAddCompanyName.getText().toString().trim();
        String location=etAddLocation.getText().toString().trim();
        String freeSpots=etAddFreeSpots.getText().toString().trim();
        String description=etAddDescription.getText().toString().trim();
        String photoPath=etInvisible.getText().toString();
        String position=etAddPosition.getText().toString().trim();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date expirationDate= null;
        try {
            expirationDate = dateFormat.parse(etAddExpirationDate.getText().toString().trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        addNewPostToDatabase(companyName,location,freeSpots,description,photoPath,position,expirationDate);

    }

    private void addNewPostToDatabase(String companyName,String location,String freeSpots,String description,String photoPath,
                                      String position,Date expirationDate){
        Map<String, Object> job = new HashMap<>();
        job.put("companyName", companyName);
        job.put("location", location);
        job.put("freeSpots", freeSpots);
        job.put("description", description);
        job.put("photoPath",photoPath);
        job.put("position", position);
        job.put("expirationDate", new Timestamp(expirationDate));

        db.collection("Jobs")
                .add(job)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddPostsActivity.this,"U postua",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddPostsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });



    }
    private void addProfilePic(View view){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE){
            switch (resultCode){
                case RESULT_OK:
                    Uri imageUri = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ivAddPhoto.setImageBitmap(bitmap);

              new UploadPhoto(AddPostsActivity.this).handleUpload(bitmap,etInvisible);

            }
        }
    }
    public void tvAddPostLogOutFunc(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(R.string.alertLogOut)
                .setPositiveButton(R.string.tvLogOut, new DialogInterface.OnClickListener()                 {

                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                    }
                }).setNegativeButton(R.string.btnCancel, null);

        AlertDialog alert1 = alert.create();
        alert1.show();
    }
}

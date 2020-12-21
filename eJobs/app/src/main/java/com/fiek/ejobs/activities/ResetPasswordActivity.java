package com.fiek.ejobs.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fiek.ejobs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    Button btnResetPassword;
    ImageButton btnBack;
    EditText etResetPwEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        btnResetPassword=(Button) findViewById(R.id.btnResetPassord);
        etResetPwEmail=(EditText) findViewById(R.id.etResetPwEmail);
        btnBack=(ImageButton) findViewById(R.id.btnBack);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnResetPassword.setEnabled(false);
                sendEmailForReset(etResetPwEmail.getText().toString());
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void sendEmailForReset(String email){
        if(!email.isEmpty()){
            FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        onBackPressed();
                        Toast.makeText(ResetPasswordActivity.this,R.string.toastEmailSend,Toast.LENGTH_LONG).show();

                    }
                    else {
                        Toast.makeText(ResetPasswordActivity.this,R.string.toastEmailNotExist,Toast.LENGTH_LONG).show();
                    }
                    btnResetPassword.setEnabled(true);
                }
            });
        }
        else {
            Toast.makeText(ResetPasswordActivity.this,R.string.toastEnterEmail,Toast.LENGTH_SHORT).show();
            btnResetPassword.setEnabled(true);
        }
    }
}

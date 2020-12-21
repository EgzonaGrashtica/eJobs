package com.fiek.ejobs.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fiek.ejobs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {
    String email;
    String password;
    EditText etEmail,etPassword;
    TextView tvForgotPw,tvSignUp;
    ImageButton btnLogIn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        btnLogIn=findViewById(R.id.btnLogIn);
        tvForgotPw=findViewById(R.id.tvForgotPw);
        tvSignUp=findViewById(R.id.tvSignUp);

        mAuth = FirebaseAuth.getInstance();

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogInFunc(v);
            }
        });
        tvForgotPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, ResetPasswordActivity.class));
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null){
            startActivity(new Intent(LogInActivity.this, MainActivity.class));
            finish();
        }


    }
    public void btnLogInFunc(View v){
        email= etEmail.getText().toString().trim();
        password= etPassword.getText().toString().trim();
        if(validateLogInData()){
            btnLogIn.setEnabled(false);
            tvSignUp.setClickable(false);
            tvForgotPw.setClickable(false);
            etEmail.setEnabled(false);
            etPassword.setEnabled(false);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Debug Info", "signInWithEmail:success");

                                btnLogIn.setEnabled(true);
                                tvSignUp.setClickable(true);
                                tvForgotPw.setClickable(true);
                                etEmail.setEnabled(true);
                                etPassword.setEnabled(true);
                                Intent intent;
                                if(email.equals("admin@hotmail.com")){
                                    intent=new Intent(LogInActivity.this, AddPostsActivity.class);
                                }
                                else{
                                intent=new Intent(LogInActivity.this, MainActivity.class);
                                }
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                btnLogIn.setEnabled(true);
                                tvSignUp.setClickable(true);
                                tvForgotPw.setClickable(true);
                                etEmail.setEnabled(true);
                                etPassword.setEnabled(true);
                                // If sign in fails, display a message to the user.
                                Log.w("Debug Info", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LogInActivity.this,R.string.toastAuthFailed,
                                        Toast.LENGTH_SHORT).show();
                                etPassword.getText().clear();
                            }


                        }
                    });
        }


    }
    public void tvSignUpFunc(View v){
        startActivity(new Intent(LogInActivity.this, SignUpActivity.class));

    }
    public boolean validateLogInData(){

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.validEmail));
            etEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getString(R.string.emptyEmail));
            etEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError(getString(R.string.emptyPassword));
            etPassword.requestFocus();
            return false;
        }
        if (password.length() < 6) {
            etPassword.setError(getString(R.string.shortPassword));
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

}

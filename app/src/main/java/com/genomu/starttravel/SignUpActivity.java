package com.genomu.starttravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();
    private FirebaseAuth auth;
    private EditText edx_email;
    private EditText edx_password;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        findViews();
        edx_email.setText(email);
        edx_password.setText(password);
    }

    public void signUpAcc(View view){
        email = edx_email.getText().toString();
        password = edx_password.getText().toString();
//        Log.d(TAG, "signUpAcc: "+email+"/"+password);
//        Toast.makeText(this,email+"/"+password,Toast.LENGTH_LONG).show();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String message =task.isSuccessful()?"Successful":"Failed";
                Log.d(TAG, "SignUp"+message);
                new AlertDialog.Builder(SignUpActivity.this)
                        .setMessage(message)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                goLogin();
                            }
                        })
                        .show();
            }
        });
    }

    private void goLogin(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("nav",R.id.navigation_users);
        startActivity(intent);
    }

    private void findViews(){
        edx_email = findViewById(R.id.email_sign_up);
        edx_password = findViewById(R.id.password_sign_up);
    }
}

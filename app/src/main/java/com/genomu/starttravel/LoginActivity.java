package com.genomu.starttravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    final String TAG = LoginActivity.class.getSimpleName();
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;
    private String userUID;

    private Button btn;
    private EditText edx_email;
    private EditText edx_password;
    private TextView tv_reg_hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar ab = getSupportActionBar();
        ab.hide();
        findViews();
        setupAuth();
        tv_reg_hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {
        this.signUp("","");
    }

    private void signUp(String email,String password) {
        Intent intent = new Intent(this,SignUpActivity.class);
        intent.putExtra("email",email);
        intent.putExtra("password",password);
        startActivity(intent);
    }

    private void findViews(){
        btn = findViewById(R.id.button_login);
        edx_email = findViewById(R.id.email_login);
        edx_password = findViewById(R.id.password_login);
        tv_reg_hint = findViewById(R.id.reg_hint_login);
    }

    public void login(View view){
        final String email = edx_email.getText().toString();
        final String password = edx_password.getText().toString();
        Log.d(TAG+"input_login,", "e-mail:"+email+"password:"+password);
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    Log.d(TAG+"onComplete", "login failed");
                    loginFailed(email, password);
                }else{
                    Log.d(TAG+"onComplete", "login succeeded");
                    loginSucceeded();
                }
            }
        });
    }

    private void loginSucceeded() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("nav",R.id.navigation_users);
        startActivity(intent);
    }

    private void loginFailed(final String email, final String password) {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Login failed")
                .setMessage("Do you want to sign up an account with current e-mail and password?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signUp(email,password);
                    }
                })
                .setNegativeButton("cancel",null)
                .show();
    }

    private void setupAuth() {
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null) {
                    Log.d(TAG+"onAuthStateChanged", "logged in"+ user.getUid());
                    userUID =  user.getUid();
                }else{
                    Log.d(TAG+"onAuthStateChanged", "logged out");
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authStateListener);
    }
}

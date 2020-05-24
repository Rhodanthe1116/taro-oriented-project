package com.genomu.starttravel.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.genomu.starttravel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.genomu.starttravel.activity.SignUpActivity.FUNC_SUP;

public class LoginActivity extends AppCompatActivity {

    public static final int FUNC_LIN = 3;
    final String TAG = LoginActivity.class.getSimpleName();
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;
    private String userUID;

    private Button btn;
    private EditText edx_email;
    private EditText edx_password;
    private TextView tv_reg_hint;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FUNC_SUP){
            if(resultCode==RESULT_OK){
                setResult(RESULT_OK);
                Log.d(TAG, "onSignUpResult: ");
                finish();
            }else if(resultCode==RESULT_CANCELED){
                Toast.makeText(this,"註冊失敗",Toast.LENGTH_LONG).show();
            }
        }
    }

    private TextWatcher loginWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String emailInput = edx_email.getText().toString().trim();
            String passwordInput = edx_password.getText().toString().trim();
            btn.setEnabled(!emailInput.isEmpty()&&!passwordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
        setupAuth();
        edx_email.addTextChangedListener(loginWatcher);
        edx_password.addTextChangedListener(loginWatcher);
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
        startActivityForResult(intent,FUNC_SUP);
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
        Intent intent = getIntent();
        setResult(RESULT_OK,intent);
        finish();
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

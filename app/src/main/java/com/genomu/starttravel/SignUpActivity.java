package com.genomu.starttravel;

import androidx.annotation.NonNull;
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

import com.genomu.starttravel.util.DatabaseInvoker;
import com.genomu.starttravel.util.HanWen;
import com.genomu.starttravel.util.SetUserCommand;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    public static final int FUNC_SUP = 4;

    private static final String TAG = SignUpActivity.class.getSimpleName();
    private FirebaseAuth auth;
    private EditText edx_email;
    private EditText edx_password;
    private EditText edx_name;
    private String email;
    private String password;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ActionBar ab = getSupportActionBar();
        ab.hide();
        auth = FirebaseAuth.getInstance();
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        findViews();
        edx_email.setText(email);
        edx_password.setText(password);
        edx_password.addTextChangedListener(signUpWatcher);
        edx_email.addTextChangedListener(signUpWatcher);
        edx_name.addTextChangedListener(signUpWatcher);

    }
    private boolean isSuccessful = false;
    private String UID = "b07505019";
    public void signUpAcc(View view){
        email = edx_email.getText().toString();
        password = edx_password.getText().toString();
//        Log.d(TAG, "signUpAcc: "+email+"/"+password);
//        Toast.makeText(this,email+"/"+password,Toast.LENGTH_LONG).show();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                String message = task.isSuccessful()?"Successful":"Failed";
                isSuccessful = task.isSuccessful();
                if(task.isSuccessful()){
                    Log.d(TAG, "create status:"+ UserAuth.getInstance().getStatus());
                    DatabaseInvoker invoker = new DatabaseInvoker();
                    invoker.addCommand(new SetUserCommand(new HanWen(),auth.getUid(),edx_name.getText().toString(),new ArrayList<Order>()));
                    UID = auth.getUid();
                    invoker.assignCommand();
//                    Log.d(TAG, "create user: "+edx_name.getText().toString()+","+auth.getUid()+","+UserInn.getInstance().getNumberOfOrder());
                }
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

    private TextWatcher signUpWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String emailInput = edx_email.getText().toString().trim();
            String passwordInput = edx_password.getText().toString().trim();
            String nameInput = edx_name.getText().toString().trim();
            btn.setEnabled(!emailInput.isEmpty()&&!passwordInput.isEmpty()&&!nameInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void goLogin(){
        Intent intent = getIntent();
        if(isSuccessful){
            setResult(RESULT_OK,intent);
            finish();
        }else {
            setResult(RESULT_CANCELED,intent);
            finish();
        }

    }

    private void findViews(){
        btn = findViewById(R.id.btn_sign_up);
        edx_email = findViewById(R.id.email_sign_up);
        edx_password = findViewById(R.id.password_sign_up);
        edx_name = findViewById(R.id.name_sign_up);
    }
}

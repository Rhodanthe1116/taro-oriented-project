package com.genomu.starttravel.ui.users;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.genomu.starttravel.LoginActivity;
import com.genomu.starttravel.R;
import com.google.firebase.auth.FirebaseAuth;

public class UsersFragment extends Fragment {

    private boolean logged = false;

    private FirebaseAuth auth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            logged = true;
        }
        View view = inflater.inflate(R.layout.fragment_users,container,false);
        Button btn = view.findViewById(R.id.test_btn);
        Button btn_get = view.findViewById(R.id.get_current_user);
        Button btn_out = view.findViewById(R.id.logout);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!logged){
                    goLogin();
                }
            }
        });
        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get();
            }
        });
        btn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
            }
        });
        return view;
    }

    private void get(){
        Toast.makeText(getActivity(),auth.getUid(),Toast.LENGTH_LONG).show();
    }

    private void goLogin(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }


}

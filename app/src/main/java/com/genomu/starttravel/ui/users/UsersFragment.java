package com.genomu.starttravel.ui.users;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.genomu.starttravel.LoginActivity;
import com.genomu.starttravel.R;
import com.genomu.starttravel.UserAuth;
import com.genomu.starttravel.util.DatabaseInvoker;
import com.genomu.starttravel.util.GetUserCommand;
import com.genomu.starttravel.util.HanWen;
import com.genomu.starttravel.util.NameDBObserver;
import com.google.firebase.auth.FirebaseAuth;

import static com.genomu.starttravel.util.DBAspect.*;

public class UsersFragment extends Fragment{

    private static final String TAG = UsersFragment.class.getSimpleName();
    private boolean logged = false;

    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static UserAuth userAuth = UserAuth.getInstance();
    private ImageView userImage;
    private TextView userName;

    @Override
    public void onStart() {
        super.onStart();
        userAuth.updateAuth();
        Log.d(TAG, "userInn status:"+ userAuth.getStatus());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if(auth.getCurrentUser()!=null){
            userAuth.setLogged(true);
        }

        View view = inflater.inflate(R.layout.fragment_users,container,false);
        Button btn = view.findViewById(R.id.test_btn);
        Button btn_get = view.findViewById(R.id.get_current_user);
        Button btn_out = view.findViewById(R.id.logout);
        userImage = view.findViewById(R.id.image_user);
        userName = view.findViewById(R.id.name_user);

        DatabaseInvoker invoker = new DatabaseInvoker();
        NameDBObserver dbObserver = new NameDBObserver(userName);
        GetUserCommand command = new GetUserCommand(new HanWen(),userAuth.getUserUID());
        command.attach(dbObserver,NAME);
        invoker.addCommand(command);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userAuth.isLogged()){
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
                userAuth.goDefault();
                userImage.setImageResource(R.drawable.default_user);
                userName.setText("User");
            }
        });

        if(userAuth.isLogged()) {
            userImage.setImageResource(R.drawable.logged_user);
            invoker.assignCommand();
        }

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

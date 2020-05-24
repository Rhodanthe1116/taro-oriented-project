package com.genomu.starttravel.nav_pages.users;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import genomu.fire_image_helper.ImagePickingToken;
import genomu.fire_image_helper.LoadMan;
import com.genomu.starttravel.activity.LoginActivity;
import com.genomu.starttravel.activity.MainActivity;
import com.genomu.starttravel.R;
import com.genomu.starttravel.UserAuth;
import com.genomu.starttravel.util.DatabaseInvoker;
import com.genomu.starttravel.util.GetUserCommand;
import com.genomu.starttravel.util.HanWen;
import com.genomu.starttravel.util.NameDBObserver;
import com.genomu.starttravel.util.OnOneOffClickListener;
import com.genomu.starttravel.util.OrdersDBObserver;
import com.google.firebase.auth.FirebaseAuth;

import static com.genomu.starttravel.activity.LoginActivity.FUNC_LIN;
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

        if(userAuth.isLogged()){
            view = inflater.inflate(R.layout.fragment_users_logged,container,false);
            Button log_out = view.findViewById(R.id.log_out_btn);
            log_out.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    alertSignOut();
                }
            });
            userName = view.findViewById(R.id.name_users_logged);
            userImage = view.findViewById(R.id.image_users_logged);
            final String storeKey = "profile/"+userAuth.getUserUID();
            LoadMan loadMan = new LoadMan(storeKey,getActivity());
            loadMan.loadImage(userImage);
            userImage.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    changeProfilePic(storeKey);
                }
            });
            TextView membership = view.findViewById(R.id.membership);
            RecyclerView recyclerView = view.findViewById(R.id.order_list_users_logged);
            DatabaseInvoker invoker = new DatabaseInvoker();
            NameDBObserver dbObserver = new NameDBObserver(userName,membership);
            GetUserCommand command = new GetUserCommand(new HanWen(),userAuth.getUserUID());
            command.attach(dbObserver,NAME);
            command.attach(new OrdersDBObserver(recyclerView,getActivity()), ORDERS);
            invoker.addCommand(command);
            invoker.assignCommand();

        }else {
            Button btn = view.findViewById(R.id.signin_btn);
            userImage = view.findViewById(R.id.image_user);
            userName = view.findViewById(R.id.name_user);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!userAuth.isLogged()){
                        goLogin();
                    }
                }
            });
        }


        return view;
    }

    private void alertSignOut() {
        new AlertDialog.Builder(getContext()).setTitle("登出").setMessage("確定要登出?")
                .setPositiveButton(R.string.confirm_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goSignOut();
                    }
                })
                .setNegativeButton(R.string.cancel_btn,null).show();
    }

    private void goSignOut() {
        MainActivity.navGto(R.id.navigation_users);
        auth.signOut();
        userAuth.goDefault();
    }

    private void changeProfilePic(final String storeKey) {
        new AlertDialog.Builder(getContext())
                .setTitle("上傳大頭貼")
                .setNegativeButton(R.string.cancel_btn,null)
                .setPositiveButton(R.string.confirm_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ImagePickingToken token = (ImagePickingToken) getActivity();
                        token.setStoreKey(storeKey);
                    }
                }).show();
    }

    private void goLogin(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivityForResult(intent,FUNC_LIN);
    }


}

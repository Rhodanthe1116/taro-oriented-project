package com.genomu.starttravel;

import com.google.firebase.auth.FirebaseAuth;

public class UserAuth {
    private String userUID;
    private static UserAuth userAuthInstance = null;
    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private boolean logged;

    private UserAuth(String userUID, boolean logged) {
        this.userUID = userUID;
        this.logged = logged;
    }

    public static UserAuth getInstance() {
        synchronized (UserAuth.class){
            if(userAuthInstance == null){
                if(auth.getCurrentUser()!=null){
                    userAuthInstance = new UserAuth(auth.getUid(),true);
                }else {
                    userAuthInstance = new UserAuth("b07505019", false);
                }
            }
            userAuthInstance.updateAuth();
        }
        return userAuthInstance;
    }

    public void updateAuth(){
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null) {
            userAuthInstance.userUID = auth.getUid();
        }else {
            userAuthInstance.userUID = "b07505019";
        }
    }
    public String getStatus(){
        String msg = "logged>>"+ logged+" , UID>>" + userUID;
        return  msg;
    }


    public void goDefault(){
        setLogged(false);
        setUserUID("b07505019");
    }

    public String getUserUID() {
        return userUID;
    }

    private void setUserUID(String userUID) {
        this.userUID = userUID;
    }


    public boolean isLogged() {
        if(auth.getCurrentUser()!=null)
            logged = true;
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }
}

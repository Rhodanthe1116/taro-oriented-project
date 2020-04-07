package com.genomu.starttravel;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static String search_content = "";
    private static boolean searched = false;
    private static NavController navController;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "userInn status:"+ UserAuth.getInstance().getStatus());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_users,R.id.navigation_list)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        ActionBar ab = getSupportActionBar();
        ab.hide();
        int nav = getIntent().getIntExtra("nav",R.id.navigation_home);
        navGto(nav);


    }

    public static void navGto(int res_id){
        navController.navigate(res_id);
    }

    public static String getSearch_content() {
        return search_content;
    }

    public static void setSearch_content(String search_content) {
        MainActivity.search_content = search_content;
        MainActivity.searched = true;
    }

    public static void setSearched(Boolean searched){
        MainActivity.searched = searched;
    }

    public static boolean getSearched(){
        return MainActivity.searched;
    }


}

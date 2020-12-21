package com.fiek.ejobs.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.fiek.ejobs.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    ChipNavigationBar bottomBar;
    NavController navController;
    FragmentManager fragmentManager;
    FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navController= Navigation.findNavController(MainActivity.this,R.id.nav_host_fragment);
        bottomBar=findViewById(R.id.bottomBar);
        bottomBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {

                switch (i){
                    case R.id.mainPostsFragment:
                       navController.navigate(R.id.mainPostsFragment);
                        toolbar.setTitle(getResources().getString(R.string.homeMenu));
                        break;
                    case R.id.item2Fragment:
                        navController.navigate(R.id.item2Fragment);
                        toolbar.setTitle(getResources().getString(R.string.favoriteMenu));
                    break;
                    case R.id.profileFragment:
                        navController.navigate(R.id.profileFragment);
                        toolbar.setTitle(getResources().getString(R.string.statisticsMenu));
                        break;
                    case R.id.statisticsFragment:
                        navController.navigate(R.id.staticticsFragment);
                        toolbar.setTitle(getResources().getString(R.string.profileMenu));
                        break;
                }
            }
        });
        bottomBar.setItemSelected(R.id.mainPostsFragment,true);
        currentUser = mAuth.getCurrentUser();
        if(currentUser ==null){
            startActivity(new Intent(MainActivity.this, LogInActivity.class));
            finish();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        bottomBar=findViewById(R.id.bottomBar);
        getMenuInflater().inflate(R.menu.menu,menu);
        NavigationUI.setupWithNavController(bottomBar,navController);
        return super.onCreateOptionsMenu(menu);

    }*/
}

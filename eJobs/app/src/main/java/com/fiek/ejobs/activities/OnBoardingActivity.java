package com.fiek.ejobs.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.fiek.ejobs.utils.OnBoardingTransform;
import com.fiek.ejobs.adapters.OnBoardingViewPagerAdapter;
import com.fiek.ejobs.R;

public class OnBoardingActivity extends AppCompatActivity {

    public static ViewPager viewPager;
    OnBoardingViewPagerAdapter onBoardingViewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        viewPager=findViewById(R.id.viewPager);
        onBoardingViewPagerAdapter= new OnBoardingViewPagerAdapter(this);
        viewPager.setAdapter(onBoardingViewPagerAdapter);
        viewPager.setPageTransformer(true, new OnBoardingTransform());
        if(isOpenAlready()){
            Intent intent=new Intent(OnBoardingActivity.this, LogInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else {
            SharedPreferences.Editor editor=getSharedPreferences("onBoarding",MODE_PRIVATE).edit();
            editor.putBoolean("onBoarding",true);
            editor.commit();
        }
    }

    private boolean isOpenAlready() {
        SharedPreferences sharedPreferences=getSharedPreferences("onBoarding",MODE_PRIVATE);
        boolean result=sharedPreferences.getBoolean("onBoarding",false);
        return false;
    }
}

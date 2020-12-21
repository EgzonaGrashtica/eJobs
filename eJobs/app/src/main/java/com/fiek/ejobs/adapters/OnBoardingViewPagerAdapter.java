package com.fiek.ejobs.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.fiek.ejobs.R;
import com.fiek.ejobs.activities.LogInActivity;
import com.fiek.ejobs.activities.OnBoardingActivity;

public class OnBoardingViewPagerAdapter extends PagerAdapter {

    Context ctx;

    public OnBoardingViewPagerAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater= (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.on_boarding_screen,container,false);
        Animation animation1 = AnimationUtils.loadAnimation(ctx,R.anim.form_right_to_left);
        Animation animation2 = AnimationUtils.loadAnimation(ctx,R.anim.from_left_to_right);
        ImageView firstPhoto=view.findViewById(R.id.firstPhoto);
        ImageView indOne=view.findViewById(R.id.indOne);
        ImageView indTwo=view.findViewById(R.id.indtwo);
        ImageView indThree=view.findViewById(R.id.indThree);

        TextView firstIntroTitle=view.findViewById(R.id.firstIntroTitle);
        TextView firstIntroText=view.findViewById(R.id.firstIntroText);

        Button continueBtn=view.findViewById(R.id.contiueBtn);
        switch (position)
        {
            case 0:
                firstPhoto.setImageResource(R.drawable.first_onboarding_photo);
                indOne.setImageResource(R.drawable.selected);
                indTwo.setImageResource(R.drawable.unselected);
                indThree.setImageResource(R.drawable.unselected);


                firstIntroTitle.setText(R.string.first_intro_title);
                firstIntroText.setText(R.string.first_intro_text);

                continueBtn.setText(R.string.continueBtn);
                continueBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnBoardingActivity.viewPager.setCurrentItem(position+1);

                    }
                });

                break;
            case 1:
                firstPhoto.setImageResource(R.drawable.second_photo);
                indOne.setImageResource(R.drawable.unselected);
                indTwo.setImageResource(R.drawable.selected);
                indThree.setImageResource(R.drawable.unselected);

                firstIntroTitle.setText(R.string.second_intro_title);
                firstIntroText.setText(R.string.second_intro_text);
                continueBtn.setText(R.string.continueBtn);
                continueBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnBoardingActivity.viewPager.setCurrentItem(position+1);
                    }
                });

                break;
            case 2:
                firstPhoto.setImageResource(R.drawable.third_photo);
                indOne.setImageResource(R.drawable.unselected);
                indTwo.setImageResource(R.drawable.unselected);
                indThree.setImageResource(R.drawable.selected);

                firstIntroTitle.setText(R.string.third_intro_title);
                firstIntroText.setVisibility(view.GONE);
                continueBtn.setText(R.string.getStartedBtn);
                continueBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(ctx, LogInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                        ctx.startActivity(intent);

                    }
                });

                break;

        }


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

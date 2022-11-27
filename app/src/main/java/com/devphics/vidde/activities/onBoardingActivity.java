package com.devphics.vidde.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import com.devphics.vidde.Adapters.SliderAdapter;
import com.devphics.vidde.R;

public class onBoardingActivity extends AppCompatActivity {
    ViewPager viewPager;
    LinearLayout dotsLayout;
    SliderAdapter adapter;
    public static ImageView imageView;
    TextView[] dots;
    Button nextButton;
    public static int CURRENT_POSSITION = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        viewPager = findViewById(R.id.viewpager);
        dotsLayout = findViewById(R.id.dotsLayout);
        imageView = findViewById(R.id.ivdots);
        nextButton = findViewById(R.id.nextOnBoarding);
        nextButton.setOnClickListener(view -> {
            if(nextButton.getText().toString()=="Get Started"){
                getSharedPreferences("config",MODE_PRIVATE).edit().putString("isAvailable","y").apply();
            }

            if(CURRENT_POSSITION==0)
            {
                viewPager.setCurrentItem(1,true);
            }else if(CURRENT_POSSITION==1){
                viewPager.setCurrentItem(2,true);
            }else{
                startActivity(new Intent(onBoardingActivity.this,PermissionActivity.class));
                finish();
            }
        });

        adapter = new SliderAdapter(this);

        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    imageView.setImageResource(R.drawable.page1dot);
                    nextButton.setText("Next");
                    CURRENT_POSSITION = position;
                }else if(position==1){
                    imageView.setImageResource(R.drawable.page2dot);
                    nextButton.setText("Next");
                    CURRENT_POSSITION = position;
                }else{
                    imageView.setImageResource(R.drawable.page3dot);
                    nextButton.setText("Get Started");
                    CURRENT_POSSITION = position;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }





}
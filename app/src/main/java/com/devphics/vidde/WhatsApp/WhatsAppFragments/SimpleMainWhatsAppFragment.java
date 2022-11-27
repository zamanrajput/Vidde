package com.devphics.vidde.WhatsApp.WhatsAppFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.devphics.vidde.R;
import com.devphics.vidde.WhatsApp.Adapters.SimpleWhatsAppPagerAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;


public class SimpleMainWhatsAppFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_main_simple_whatsapp,container,false);

        TabItem videoTab = v.findViewById(R.id.VideoTabS);
        TabItem imageTab = v.findViewById(R.id.ImageTabS);
        TabLayout layout = v.findViewById(R.id.tabLayoutS);
        ViewPager viewPager  = v.findViewById(R.id.ViewPagerS);





        SimpleWhatsAppPagerAdapter pagerAdapter = new SimpleWhatsAppPagerAdapter(getChildFragmentManager(),2);
        layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0||tab.getPosition()==1) pagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });







        viewPager.setAdapter(pagerAdapter);


        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(layout));


        return v;

    }



}
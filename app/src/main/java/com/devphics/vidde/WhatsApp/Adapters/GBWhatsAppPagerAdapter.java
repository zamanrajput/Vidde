package com.devphics.vidde.WhatsApp.Adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.devphics.vidde.WhatsApp.WhatsAppFragments.GBWhatsAppImagesFragment;
import com.devphics.vidde.WhatsApp.WhatsAppFragments.GBWhatsAppVideoFragment;

public class GBWhatsAppPagerAdapter extends FragmentPagerAdapter {

    int tabCount;
    public GBWhatsAppPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm,behavior);
        tabCount = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new GBWhatsAppVideoFragment();
            case 1:
                return  new GBWhatsAppImagesFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }
}

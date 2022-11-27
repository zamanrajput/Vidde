package com.devphics.vidde.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.devphics.vidde.R;

public class SliderAdapter extends PagerAdapter {
    Context context;




    int[] dots={
        R.drawable.page1dot, R.drawable.page2dot,R.drawable.page3dot
    };

    LayoutInflater layoutInflater;
    String[] disc = {
            "Download video through link or open social media apps directly from our app to get easy access.",
            "Download videos in any format from 144p to Full HD or even convert to mp3 audio",
            "Enjoy the videos and share it with friends and family members"
    };
    String[] heading = {
            "Download Video",
            "Select video or audio quality",
            "Enjoy the video with and share with friends"

    };
    int[] images = {
            R.drawable.b1,
            R.drawable.b2,
            R.drawable.b3
    };

    public SliderAdapter(Context context) {
        this.context = context;


    }





    @Override
    public int getCount() {
        return heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {


        return view==(LinearLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout, container, false);
        TextView head, dis;
        head = view.findViewById(R.id.headingTV);
        dis = view.findViewById(R.id.disTV);
        ImageView imageView = view.findViewById(R.id.imgV);
        imageView.setImageResource(images[position]);

        head.setText(heading[position]);
        dis.setText(disc[position]);

        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position,@NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}

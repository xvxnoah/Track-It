package com.example.trackit.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.trackit.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;
    }

    //Arrays
    public int[] slide_images = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3
    };

    public String[] slide_headings = {
            "Guanya control total dels teus diners",
            "Coneix on van els teus diners",
            "Sigues previsor"
    };

    public String[] slide_descs = {
            "Converteix-te en el teu propi gestor de diners i fes que cada cèntim valgui",
            "Fes un seguiment de les teves operacions amb categories i informes financers",
            "Configura el teu pressupost per cada categoria per tenir un bon control"
    };
    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDesription = (TextView) view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDesription.setText(slide_descs[position]);

        container.addView(view);

        return view;
    };

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){

        container.removeView((RelativeLayout)object);

    }
}

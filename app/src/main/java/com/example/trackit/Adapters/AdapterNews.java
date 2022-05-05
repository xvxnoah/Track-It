package com.example.trackit.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackit.News.Detailed;
import com.example.trackit.Model.Articles;
import com.example.trackit.R;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterNews extends RecyclerView.Adapter<AdapterNews.ViewHolder> {

    Context context;
    List<Articles> articles;

    public AdapterNews(Context context, List<Articles> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public AdapterNews.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNews.ViewHolder holder, int position) {
        final Articles a = articles.get(position);

        String imageUrl = a.getUrlToImage();
        String url = a.getUrl();

        Picasso.with(context).load(imageUrl).into(holder.imageView);

        holder.tvTitle.setText(a.getTitle());
        holder.tvSource.setText(a.getSource().getName());
        holder.tvDate.setText("\u2022"+dateTime(a.getPublishedAt()));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Detailed.class);
                intent.putExtra("title", a.getTitle());
                intent.putExtra("source", a.getSource().getName());
                intent.putExtra("time", dateTime(a.getPublishedAt()));
                intent.putExtra("desc", a.getDescription());
                intent.putExtra("imageUrl", a.getUrlToImage());
                intent.putExtra("url", a.getUrl());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSource, tvDate;
        ImageView imageView;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSource = itemView.findViewById(R.id.tvSource);
            tvDate = itemView.findViewById(R.id.tvDate);
            imageView = itemView.findViewById(R.id.imageNews);
            cardView = itemView.findViewById(R.id.cardViewNews);
        }
    }

    public String dateTime(String t){
        PrettyTime prettyTime = new PrettyTime(new Locale(getCountry()));
        String time = null;

        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:", new Locale("es", "ES"));
            Date date = simpleDateFormat.parse(t);
            time = prettyTime.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

    public String getCountry(){
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();

        return country.toLowerCase();
    }
}

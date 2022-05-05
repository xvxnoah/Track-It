package com.example.trackit.News;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.trackit.Adapters.AdapterNews;
import com.example.trackit.Data.ApiClient;
import com.example.trackit.Model.Articles;
import com.example.trackit.Model.Headlines;
import com.example.trackit.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsPage extends AppCompatActivity {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText etQuery;
    Button btnSearch;
    final String API_KEY = "9ce36b4727ec42e9a5e21ad47eac4b1f";
    AdapterNews adapterNews;
    List<Articles> articles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);
        getSupportActionBar().hide();

        Window window = NewsPage.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(NewsPage.this, R.color.whiteNews));

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.recyclerViewNews);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        etQuery = findViewById(R.id.editTextNews);
        btnSearch = findViewById((R.id.buttonNews));

        String country = getCountry();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveJson("", country, API_KEY);
            }
        });
        retrieveJson("", country, API_KEY);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etQuery.getText().toString().equals("")){
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            retrieveJson("", country, API_KEY);
                        }
                    });
                    retrieveJson("", country, API_KEY);
                } else{
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            retrieveJson(etQuery.getText().toString(), country, API_KEY);
                        }
                    });
                    retrieveJson(etQuery.getText().toString(), country, API_KEY);
                }
            }
        });
        //setListeners();
    }

    public void retrieveJson(String query, String country, String apiKey){
        swipeRefreshLayout.setRefreshing(true);
        Call<Headlines> call;

        if(!etQuery.getText().toString().equals("")){
            call = ApiClient.getInstance().getApi().getSpecificData(query, apiKey);
        } else{
            call = ApiClient.getInstance().getApi().getHeadlines(country, apiKey);
        }

        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if(response.isSuccessful() && response.body().getArticles() != null){
                    swipeRefreshLayout.setRefreshing(false);
                    articles.clear();
                    articles = response.body().getArticles();
                    adapterNews = new AdapterNews(NewsPage.this, articles);
                    recyclerView.setAdapter(adapterNews);
                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(NewsPage.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getCountry(){
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();

        return country.toLowerCase();
    }

    // CHECK
    private void setListeners() {
        ImageButton back = findViewById(R.id.back_income);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
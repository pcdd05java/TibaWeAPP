package idv.ca107g2.tibawe.classzone;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;

import idv.ca107g2.tibawe.campuszone.LatestNews;
import idv.ca107g2.tibawe.campuszone.LatestNewsAdapter;
import idv.ca107g2.tibawe.R;

public class Class_News extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_news);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String[] newsTitles = new String[LatestNews.latestNews.length];
        for(int i = 0 ; i <newsTitles.length; i++){
            newsTitles[i] = LatestNews.latestNews[i].getNewsTitle();
        }


        int[] newsPics = new int[LatestNews.latestNews.length];
        for(int i = 0; i<newsPics.length; i++){
            newsPics[i] = LatestNews.latestNews[i].getNewsPicId();
        }

        String[] newsContents = new String[LatestNews.latestNews.length];
        for(int i = 0 ; i <newsContents.length; i++){
            newsContents[i] = LatestNews.latestNews[i].getNewsContent();
        }


        RecyclerView newsRecycler = findViewById(R.id.rvClassNews);
        LatestNewsAdapter adapter = new LatestNewsAdapter(newsTitles, newsPics, newsContents);
        newsRecycler.setAdapter(adapter);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        newsRecycler.setLayoutManager(layoutManager);
    }
}

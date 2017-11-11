package com.rojina.endlessscrollyoutubevideoplayer.videos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rojina.endlessscrollyoutubevideoplayer.R;
import com.rojina.endlessscrollyoutubevideoplayer.api.ApiService;
import com.rojina.endlessscrollyoutubevideoplayer.api.RetrofitApiClient;
import com.rojina.endlessscrollyoutubevideoplayer.base.BaseActivity;
import com.rojina.endlessscrollyoutubevideoplayer.utils.EndlessRecyclerViewScrollListener;
import com.rojina.endlessscrollyoutubevideoplayer.utils.NetworkUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by
 * name:rojina kc
 * email:rojinakc41@gmail.com
 * on 10/15/2017.
 */


public class VideosActivity extends BaseActivity {

    EndlessRecyclerViewScrollListener scrollListener;
    private static final String TAG=VideosActivity.class.getSimpleName();
    VideoNewsAdapter shortMovieListAdapter;
    RecyclerView recyclerView;
    ApiService kathmanduOnlineService;
LinearLayoutManager linearLayoutManager;
    ProgressBar moreVideoProgressBar;
    TextView errorText;
    String pageToken;
    List<Video.Item> videoDtoList;
    List<Video.Item> allVIdeoList;

    String name;
    public static final String EXTRA_URL="playlistUrl";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            if(bundle.containsKey("name")){
                name=bundle.getString("name");
            }
        }
        setContentView(R.layout.activity_videos);
        setUpToolbar(name);
        initializeUi();

    }

    private void initializeUi() {
        kathmanduOnlineService= new RetrofitApiClient(this).createPurchaseService(ApiService.class);
        recyclerView = (RecyclerView) findViewById(R.id.show_video_news_rv);
        errorText= (TextView) findViewById(R.id.show_error_text);
        moreVideoProgressBar= (ProgressBar) findViewById(R.id.load_more_videos_progress_bar);
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);


    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchNewsVideo("");
        onScrollListener();
    }

    private void fetchNewsVideo(String token) {
        showDialog(getString(R.string.progress_bar_video));

        Call<Video> call=kathmanduOnlineService.showVieoNews(token);
        call.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {

                if(response.isSuccessful()) {
                    hideDialog();
                    Log.d(TAG, "inside success");
                    allVIdeoList=response.body().getItems();
                    loadNewsVideo(response.body().getItems());
                    pageToken=response.body().getNextPageToken();
                }else{
                    hideDialog();

                    errorText.setText(getString(R.string.cannot_connect_to_server));
                }
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
                hideDialog();
              //  Log.d(TAG,"inside error");
                //Log.d(TAG,"Error");
                errorText.setText(getString(R.string.cannot_connect_to_server));
                recyclerView.clearOnScrollListeners();

            }
        });
       

    }

    private void loadNewsVideo(List<Video.Item> shortMovieDTOList) {
        //Log.d(TAG,"inside load video");


        shortMovieListAdapter = new VideoNewsAdapter(VideosActivity.this,shortMovieDTOList);
        shortMovieListAdapter.setVideoNewsClickListener(newsClickListener);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(shortMovieListAdapter);

    }

    private void onScrollListener(){
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, final int totalItemsCount, RecyclerView view) {
               createObject(pageToken);
                moreVideoProgressBar.setVisibility(View.VISIBLE);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    private void createObject(String token) {
        Call<Video> call=kathmanduOnlineService.showVieoNews(token);
        call.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {

                if (response.isSuccessful()) {

                    videoDtoList = response.body().getItems();
                    pageToken = response.body().getNextPageToken();
                    allVIdeoList.addAll(videoDtoList);

                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            moreVideoProgressBar.setVisibility(View.GONE);
                            shortMovieListAdapter.notifyItemRangeInserted(shortMovieListAdapter.getItemCount(), allVIdeoList.size() - 1);
                        }
                    });
                }
                else{
                    Toast.makeText(VideosActivity.this, getString(R.string.cannot_connect_to_server), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
                hideDialog();
                Log.d(TAG,"inside error");
                Snackbar.make(errorText,t.getCause().getMessage().toString(), Snackbar.LENGTH_LONG);

            }
        });

    }

    private VideoNewsAdapter.onVideoNewsClickListener newsClickListener=
            new VideoNewsAdapter.onVideoNewsClickListener() {
                @Override
                public void onClick(Video.Item item, int position) {
                    if(NetworkUtil.isInternetOn(VideosActivity.this)) {
                        Intent plaVideoIntent = new Intent(VideosActivity.this, VideoPlayerActivity.class);
                        plaVideoIntent.putExtra(VideoPlayerActivity.EXTRA_VIDEO_ID, item.getId().getVideoId());
                        plaVideoIntent.putExtra(VideoPlayerActivity.EXTRA_TITLE, item.getSnippet().getTitle());
                        startActivity(plaVideoIntent);
                    }else{
                        Toast.makeText(VideosActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    }
                }
            };


    }





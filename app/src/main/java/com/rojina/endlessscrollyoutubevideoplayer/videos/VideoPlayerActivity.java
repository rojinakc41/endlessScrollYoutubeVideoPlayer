package com.rojina.endlessscrollyoutubevideoplayer.videos;

import android.os.Bundle;
import android.util.Log;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.rojina.endlessscrollyoutubevideoplayer.R;
import com.rojina.endlessscrollyoutubevideoplayer.utils.AppText;

/**
 * Created by
 * name:rojina kc
 * email:rojinakc41@gmail.com
 * on 10/15/2017.
 */


public class VideoPlayerActivity extends YouTubeFailureRecoveryActivity {

    private static final String TAG = VideoPlayerActivity.class.getSimpleName();

    public static final String EXTRA_VIDEO_ID = "videoId";
    public static final String EXTRA_TITLE = "videoTitle";


    private String videoId;

    YouTubePlayerView youTubeView;
String orientation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);


        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            if(bundle.containsKey(EXTRA_VIDEO_ID))
                videoId = bundle.getString(EXTRA_VIDEO_ID);

        }

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);

        youTubeView.initialize(AppText.DEVELOPER_KEY_YOUTUBE_API, this);
    }




    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {


        Log.d(TAG, "onInitializationSuccess:wasRestored::" + wasRestored);

        if (!wasRestored) {
            youTubePlayer.loadVideo(videoId);
        }


    }
}

package com.rojina.endlessscrollyoutubevideoplayer.videos;

import android.content.Intent;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.rojina.endlessscrollyoutubevideoplayer.utils.AppText;


/**
 * Created by
 * name:rojina kc
 * email:rojinakc41@gmail.com
 * on 10/15/2017.
 */

public abstract class YouTubeFailureRecoveryActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private final String TAG = YouTubeFailureRecoveryActivity.class.getName();
    private static final int RECOVERY_DIALOG_REQUEST = 12;


    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {

        //Log.d(TAG, "onInitializationFailure");
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = (errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(AppText.DEVELOPER_KEY_YOUTUBE_API, this);
        }
    }

    protected abstract YouTubePlayer.Provider getYouTubePlayerProvider();

}

package com.rojina.endlessscrollyoutubevideoplayer.api;



import com.rojina.endlessscrollyoutubevideoplayer.utils.AppText;
import com.rojina.endlessscrollyoutubevideoplayer.videos.Video;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by
 * name:rojina kc
 * email:rojinakc41@gmail.com
 * on 10/15/2017.
 */


public interface ApiService {

    @GET(AppText.YOUTUBE_REMAINING_URL)
    Call<Video> showVieoNews(@Query("pageToken") String token);



}

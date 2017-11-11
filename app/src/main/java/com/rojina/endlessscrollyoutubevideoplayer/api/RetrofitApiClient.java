package com.rojina.endlessscrollyoutubevideoplayer.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rojina.endlessscrollyoutubevideoplayer.utils.AppText;
import com.rojina.endlessscrollyoutubevideoplayer.utils.NetworkUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;

/**
 * Created by
 * name:rojina kc
 * email:rojinakc41@gmail.com
 * on 10/15/2017.
 */

public class RetrofitApiClient {
    private static Retrofit retrofit = null;
    private static final String TAG=RetrofitApiClient.class.getSimpleName();
    private static final String CACHE_CONTROL = "Cache-Control";
    static Context context;
    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    public RetrofitApiClient(Context context){
        this.context=context;
    }




    /** this code snippet creates retrofit for purchasing */
    public static Retrofit provideYoutubeRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(AppText.YOUTUBE_BASE_URL)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    private static OkHttpClient provideOkHttpClient(){
        return new OkHttpClient.Builder()
                .addInterceptor( provideHttpLoggingInterceptor() )
                .addInterceptor( provideOfflineCacheInterceptor() )
                .addNetworkInterceptor( provideCacheInterceptor() )
                .cache(provideCache())
                .build();
    }

    private static HttpLoggingInterceptor provideHttpLoggingInterceptor ()
    {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        return logging;
    }


    public static Interceptor provideCacheInterceptor()
    {
        return new Interceptor()
        {
            @Override
            public Response intercept (Chain chain) throws IOException
            {
                Response response = chain.proceed( chain.request() );

                // re-write response header to force use of cache
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(1, TimeUnit.MINUTES)
                        .build();

                return response.newBuilder()
                        .header( CACHE_CONTROL, cacheControl.toString() )
                        .build();
            }
        };
    }

    private static Cache provideCache ()
    {
        Cache cache = null;
        try
        {
            cache = new Cache( new File( context.getCacheDir(), "http-cache" ),
                    20 * 1024 * 1024 ); // 10 MB
         //   Log.d(TAG,cache.toString());
           // Log.d(TAG,cache.directory().getAbsolutePath());
            //Log.d(TAG,cache.directory().getPath());
            //Log.d(TAG,cache.directory().getCanonicalPath());
            //Log.d(TAG,String.valueOf(cache.maxSize()));
        }
        catch (Exception e)
        {
            //Log.d(TAG,"Could not create cache");
        }
        return cache;
    }



    public static Interceptor provideOfflineCacheInterceptor()
    {
        return new Interceptor()
        {
            @Override
            public Response intercept (Chain chain) throws IOException
            {
                Request request = chain.request();

                if (!NetworkUtil.isInternetOn(context))
                {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale( 7, TimeUnit.DAYS)
                            .build();

                    request = request.newBuilder()
                            .header("Content-Encoding","gzip")
                            .cacheControl( cacheControl )
                            .build();
                }

                return chain.proceed( request );
            }
        };
    }



    public  <S> S createPurchaseService(Class<S> serviceClass) {
        return provideYoutubeRetrofit().create(serviceClass);
    }
}

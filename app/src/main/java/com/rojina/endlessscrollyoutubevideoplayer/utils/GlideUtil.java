package com.rojina.endlessscrollyoutubevideoplayer.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rojina.endlessscrollyoutubevideoplayer.R;

/**
 * Created by
 * name:rojina kc
 * email:rojinakc41@gmail.com
 * on 10/15/2017.
 */


public class GlideUtil {
    public static void loadImage(String url, ImageView imageView) {
        if(url!="NULL") {
            Context context = imageView.getContext();
            ColorDrawable cd = new ColorDrawable(ContextCompat.getColor(context, R.color.colorPrimary));
            Glide.with(context)
                    .load(url)
                    .placeholder(cd)
                    .crossFade()
                    .centerCrop()
                    .into(imageView);
        }
    }


}

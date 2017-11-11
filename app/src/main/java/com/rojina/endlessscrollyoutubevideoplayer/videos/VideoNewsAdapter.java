package com.rojina.endlessscrollyoutubevideoplayer.videos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.rojina.endlessscrollyoutubevideoplayer.R;
import com.rojina.endlessscrollyoutubevideoplayer.utils.GlideUtil;

import java.util.List;

/**
 * Created by
 * name:rojina kc
 * email:rojinakc41@gmail.com
 * on 10/15/2017.
 */


public class VideoNewsAdapter extends RecyclerView.Adapter<VideoNewsAdapter.MyViewHolder>{

    private final String TAG = VideoNewsAdapter.class.getName();

    private List<Video.Item> videoList;
    private Context mContext;

    public VideoNewsAdapter(Context context, List<Video.Item> objects) {

        this.mContext = context;
        this.videoList = objects;

     //   Log.d(TAG,"inside video news adapter");
       // Log.d(TAG,"size of object is:"+videoList.size());
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news_video_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.bindView(videoList.get(position));

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }





    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvTitle;
        private YouTubeThumbnailView youtubeThumbnail;
        Video.Item item;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            tvTitle = (TextView)view.findViewById(R.id.textView_title_video_listitem);
            youtubeThumbnail = (YouTubeThumbnailView)view.findViewById(R.id.thumbnailview_videolist);

        }

        private void bindView(Video.Item item){
            this.item=item;
            tvTitle.setText(item.getSnippet().getTitle());

            GlideUtil.loadImage(item.getSnippet().getThumbnails().getMedium().getUrl(),youtubeThumbnail);
            Glide.with(mContext).load(item.getSnippet().getThumbnails().getMedium().getUrl()).into(youtubeThumbnail);
      //      Log.d(TAG,"ImageURL::::"+item.getSnippet().getTitle());


        }


        @Override
        public void onClick(View view) {
            if(menuClickListener!=null){
                menuClickListener.onClick(item,getAdapterPosition());
            }
        }
    }

    public void setVideoNewsClickListener(VideoNewsAdapter.onVideoNewsClickListener menuClick){
        menuClickListener=menuClick;

    }

    private VideoNewsAdapter.onVideoNewsClickListener menuClickListener;

    public interface onVideoNewsClickListener{
        void onClick(Video.Item item, int position);
    }
}

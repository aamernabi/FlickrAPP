package com.pixerf.flickr.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pixerf.flickr.R;
import com.pixerf.flickr.model.Photo;
import com.pixerf.flickr.utils.imageutils.ImageLoader;
import com.pixerf.flickr.view.activities.PhotoActivity;

import java.util.List;

/**
 * created by Aamer on 12/18/2016.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private final static String TAG = PhotoAdapter.class.getSimpleName();
    private Context context;
    private List<Photo> photoList;
    private ImageLoader imageLoader;

    public PhotoAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photoList = photos;
        this.imageLoader = new ImageLoader(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Photo photo = photoList.get(position);
        // lazy-loading image
        imageLoader.load(photo.getUrl(), holder.imageViewPhoto);

        holder.imageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PhotoActivity.class);
                intent.putExtra("photo", photo);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public void clear() {
        //running = false;
        photoList.clear();
        //imageCache.evictAll();
    }

    public void add(List<Photo> list) {
        this.photoList.addAll(list);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPhoto;

        ViewHolder(View itemView) {
            super(itemView);
            imageViewPhoto = (ImageView) itemView.findViewById(R.id.imageViewPhoto);
        }
    }

}

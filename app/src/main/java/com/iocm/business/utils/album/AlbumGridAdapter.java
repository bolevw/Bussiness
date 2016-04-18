package com.iocm.business.utils.album;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.iocm.business.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Jack on 2015/10/20.
 */
public class AlbumGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_CAMERA = 0x0011;
    private static final int TYPE_ITEM = 0x0022;

    private ArrayList<Uri> uris;
    private Activity activity;
    private OnItemClickListener onItemClickListener;
    private boolean enableCamera;
    private int itemWith = 300;

    public AlbumGridAdapter(Activity activity, ArrayList<Uri> uris, OnItemClickListener onItemClickListener, int itemWith, boolean enableCamera) {
        super();
        this.activity = activity;
        this.onItemClickListener = onItemClickListener;
        this.itemWith = itemWith;
        this.enableCamera = enableCamera;
        this.uris = uris;
    }

    @Override
    public int getItemViewType(int position) {
        return (enableCamera && position == 0) ? TYPE_CAMERA : TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == TYPE_CAMERA ?
                new CameraViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_album_grid_camera, parent, false))
                : new GridViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_album_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (enableCamera) {
            if (position == 0) {
                ((CameraViewHolder) holder).albumGridItemCameraButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(null, position);
                    }
                });
            } else {
                Picasso.with(activity)
                        .load(uris.get(position - 1))
                        .placeholder(R.color.cut_line)
                        .centerCrop()
                        .resize(itemWith, itemWith)
                        .into(((GridViewHolder) holder).albumGridItemImageView);
                ((GridViewHolder) holder).albumGridItemButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(uris.get(position - 1), position);
                    }
                });
            }
        } else {
            Picasso.with(activity)
                    .load(uris.get(position))
                    .placeholder(R.color.cut_line)
                    .centerCrop()
                    .resize(itemWith, itemWith)
                    .into(((GridViewHolder) holder).albumGridItemImageView);
            ((GridViewHolder) holder).albumGridItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(uris.get(position), position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return enableCamera ? uris.size() + 1 : uris.size();
    }


    public class GridViewHolder extends RecyclerView.ViewHolder {
        public ImageView albumGridItemImageView;
        public Button albumGridItemButton;

        public GridViewHolder(View view) {
            super(view);
            this.albumGridItemImageView = (ImageView) view.findViewById(R.id.albumGridItemImageView);
            this.albumGridItemButton = (Button) view.findViewById(R.id.albumGridItemButton);
        }
    }

    public class CameraViewHolder extends RecyclerView.ViewHolder {
        public Button albumGridItemCameraButton;

        public CameraViewHolder(View view) {
            super(view);
            this.albumGridItemCameraButton = (Button) view.findViewById(R.id.albumGridItemCameraButton);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(Uri uri, int position);
    }

}

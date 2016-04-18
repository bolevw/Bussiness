package com.iocm.business.utils.album;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Jack on 2015/10/20.
 */
public class AlbumSpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public AlbumSpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        outRect.top = space;
    }
}

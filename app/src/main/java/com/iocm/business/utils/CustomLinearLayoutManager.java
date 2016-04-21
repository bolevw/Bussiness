package com.iocm.business.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/4/21.
 */
public class CustomLinearLayoutManager extends LinearLayoutManager {

    private static final String TAG = CustomLinearLayoutManager.class.getSimpleName();

    public CustomLinearLayoutManager(Context context) {
        super(context);
        this.context = context;
    }

    public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    private int[] mMeasuredDimension = new int[2];

    private Context context;

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        int height = 0;
        int childCount = getItemCount();
        height = (int) (context.getResources().getDisplayMetrics().density * 40 * childCount);
        Log.d("count", childCount + "");
        setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
    }


    private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec,
                                   int heightSpec, int[] measuredDimension) {
        try {
            View view = recycler.getViewForPosition(0);//fix 动态添加时报IndexOutOfBoundsException

            if (view != null) {
                RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();

                int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
                        getPaddingLeft() + getPaddingRight(), p.width);

                int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                        getPaddingTop() + getPaddingBottom(), p.height);

                view.measure(childWidthSpec, childHeightSpec);
                measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
                measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
                recycler.recycleView(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
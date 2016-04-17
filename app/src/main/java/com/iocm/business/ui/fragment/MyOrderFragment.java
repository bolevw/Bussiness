package com.iocm.business.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iocm.business.R;
import com.iocm.business.base.BaseFragment;
import com.iocm.business.model.OrderModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liubo on 4/16/16.
 */
public class MyOrderFragment extends BaseFragment {


    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;


    private List<OrderModel> viewData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_order, container, false);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    protected void initView(View v) {
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.myOrderSwipeRefreshLayout);
        recyclerView = (RecyclerView) v.findViewById(R.id.myOrderRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

    @Override
    protected void bind() {

    }

    @Override
    protected void setListener() {
        recyclerView.setAdapter(new RVAdapter());
        swipeRefreshLayout.setOnRefreshListener(refreshListener);

    }

    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

        }
    };

    @Override
    protected void unbind() {

    }


    private class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(getActivity()).inflate(R.layout.item_my_order_recyclerview, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            OrderModel model = viewData.get(position);
            VH vh = (VH) holder;
            vh.name.setText(model.getUsername());
            vh.detail.setText(model.getDetail());
            vh.tableNo.setText("桌号：" + model.getTableNo());

            vh.content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return viewData.size();
        }

        private class VH extends RecyclerView.ViewHolder {

            private LinearLayout content;
            private TextView name;
            private TextView tableNo;
            private TextView detail;


            public VH(View itemView) {
                super(itemView);
                content = (LinearLayout) itemView.findViewById(R.id.itemMyOrderContent);
                name = (TextView) itemView.findViewById(R.id.itemUsername);
                tableNo = (TextView) itemView.findViewById(R.id.itemTableNo);
                detail = (TextView) itemView.findViewById(R.id.itemOrderDetail);

            }
        }
    }
}

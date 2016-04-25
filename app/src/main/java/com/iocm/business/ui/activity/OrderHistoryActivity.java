package com.iocm.business.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.iocm.business.R;
import com.iocm.business.base.BaseActivity;
import com.iocm.business.model.OrderHistoryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/21.
 */
public class OrderHistoryActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<OrderHistoryModel> viewData = new ArrayList<>();
    private int allMoney;
    private TextView allMoneyTextView;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_order_history);

        recyclerView = (RecyclerView) findViewById(R.id.orderHistoryRecyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.orderHistoryRefreshLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        allMoneyTextView = (TextView) findViewById(R.id.allMoneyTextView);


    }

    @Override
    protected void setListener() {
        recyclerView.setAdapter(new RVAdapter());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    @Override
    protected void bind() {
        getData();
    }

    private void getData() {

        AVQuery<AVObject> query = new AVQuery<>("OrderTable");
        query.whereGreaterThan("orderStatus", 4);
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                swipeRefreshLayout.setRefreshing(false);
                if (e == null && list.size() > 0) {
                    viewData.clear();
                    allMoney = 0;
                    for (AVObject object : list) {
                        OrderHistoryModel model = new OrderHistoryModel();
                        model.setTablenum(object.getString("tableNum"));
                        model.setMoney(object.getString("orderMoney"));
                        model.setOrderStatus(object.getNumber("orderStatus").intValue());
                        model.setOrderNum(object.getObjectId());
                        if (model.getOrderStatus() == 6) {
                            allMoney = allMoney + Integer.parseInt(model.getMoney());
                        }
                        viewData.add(model);
                    }
                    allMoneyTextView.setText("已成功收钱:" + allMoney + "元");
                    recyclerView.getAdapter().notifyDataSetChanged();
                } else {
                    Log.e("error", e.getMessage() + e.getCode());
                }
            }
        });
    }

    private class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history_recyclerview, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            OrderHistoryModel model = viewData.get(position);
            VH vh = (VH) holder;

            vh.tableNumTextView.setText("桌号：" + model.getTablenum());
            vh.orderNumTextView.setText("订单号：" + model.getOrderNum());
            vh.moneyTextView.setText("价格:" + model.getMoney() + "元");
            vh.payStatusTextView.setText(model.getOrderStatus() == 6 ? "状态：已支付" : "状态：待支付");
        }

        @Override
        public int getItemCount() {
            return viewData.size();
        }


        private class VH extends RecyclerView.ViewHolder {

            private TextView tableNumTextView;
            private TextView orderNumTextView;
            private TextView moneyTextView;
            private TextView payStatusTextView;

            public VH(View itemView) {
                super(itemView);


                moneyTextView = (TextView) itemView.findViewById(R.id.orderMoneyTextView);
                payStatusTextView = (TextView) itemView.findViewById(R.id.payStatusTextView);
                tableNumTextView = (TextView) itemView.findViewById(R.id.tableNumTextView);
                orderNumTextView = (TextView) itemView.findViewById(R.id.orderNumTextView);
            }
        }
    }

    @Override
    protected void unBind() {

    }
}

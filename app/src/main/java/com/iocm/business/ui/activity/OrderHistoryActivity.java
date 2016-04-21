package com.iocm.business.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.google.gson.reflect.TypeToken;
import com.iocm.business.R;
import com.iocm.business.base.BaseActivity;
import com.iocm.business.model.OrderAVModel;
import com.iocm.business.model.OrderItemAVModel;
import com.iocm.business.utils.GsonUtils;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Administrator on 2016/4/21.
 */
public class OrderHistoryActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_order_history);

        recyclerView = (RecyclerView) findViewById(R.id.orderHistoryRecyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.orderHistoryRefreshLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void setListener() {
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
        query.whereNotEqualTo("orderStatus", 3);
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                swipeRefreshLayout.setRefreshing(false);
                if (e == null && list.size() > 0) {
                  //  viewData.clear();
                    for (AVObject object : list) {
                        OrderAVModel model = new OrderAVModel();
                        model.setId(object.getObjectId());
                        model.setTableNum(object.getString("tableNum"));
                        model.setUserId(object.getString("userId"));
                        model.setUsername(object.getString("username"));
                        model.setOrderStatus(object.getNumber("orderStatus").intValue());
                        Type listType = new TypeToken<List<OrderItemAVModel>>() {
                        }.getType();
                        List<OrderItemAVModel> listData = GsonUtils.getInstance().getGson().fromJson(object.getString("menuList"), listType);
                        model.setMenuList(listData);
                 //       viewData.add(model);
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void unBind() {

    }
}

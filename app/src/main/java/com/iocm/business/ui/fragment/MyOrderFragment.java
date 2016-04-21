package com.iocm.business.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.google.gson.reflect.TypeToken;
import com.iocm.business.R;
import com.iocm.business.base.BaseFragment;
import com.iocm.business.model.ItemData;
import com.iocm.business.model.MenuAVModel;
import com.iocm.business.model.OrderAVModel;
import com.iocm.business.model.OrderItemAVModel;
import com.iocm.business.ui.activity.OrderHistoryActivity;
import com.iocm.business.utils.GsonUtils;
import com.iocm.business.utils.ToastUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/18.
 */
public class MyOrderFragment extends BaseFragment {

    private static final int CLIENT_WATI = 1;
    private static final int CLIENT_GET_MENU = 2; //客户得到菜
    private static final int COOK_FINISH_THIS_MENU = 3; //厨师做完一道菜

    private static final int ORDER_BEGIN = 2; // 开始做菜
    private static final int ORDER_FINISH = 3;// 全部做完
    private static final int ORDER_CLIENT_GET_ALL = 4; // 客户得到所有的菜

    private SwipeRefreshLayout refreshLayout;

    private RecyclerView orderRecyclerView;

    private List<OrderAVModel> viewData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_order, container, false);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    protected void initView(View v) {
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.orderRefreshLayout);

        orderRecyclerView = (RecyclerView) v.findViewById(R.id.orderRecyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    protected void bind() {
        getData();
    }

    private void getData() {

        AVQuery<AVObject> query = new AVQuery<>("OrderTable");
        query.whereNotEqualTo("orderStatus", ORDER_CLIENT_GET_ALL);
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                refreshLayout.setRefreshing(false);
                if (e == null && list.size() > 0) {
                    viewData.clear();
                    for (AVObject object : list) {
                        OrderAVModel model = new OrderAVModel();
                        model.setId(object.getObjectId());
                        model.setTableNum(object.getString("tableNum"));
                        model.setUserId(object.getString("userId"));
                        model.setUsername(object.getString("username"));
                        model.setOrderStatus(object.getNumber("orderStatus").intValue());
                        model.setOrderMoney(object.getString("orderMoney"));
                        Type listType = new TypeToken<List<OrderItemAVModel>>() {
                        }.getType();
                        List<OrderItemAVModel> listData = GsonUtils.getInstance().getGson().fromJson(object.getString("menuList"), listType);
                        model.setMenuList(listData);
                        viewData.add(model);
                    }
                    orderRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void setListener() {
        orderRecyclerView.setAdapter(new OrderRVAdapter());
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    @Override
    protected void unbind() {

    }

    class OrderRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new OrderVH(LayoutInflater.from(getActivity()).inflate(R.layout.item_order_recyclerview, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final OrderVH vh = (OrderVH) holder;
            final OrderAVModel model = viewData.get(position);
            vh.tableNumTextView.setText(model.getTableNum() + "号桌");
            vh.orderMoneyTextView.setText("总计：" + model.getOrderMoney() + "元");
            vh.setItemViewData(new ItemData<Integer, List<OrderItemAVModel>>(position, model.getMenuList()));

            if (model.getOrderStatus() == ORDER_BEGIN) {
                vh.confirmButton.setText("已经通知顾客开始做菜了");
                vh.confirmButton.setEnabled(false);
            } else if (model.getOrderStatus() == ORDER_FINISH) {
                vh.tableNumTextView.setText(model.getTableNum() + "号桌的菜 已经做完了。");
                vh.confirmButton.setEnabled(false);
            } else if (model.getOrderStatus() == ORDER_CLIENT_GET_ALL) {
                vh.confirmButton.setText("顾客确认收到所有菜");
                vh.confirmButton.setEnabled(false);
            } else {
                vh.confirmButton.setText("开始做菜");
                vh.confirmButton.setEnabled(true);
            }
            vh.confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AVQuery<AVObject> query = new AVQuery<AVObject>("OrderTable");
                    query.whereEqualTo("objectId", model.getId());
                    query.findInBackground(new FindCallback<AVObject>() {
                        @Override
                        public void done(List<AVObject> list, AVException e) {
                            AVObject object = list.get(0);
                            object.put("orderStatus", ORDER_BEGIN);//开始做菜
                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        vh.confirmButton.setText("已经通知顾客开始做菜了");
                                        vh.confirmButton.setEnabled(false);
                                    }
                                }
                            });
                        }
                    });
                }
            });

        }

        @Override
        public int getItemCount() {
            return viewData.size();
        }

        private class OrderVH extends RecyclerView.ViewHolder {
            private RecyclerView itemOrderRecyclerView;
            private TextView tableNumTextView;
            private Button confirmButton;
            private TextView orderMoneyTextView;

            private ItemData<Integer, List<OrderItemAVModel>> itemViewData = new ItemData<>();

            public ItemData<Integer, List<OrderItemAVModel>> getItemViewData() {

                return itemViewData;
            }

            public void setItemViewData(ItemData<Integer, List<OrderItemAVModel>> itemViewData) {
                this.itemViewData = itemViewData;
                itemOrderRecyclerView.getAdapter().notifyDataSetChanged();
            }

            public OrderVH(View itemView) {
                super(itemView);

                orderMoneyTextView = (TextView) itemView.findViewById(R.id.orderMoneyTextView);
                tableNumTextView = (TextView) itemView.findViewById(R.id.tableNumTextView);
                itemOrderRecyclerView = (RecyclerView) itemView.findViewById(R.id.itemOrderRecyclerView);

                confirmButton = (Button) itemView.findViewById(R.id.itemOrderConfirmButton);

                itemOrderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                itemOrderRecyclerView.setAdapter(new ItemAdapter());
            }


            private class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return new ItemVH(LayoutInflater.from(getActivity()).inflate(R.layout.item_order_recyclerview_item, parent, false));
                }

                @Override
                public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
                    final ItemVH vh = (ItemVH) holder;
                    OrderItemAVModel model = itemViewData.getValue().get(position);

                    vh.name.setText(model.getModel().getName());
                    int status = model.getModel().getMenuStatus();
                    vh.confirmButton.setTag(itemViewData.getKey());

                    if (status == CLIENT_GET_MENU || status == COOK_FINISH_THIS_MENU) {
                        vh.confirmButton.setVisibility(View.GONE);
                    } else {
                        vh.confirmButton.setVisibility(View.VISIBLE);
                    }


                    vh.confirmButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final List<OrderItemAVModel> list1 = itemViewData.getValue();
                            OrderItemAVModel orderItemAVModel = list1.get(position);
                            MenuAVModel menuAVModel = orderItemAVModel.getModel();
                            menuAVModel.setMenuStatus(COOK_FINISH_THIS_MENU); //做好了
                            list1.remove(position);
                            orderItemAVModel.setModel(menuAVModel);
                            list1.add(orderItemAVModel);
//                            setItemViewData(new ItemData<Integer, List<OrderItemAVModel>>(itemViewData.getKey(), list));

                            boolean getALL = true;
                            for (OrderItemAVModel avModel : list1) {
                                if (avModel.getModel().getMenuStatus() == CLIENT_WATI) {
                                    getALL = false;
                                    break;
                                }
                            }

                            int p = (int) vh.confirmButton.getTag();
                            String obId = viewData.get(p).getId();
                            AVQuery<AVObject> query = new AVQuery<AVObject>("OrderTable");
                            query.whereEqualTo("objectId", obId);
                            final boolean finalGetALL = getALL;
                            query.findInBackground(new FindCallback<AVObject>() {
                                @Override
                                public void done(List<AVObject> list, AVException e) {
                                    if (e == null && list.size() > 0) {
                                        AVObject getOb = list.get(0);
                                        getOb.put("menuList", GsonUtils.getInstance().getGson().toJson(list1));
                                        if (finalGetALL) {
                                            getOb.put("orderStatus", ORDER_FINISH);
                                        } else {
                                            getOb.put("orderStatus", ORDER_BEGIN);
                                        }
                                        getOb.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(AVException e) {
                                                if (e == null) {
                                                    vh.confirmButton.setVisibility(View.GONE);
                                                    ToastUtils.showNormalToast("修改成功");
                                                    getData();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });


                }

                @Override
                public int getItemCount() {
                    return itemViewData.getValue().size();
                }

                private class ItemVH extends RecyclerView.ViewHolder {
                    private TextView name;
                    private Button confirmButton;

                    public ItemVH(View itemView) {
                        super(itemView);

                        name = (TextView) itemView.findViewById(R.id.menuNameTextView);
                        confirmButton = (Button) itemView.findViewById(R.id.confirmButton);
                    }
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_order_history, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_order_history) {
            startActivity(new Intent(getActivity(), OrderHistoryActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

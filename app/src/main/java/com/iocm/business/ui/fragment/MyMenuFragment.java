package com.iocm.business.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.iocm.business.R;
import com.iocm.business.base.BaseFragment;
import com.iocm.business.model.MenuModel;
import com.iocm.business.utils.PicassoUtils;
import com.iocm.business.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liubo on 4/16/16.
 */
public class MyMenuFragment extends BaseFragment {


    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private List<MenuModel> viewData = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_menu, container, false);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    protected void initView(View v) {
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.myMenuSwipeRefreshLayout);

        recyclerView = (RecyclerView) v.findViewById(R.id.myMenuRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        refreshLayout.setColorSchemeColors(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
    }

    @Override
    protected void bind() {
        getData();

    }

    private void getData() {
        refreshLayout.setRefreshing(true);
        AVQuery<AVObject> query = new AVQuery<>("MenuTable");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                refreshLayout.setRefreshing(false);
                if (null == e) {
                    if (list.size() != 0) {
                        viewData.clear();

                        for (AVObject object : list) {
                            MenuModel model = new MenuModel();
                            model.setId(object.getObjectId());
                            model.setName(object.getString("name"));
                            model.setDetail(object.getString("detail"));
                            model.setMoney(object.getString("money"));
                            String url = object.getAVFile("picSrc").getUrl();
                            model.setImageSrc(url);
                            model.setHas(object.getBoolean("has"));
                            model.setType(object.getString("type"));
                            model.setTaste(object.getString("taste"));
                            model.setMethod(object.getString("method"));
                            model.setFunction(object.getString("function"));
                            viewData.add(model);
                        }

                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.showNormalToast("请检查您的网络!");
                }
            }
        });
    }

    @Override
    protected void setListener() {
        refreshLayout.setOnRefreshListener(onRefreshListener);
        recyclerView.setAdapter(new RVAdapter());
    }

    @Override
    protected void unbind() {

    }


    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            getData();
        }
    };


    private class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(getActivity()).inflate(R.layout.item_my_menu_recyclerview, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final MenuModel model = viewData.get(position);
            VH vh = (VH) holder;
            vh.itemContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("菜品详情");
                    builder.setView(R.layout.layout_menu_detail);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    TextView detail = (TextView) dialog.findViewById(R.id.menuDetailTextView);
                    TextView name = (TextView) dialog.findViewById(R.id.menuNameTextView);
                    TextView function = (TextView) dialog.findViewById(R.id.menuFunction);
                    TextView menuType = (TextView) dialog.findViewById(R.id.menuTypeTextView);
                    TextView menuMethod = (TextView) dialog.findViewById(R.id.menuMethod);
                    TextView money = (TextView) dialog.findViewById(R.id.menuMoneyTextView);

                    detail.setText("食材：" + model.getDetail());
                    name.setText("菜名：" + model.getName());
                    function.setText("营养价值：" + model.getFunction());
                    menuType.setText("菜系：" + model.getType());
                    menuMethod.setText("做法：" + model.getMethod());
                    money.setText("价格：" + model.getMoney() + "元/份");
                }
            });

            vh.nameTextView.setText(model.getName());
            vh.detailTextView.setText(model.getDetail());
            vh.moneyTextView.setText("价格:" + model.getMoney() + "元");
            PicassoUtils.normalShowImage(getActivity(), model.getImageSrc(), vh.menuImageView);

        }

        @Override
        public int getItemCount() {
            return viewData.size();
        }


        private class VH extends RecyclerView.ViewHolder {

            private LinearLayout itemContent;
            private ImageView menuImageView;
            private TextView nameTextView;
            private TextView detailTextView;
            private TextView moneyTextView;

            public VH(View itemView) {
                super(itemView);

                itemContent = (LinearLayout) itemView.findViewById(R.id.itemMyMenuContent);

                moneyTextView = (TextView) itemView.findViewById(R.id.itemMenuMoney);
                menuImageView = (ImageView) itemView.findViewById(R.id.itemMenuImageView);
                nameTextView = (TextView) itemView.findViewById(R.id.itemMenuName);
                detailTextView = (TextView) itemView.findViewById(R.id.itemMenuDetail);
            }
        }
    }


}

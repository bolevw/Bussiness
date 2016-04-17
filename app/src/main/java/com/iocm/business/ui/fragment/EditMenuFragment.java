package com.iocm.business.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.iocm.business.R;
import com.iocm.business.base.BaseFragment;
import com.iocm.business.ui.activity.AddMenuActivity;

/**
 * Created by liubo on 4/16/16.
 */
public class EditMenuFragment extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_menu, container, false);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    protected void initView(View v) {

    }

    @Override
    protected void bind() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void unbind() {

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.eidt_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_menu) {
            startActivity(new Intent(getActivity(), AddMenuActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

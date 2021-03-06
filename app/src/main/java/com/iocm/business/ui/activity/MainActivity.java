package com.iocm.business.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.avos.avoscloud.AVUser;
import com.iocm.business.R;
import com.iocm.business.base.BaseActivity;
import com.iocm.business.model.NameValue;
import com.iocm.business.ui.fragment.EditMenuFragment;
import com.iocm.business.ui.fragment.MyMenuFragment;
import com.iocm.business.ui.fragment.MyOrderFragment;
import com.iocm.business.utils.FragmentUtils;
import com.iocm.business.utils.SharedPreferencesUtil;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    Toolbar toolbar;
    private SharedPreferencesUtil preferencesUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferencesUtil = SharedPreferencesUtil.getInstance(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentUtils.replaceFragment(getSupportFragmentManager(), R.id.fragmentContainer, new MyMenuFragment(), false, "MyOrderFragment");
        navigationView.setCheckedItem(R.id.nav_camera);

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void bind() {

    }

    @Override
    protected void unBind() {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            FragmentUtils.replaceFragment(getSupportFragmentManager(), R.id.fragmentContainer, new MyMenuFragment(), false, "MyMenuFragment");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_gallery) {
            FragmentUtils.replaceFragment(getSupportFragmentManager(), R.id.fragmentContainer, new EditMenuFragment(), false, "EditMenuFragment");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_slideshow) {
            FragmentUtils.replaceFragment(getSupportFragmentManager(), R.id.fragmentContainer, new MyOrderFragment(), false, "MyOrderFragment");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_manage) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("是否退出？");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    preferencesUtil.saveValue(new NameValue("login", false));
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    AVUser.getCurrentUser().logOut();
                    finish();
                }
            });
            builder.create().show();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }
        return true;
    }
}

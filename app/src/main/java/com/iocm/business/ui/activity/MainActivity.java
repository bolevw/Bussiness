package com.iocm.business.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.iocm.business.R;
import com.iocm.business.base.BaseActivity;
import com.iocm.business.ui.fragment.EditMenuFragment;
import com.iocm.business.ui.fragment.MyMenuFragment;
import com.iocm.business.ui.fragment.MyOrderFragment;
import com.iocm.business.utils.FragmentUtils;
import com.iocm.business.utils.ToastUtils;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        } else if (id == R.id.nav_gallery) {
            FragmentUtils.replaceFragment(getSupportFragmentManager(), R.id.fragmentContainer, new EditMenuFragment(), false, "EditMenuFragment");
        } else if (id == R.id.nav_slideshow) {
            FragmentUtils.replaceFragment(getSupportFragmentManager(), R.id.fragmentContainer, new MyOrderFragment(), false, "MyOrderFragment");
        } else if (id == R.id.nav_manage) {
            ToastUtils.showNormalToast("log out");
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

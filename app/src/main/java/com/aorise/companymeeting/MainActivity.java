package com.aorise.companymeeting;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.aorise.companymeeting.adapter.GridRecycleAdapter;
import com.aorise.companymeeting.adapter.GridRecycleAdapterItemClick;
import com.aorise.companymeeting.base.MeettingRomItem;
import com.aorise.companymeeting.base.MeettingRomMode;
import com.aorise.companymeeting.base.MeettingRomViewModel;
import com.aorise.companymeeting.databinding.ActivityMainBinding;
import com.hjq.toast.ToastUtils;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BaseRefreshListener, GridRecycleAdapterItemClick {
    private ActivityMainBinding activityMainBinding;

    private ArrayList<MeettingRomItem> mList;

    private long[] mHints = new long[2];
    private static final long EXIT_INTERVAL = 2000L;
    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,};
    List<String> mPermissionList = new ArrayList<>();
    private int State;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT > 23) {
            mPermissionList.clear();//清空没有通过的权限
            for (int i = 0; i < permissions.length; i++) {
                if (ActivityCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);//添加还未授予的权限
                }
            }

            //申请权限
            if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
                ActivityCompat.requestPermissions(this, permissions, 2040);
            } else {
                //说明权限都已经通过，可以做你想做的事情去
            }
        }

        activityMainBinding.appbarMain.contentMain.pltrMeeting.setRefreshListener(this);
        mList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            mList.add(new MeettingRomItem("" + i, "奥昇二楼" + i, i * 3, i % 2 == 0));
        }

        activityMainBinding.appbarMain.contentMain.meetingList.setLayoutManager(new LinearLayoutManager(this));
        activityMainBinding.appbarMain.contentMain.meetingList.setAdapter(new GridRecycleAdapter(this, mList, this));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            System.arraycopy(mHints, 1, mHints, 0, mHints.length - 1);
            mHints[mHints.length - 1] = SystemClock.uptimeMillis();
            if ((SystemClock.uptimeMillis() - mHints[0]) > EXIT_INTERVAL) {
                ToastUtils.show("再按一次返回键退出!");
            } else {
                super.onBackPressed();
            }

        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.meeting_order:
                setState(MeettingRomMode.MODE_ORDER);
                break;
            case R.id.meeting_register:
                setState(MeettingRomMode.MODE_REGISTER);
                break;
            case R.id.meeting_status:
                setState(MeettingRomMode.MODE_STATUS);
                break;
            case R.id.meeting_mananger:
                setState(MeettingRomMode.MODE_SAFEGUARD);
                break;
            case R.id.meeting_clean:
                setState(MeettingRomMode.MODE_CLEAN);
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void refresh() {
        activityMainBinding.appbarMain.contentMain.pltrMeeting.finishRefresh();
    }

    @Override
    public void loadMore() {
        activityMainBinding.appbarMain.contentMain.pltrMeeting.finishLoadMore();
    }

    private void setState(int state) {
        State = state;
    }

    private int getState() {
        return State;
    }

    @Override
    public void GridRecycleItemClick(int position) {
        ToastUtils.show("您点击了" + (position + 1));
        Intent mIntent = new Intent();
        mIntent.setClass(this,CalendarChooseActivity.class);
        startActivity(mIntent);
    }
}

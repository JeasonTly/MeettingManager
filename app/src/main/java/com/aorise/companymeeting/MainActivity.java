package com.aorise.companymeeting;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aorise.companymeeting.adapter.GridRecycleAdapter;
import com.aorise.companymeeting.adapter.GridRecycleAdapterItemClick;
import com.aorise.companymeeting.base.LogT;
import com.aorise.companymeeting.base.MeettingContent;
import com.aorise.companymeeting.base.MeettingRomItem;
import com.aorise.companymeeting.base.MeettingRomMode;
import com.aorise.companymeeting.base.TimeAreaUtil;
import com.aorise.companymeeting.databinding.ActivityMainBinding;
import com.aorise.companymeeting.sqlite.DatabaseHelper;
import com.hjq.toast.ToastUtils;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements BaseRefreshListener, GridRecycleAdapterItemClick {
    private ActivityMainBinding activityMainBinding;

    private ArrayList<MeettingRomItem> mList = new ArrayList<>();

    private long[] mHints = new long[2];
    private static final long EXIT_INTERVAL = 2000L;
    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,};
    List<String> mPermissionList = new ArrayList<>();
    private DatabaseHelper mDatabaseHelper;
    private GridRecycleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDatabaseHelper = new DatabaseHelper(this);

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

        activityMainBinding.appbarMain.contentMain.meetingList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GridRecycleAdapter(this, mList, this);
        activityMainBinding.appbarMain.contentMain.meetingList.setAdapter(mAdapter);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_meetting_room:
                final View content_view = LayoutInflater.from(this).inflate(R.layout.add_meetting_dialog, null);
                new AlertDialog.Builder(this).setTitle("添加会议室")
                        .setView(content_view)
                        .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MeettingRomItem meettingRomItem = new MeettingRomItem();
                                EditText editText = content_view.findViewById(R.id.add_room_name);
                                if (TextUtils.isEmpty(editText.getText().toString())) {
                                    ToastUtils.show("会议室名称不能为空");
                                    return;
                                }
                                for (MeettingRomItem data : mList) {
                                    if (data.getName().equals(editText.getText().toString())) {
                                        ToastUtils.show("会议室名称冲突，请重新输入");
                                        editText.setText("");
                                        return;
                                    }
                                }
                                meettingRomItem.setName(editText.getText().toString());
                                meettingRomItem.setStatus(0);
                                meettingRomItem.setTodo_Count(0);
                                mList.add(meettingRomItem);
                                mDatabaseHelper.insertRoom(meettingRomItem);
                                mAdapter.refreshData(mList);
                                dialog.dismiss();
                            }
                        }).create().show();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        System.arraycopy(mHints, 1, mHints, 0, mHints.length - 1);
        mHints[mHints.length - 1] = SystemClock.uptimeMillis();
        if ((SystemClock.uptimeMillis() - mHints[0]) > EXIT_INTERVAL) {
            ToastUtils.show("再按一次返回键退出!");
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mList = mDatabaseHelper.getList();
        if (mList != null) {
            Date date = new Date();
            SimpleDateFormat sm = new SimpleDateFormat("yyyy:MM:dd-HH:mm");
            for (MeettingRomItem data : mList) {
                List<MeettingContent> meettingContentList = DatabaseHelper.getInstance(this).queryAllDayofMeetting(data.getName());
                int status = TimeAreaUtil.getInstance().getMeettingRoomStatus(meettingContentList,sm.format(date));
                LogT.d("RRoomstatus is "+status);
                data.setStatus(status);
                data.setTodo_Count(meettingContentList.size());
            }
            mAdapter.refreshData(mList);
        }

    }

    @Override
    public void refresh() {
        Date date = new Date();
        SimpleDateFormat sm = new SimpleDateFormat("yyyy:MM:dd-HH:mm");
        for (MeettingRomItem data : mList) {
            List<MeettingContent> meettingContentList = DatabaseHelper.getInstance(this).queryAllDayofMeetting(data.getName());
            int status = TimeAreaUtil.getInstance().getMeettingRoomStatus(meettingContentList,sm.format(date));
            data.setStatus(status);
            data.setTodo_Count(meettingContentList.size());
        }
        mAdapter.refreshData(mList);
        activityMainBinding.appbarMain.contentMain.pltrMeeting.finishRefresh();
    }

    @Override
    public void loadMore() {
        activityMainBinding.appbarMain.contentMain.pltrMeeting.finishLoadMore();
    }

    @Override
    public void GridRecycleItemClick(int position) {
        Intent mIntent = new Intent();
        mIntent.putExtra("room_name", mList.get(position).getName());
        mIntent.setClass(this, CalendarChooseActivity.class);
        startActivityForResult(mIntent, 2509);
    }

    @Override
    public void GridRecycleItemLongClick(final String name) {
        View view = LayoutInflater.from(this).inflate(R.layout.delete_warning,null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("警告!")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper.getInstance(MainActivity.this).deleteRoom(name);
                        mList = DatabaseHelper.getInstance(MainActivity.this).getList();
                        mAdapter.refreshData(mList);
                        ToastUtils.show("会议室删除成功,会议室相应的内容亦已删除!");
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(false).create();
        dialog.show();
    }

}

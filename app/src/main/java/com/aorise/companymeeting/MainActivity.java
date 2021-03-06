package com.aorise.companymeeting;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.aorise.companymeeting.adapter.BaseAdapter;
import com.aorise.companymeeting.adapter.DepartmentRecycleListAdapter;
import com.aorise.companymeeting.adapter.RoomRecycleListAdapter;
import com.aorise.companymeeting.adapter.RoomRecycleListAdapterClick;
import com.aorise.companymeeting.base.DepartmentInfo;
import com.aorise.companymeeting.base.LogT;
import com.aorise.companymeeting.base.MeettingInfo;
import com.aorise.companymeeting.base.MeettingRomItem;
import com.aorise.companymeeting.base.SpacesItemDecoration;
import com.aorise.companymeeting.base.TimeAreaUtil;
import com.aorise.companymeeting.databinding.ActivityMainBinding;
import com.aorise.companymeeting.databinding.DialogDepartDetailInfoBinding;
import com.aorise.companymeeting.sqlite.DatabaseHelper;
import com.hjq.toast.ToastUtils;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements BaseRefreshListener, RoomRecycleListAdapterClick {
    private ActivityMainBinding activityMainBinding;

    private ArrayList<MeettingRomItem> mList = new ArrayList<>();
    private ArrayList<DepartmentInfo> mDepartList = new ArrayList<>();
    /**
     * 按键退出
     */
    private long[] mHints = new long[2];
    private static final long EXIT_INTERVAL = 2000L;
    /**
     * 权限
     */
    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,};
    List<String> mPermissionList = new ArrayList<>();

    private DatabaseHelper mDatabaseHelper;
    private RoomRecycleListAdapter mAdapter;
    private DepartmentRecycleListAdapter mDepartAdapter;
    private String depart_dialog_room_name = "";
    private MenuItem menuItem;
    private boolean isDepartmentMode = false;

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


        /**
         * 会议室添加
         */

        mAdapter = new RoomRecycleListAdapter(this, mList, this);
        activityMainBinding.appbarMain.contentMain.pltrMeeting.setRefreshListener(this);
        activityMainBinding.appbarMain.contentMain.meetingList.setLayoutManager(new LinearLayoutManager(this));
        activityMainBinding.appbarMain.contentMain.meetingList.setAdapter(mAdapter);

        /**
         * 部门添加
         */

        mDepartAdapter = new DepartmentRecycleListAdapter(this, mDepartList, this);
        activityMainBinding.appbarMain.contentMain.dpartmentList.setLayoutManager(new LinearLayoutManager(this));
        activityMainBinding.appbarMain.contentMain.dpartmentList.addItemDecoration(new SpacesItemDecoration(8));
        activityMainBinding.appbarMain.contentMain.dpartmentList.setAdapter(mDepartAdapter);
        activityMainBinding.appbarMain.contentMain.pltrMeetingDepart.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                modifyDepartmentStatus();
                //  DatabaseHelper.getInstance(MainActivity.this).
                activityMainBinding.appbarMain.contentMain.pltrMeetingDepart.finishRefresh();

            }

            @Override
            public void loadMore() {
                activityMainBinding.appbarMain.contentMain.pltrMeetingDepart.finishLoadMore();
            }
        });

        activityMainBinding.appbarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDepartmentMode = !isDepartmentMode;
                menuItem.setTitle(isDepartmentMode ? "新建部门" : "新建会议室");
                activityMainBinding.appbarMain.contentMain.pltrMeeting.setVisibility(isDepartmentMode ? View.GONE : View.VISIBLE);
                activityMainBinding.appbarMain.contentMain.pltrMeetingDepart.setVisibility(isDepartmentMode ? View.VISIBLE : View.GONE);
                if (isDepartmentMode) {
                    modifyDepartmentStatus();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_meetting_room:
                if (isDepartmentMode) {
                    createNewDepartment();
                } else {
                    createNewRoom();
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        menuItem = menu.findItem(R.id.add_meetting_room);
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
        if (isDepartmentMode) {
            modifyDepartmentStatus();
        } else {
            mList = mDatabaseHelper.getList();
            if (mList != null) {
                for (MeettingRomItem data : mList) {
                    List<MeettingInfo> meettingContentList = DatabaseHelper.getInstance(this).queryAllDayofMeetting(data.getName());
                    int status = TimeAreaUtil.getInstance().getMeettingRoomStatus(meettingContentList);
                    data.setStatus(status);
                    data.setTodo_Count(meettingContentList.size());
                }
                mAdapter.refreshData(mList);
            }
        }
    }

    @Override
    public void refresh() {

        for (MeettingRomItem data : mList) {
            List<MeettingInfo> meettingContentList = DatabaseHelper.getInstance(this).queryAllDayofMeetting(data.getName());
            int status = TimeAreaUtil.getInstance().getMeettingRoomStatus(meettingContentList);
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

    /**
     * 会议室和部门的点击事件!
     *
     * @param position
     */
    @Override
    public void GridRecycleItemClick(int position) {
        if (isDepartmentMode) {
            showDepartmentInfo(position);
        } else {
            Intent mIntent = new Intent();
            mIntent.putExtra("room_name", mList.get(position).getName());
            mIntent.setClass(this, CalendarChooseActivity.class);
            startActivityForResult(mIntent, 2509);
        }
    }

    /**
     * 长按删除对应的会议室或者部门
     *
     * @param name
     */
    @Override
    public void GridRecycleItemLongClick(final String name) {

        if (isDepartmentMode) {
            View view = LayoutInflater.from(this).inflate(R.layout.delete_warning, null);
            TextView textView = view.findViewById(R.id.delete_content);
            textView.setText("您确定要删除此部门吗?此部门相关的所有会议也将被删除!请确认!");
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("警告!")
                    .setView(view)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseHelper.getInstance(MainActivity.this).deletDepartment(name);
                            mDepartList = DatabaseHelper.getInstance(MainActivity.this).getDepartList();
                            mDepartAdapter.refreshData(mDepartList);
                            ToastUtils.show("部门删除成功,部门会议亦已删除!");
                            dialog.dismiss();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setCancelable(false).create();
            dialog.show();
        } else {
            View view = LayoutInflater.from(this).inflate(R.layout.delete_warning, null);
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

    /**
     * 更新会议室状态
     */
    private void modifyDepartmentStatus() {
        mDepartList = DatabaseHelper.getInstance(this).getDepartList();
        Date date = new Date();
        SimpleDateFormat sm = new SimpleDateFormat("yyyy年MM月dd日HH:mm");

        if (mDepartList != null && mDepartList.size() != 0) {
            for (DepartmentInfo departmentInfo : mDepartList) {//查询所有的
                List<MeettingInfo> meettingInfos = DatabaseHelper.getInstance(this).queryDepartmentMeettingListByName(departmentInfo.getName());
                MeettingInfo meettingInfo = TimeAreaUtil.getInstance().getMeettingInfo(meettingInfos);
                boolean isInMeeetting = TimeAreaUtil.getInstance().getMeettingRoomStatus(meettingInfos) == 1;

                LogT.d(" isInMeetting " + isInMeeetting);
                if (isInMeeetting) {
                    departmentInfo.setRoom_name(meettingInfo.getRoomName());
                }
                departmentInfo.setInTheMeetting(isInMeeetting);
            }
        }
        mDepartAdapter.refreshData(mDepartList);
    }

    /**
     * 添加新的会议室
     */
    private void createNewRoom() {
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
    }

    /**
     * 添加新的部门
     */
    private void createNewDepartment() {
        final View content_view = LayoutInflater.from(this).inflate(R.layout.add_meetting_dialog, null);
        TextView textView = content_view.findViewById(R.id.textView3);
        textView.setText("部门名称:");
        final EditText editText = content_view.findViewById(R.id.add_room_name);
        editText.setHint("请输入部门名称");
        new AlertDialog.Builder(this).setTitle("添加部门")
                .setView(content_view)
                .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DepartmentInfo departmentInfo = new DepartmentInfo();
                        if (TextUtils.isEmpty(editText.getText().toString())) {
                            ToastUtils.show("部门名称不能为空");
                            return;
                        }
                        for (DepartmentInfo data : mDepartList) {
                            if (data.getName().equals(editText.getText().toString())) {
                                ToastUtils.show("部门名称冲突，请重新输入");
                                editText.setText("");
                                return;
                            }
                        }
                        departmentInfo.setName(editText.getText().toString());
                        departmentInfo.setInTheMeetting(false);
                        departmentInfo.setRoom_name("无");
                        departmentInfo.setMeetting_content("");
                        mDepartList.add(departmentInfo);
                        LogT.d(" mDepartList size is  " + mDepartList.size() + " departmentInfo is " + departmentInfo);
                        mDatabaseHelper.insertDepartment(departmentInfo);
                        mDepartAdapter.refreshData(mDepartList);
                        dialog.dismiss();
                    }
                }).create().show();
    }

    /**
     * 展示部门点击事件
     *
     * @param position //点击的部门名称
     */
    private void showDepartmentInfo(int position) {
        DialogDepartDetailInfoBinding mView = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_depart_detail_info, null, false);
        final List<MeettingInfo> meettingInfos = DatabaseHelper.getInstance(this).queryDepartmentMeettingListByName(mDepartList.get(position).getName());
        for (MeettingInfo meettingInfo : meettingInfos) {
            if (!depart_dialog_room_name.contains(meettingInfo.getRoomName())) {
                if (depart_dialog_room_name.equals("")) {
                    depart_dialog_room_name = meettingInfo.getRoomName();
                } else {
                    depart_dialog_room_name = depart_dialog_room_name + "、" + meettingInfo.getRoomName();
                }
            }
        }
        LogT.d("and now depart_dialog_room_name is " + depart_dialog_room_name);
        final MeettingInfo meettingInfo = TimeAreaUtil.getInstance().getMeettingInfo(meettingInfos);
        boolean isInMeeetting = TimeAreaUtil.getInstance().getMeettingRoomStatus(meettingInfos) == 1;
        MeettingInfo nextMeetting = TimeAreaUtil.getInstance().getNextMeettingInfo(meettingInfos);
        mView.departDetailName.setText(mDepartList.get(position).getName());
        mView.departDetailStatus.setText(isInMeeetting ? "会议中" : "无会议");
        mView.departDetailRoomList.setText(depart_dialog_room_name);
        mView.departDetailRoomName.setText(meettingInfo == null ? "无" : meettingInfo.getRoomName());
        mView.departDetailNextMeetting.setText(nextMeetting == null ? "无" : (nextMeetting.getChooseDate() + nextMeetting.getStart_time()));

        mView.departDetailList.setLayoutManager(new LinearLayoutManager(this));
        mView.departDetailList.addItemDecoration(new SpacesItemDecoration(4));
        mView.departDetailList.setAdapter(new departInfoDialogAdapter(this, meettingInfos));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("部门会议信息")
                .setView(mView.getRoot())
                .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setNegativeButton(null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(true).create();
        dialog.show();
    }

    private class departInfoDialogVH extends RecyclerView.ViewHolder {

        public departInfoDialogVH(@NonNull View itemView) {
            super(itemView);
        }
    }

    private class departInfoDialogAdapter extends RecyclerView.Adapter<departInfoDialogVH> {
        private List<MeettingInfo> mList;
        private Context mContext;

        public departInfoDialogAdapter(Context context, List<MeettingInfo> meettingInfos) {
            mList = meettingInfos;
            mContext = context;
        }

        @NonNull
        @Override
        public departInfoDialogVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            TextView textView = new TextView(mContext);
            return new departInfoDialogVH(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull departInfoDialogVH departInfoDialogVH, int i) {
            TextView textView = (TextView) departInfoDialogVH.itemView;
            textView.setText("会议信息:" + mList.get(i).getRoomName() + " " + mList.get(i).getChooseDate() + mList.get(i).getStart_time());
            LogT.d("获取到的内容是 " + textView.getText());
        }

        @Override
        public int getItemCount() {
            LogT.d("dddddd " + mList.size());
            return mList.size();
        }
    }
}

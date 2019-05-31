package com.aorise.companymeeting.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aorise.companymeeting.R;
import com.aorise.companymeeting.base.LogT;
import com.aorise.companymeeting.base.MeettingContent;
import com.aorise.companymeeting.base.MeettingRomItem;
import com.aorise.companymeeting.base.TimeAreaUtil;
import com.aorise.companymeeting.databinding.MeetingItemBinding;
import com.aorise.companymeeting.sqlite.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/5/17.
 */
public class GridRecycleAdapter extends BaseAdapter<MeettingRomItem, GridRecycleAdapter.BaseViewHolder> {
    private MeetingItemBinding dataBinding;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private GridRecycleAdapterItemClick click;
    private DatabaseHelper mDbHelper;
    private Date date;
    private SimpleDateFormat dateFormat;

    public GridRecycleAdapter(Context context, List<MeettingRomItem> list, GridRecycleAdapterItemClick itemClick) {
        super(context);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = list;
        this.click = itemClick;
        mContext = context;
        mDbHelper = new DatabaseHelper(context);
        date = new Date();
        dateFormat = new SimpleDateFormat("yyyy:MM:dd-HH:mm");
    }


    @Override
    public GridRecycleAdapter.BaseViewHolder onCreateVH(ViewGroup parent, int viewType) {
        dataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.meeting_item, null, false);
        return new BaseViewHolder(dataBinding.getRoot());
    }

    @Override
    public void onBindVH(GridRecycleAdapter.BaseViewHolder viewHolder, final int position) {
        TextView todocount = (TextView) viewHolder.itemView.findViewById(R.id.todo_count);
        TextView inuse = (TextView) viewHolder.itemView.findViewById(R.id.meet_room_status);
        TextView name = (TextView) viewHolder.itemView.findViewById(R.id.meetting_name);
        name.setText(mContext.getString(R.string.meetting_item, mList.get(position).getName()));
        List<MeettingContent> meettingContentList = mDbHelper.queryDayofMeetting(mList.get(position).getName());
        int status = TimeAreaUtil.getInstance().getMeettingRoomStatus(meettingContentList, dateFormat.format(date));
        LogT.d(" status is " + status);
        switch (mList.get(position).getStatus()) {
            case 0:
                inuse.setText("未使用");
                break;
            case 1:
                inuse.setText("使用中");
                break;
        }

        todocount.setText(String.valueOf(mList.get(position).getTodo_Count()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.GridRecycleItemClick(position);
            }
        });
    }


    class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
//
//    public void reloadList(ArrayList<MeettingRomItem> list) {
//        this.mList.clear();
//        this.mList = list;
//        notifyDataSetChanged();
//    }
//
//    public void insertList(ArrayList<MeettingRomItem> addlist) {
//        this.mList.addAll(addlist);
//        notifyDataSetChanged();
//    }
}

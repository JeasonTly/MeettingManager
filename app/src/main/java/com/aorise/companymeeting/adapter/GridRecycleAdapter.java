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
import com.haibin.calendarview.Calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    public GridRecycleAdapter(Context context, List<MeettingRomItem> list, GridRecycleAdapterItemClick itemClick) {
        super(context);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = list;
        this.click = itemClick;
        mContext = context;
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
        todocount.setText(mContext.getString(R.string.meetting_item_todo, String.valueOf(mList.get(position).getTodo_Count())));
        int status = mList.get(position).getStatus();
        inuse.setText(status == 1 ? "会议室使用中" : "会议室未使用");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.GridRecycleItemClick(position);
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                click.GridRecycleItemLongClick(mList.get(position).getName());
                return true;
            }
        });
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}

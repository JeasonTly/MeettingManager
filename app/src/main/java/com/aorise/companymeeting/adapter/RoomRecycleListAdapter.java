package com.aorise.companymeeting.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aorise.companymeeting.R;
import com.aorise.companymeeting.base.MeettingRomItem;
import com.aorise.companymeeting.databinding.MeetingItemBinding;

import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/5/17.
 */
public class RoomRecycleListAdapter extends BaseAdapter<MeettingRomItem, RoomRecycleListAdapter.BaseViewHolder> {
    private MeetingItemBinding dataBinding;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private RoomRecycleListAdapterClick click;

    public RoomRecycleListAdapter(Context context, List<MeettingRomItem> list, RoomRecycleListAdapterClick itemClick) {
        super(context);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = list;
        this.click = itemClick;
        mContext = context;
    }

    @Override
    public RoomRecycleListAdapter.BaseViewHolder onCreateVH(ViewGroup parent, int viewType) {
        dataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.meeting_item, null, false);
        return new BaseViewHolder(dataBinding.getRoot());
    }

    @Override
    public void onBindVH(RoomRecycleListAdapter.BaseViewHolder viewHolder, final int position) {
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

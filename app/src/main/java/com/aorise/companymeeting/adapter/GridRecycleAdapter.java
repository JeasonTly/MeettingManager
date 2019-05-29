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
import com.aorise.companymeeting.base.MeettingRomItem;
import com.aorise.companymeeting.databinding.MeetingItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/5/17.
 */
public class GridRecycleAdapter extends RecyclerView.Adapter<GridRecycleAdapter.BaseViewHolder> {
    private MeetingItemBinding dataBinding;
    private List<MeettingRomItem> list;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private GridRecycleAdapterItemClick click;
    public GridRecycleAdapter(Context context, List<MeettingRomItem> list,GridRecycleAdapterItemClick itemClick) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
        this.click = itemClick;
        mContext = context;
    }

    @NonNull
    @Override
    public GridRecycleAdapter.BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        dataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.meeting_item, null, false);
        return new BaseViewHolder(dataBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull GridRecycleAdapter.BaseViewHolder viewHolder, final int position) {
        TextView todocount = (TextView) viewHolder.itemView.findViewById(R.id.todo_count);
        TextView inuse = (TextView) viewHolder.itemView.findViewById(R.id.meet_room_status);
        TextView name = (TextView) viewHolder.itemView.findViewById(R.id.meetting_name);
        name.setText(mContext.getString(R.string.meetting_item, list.get(position).getName(), list.get(position).getLocation()));
        inuse.setText(list.get(position).isInUse() ? "使用中" : "未使用");
        Log.d("GridRecycleAdapter", "" + list.get(position).getTodo_Count());
        todocount.setText(String.valueOf(list.get(position).getTodo_Count()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.GridRecycleItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void reloadList(ArrayList<MeettingRomItem> list) {
        this.list.clear();
        this.list = list;
        notifyDataSetChanged();
    }

    public void insertList(ArrayList<MeettingRomItem> addlist) {
        this.list.addAll(addlist);
        notifyDataSetChanged();
    }
}

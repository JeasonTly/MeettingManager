package com.aorise.companymeeting.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aorise.companymeeting.R;
import com.aorise.companymeeting.base.DepartmentInfo;
import com.aorise.companymeeting.base.LogT;
import com.aorise.companymeeting.databinding.ListDepartmentItemBinding;

import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/6/10.
 */
public class DepartmentRecycleListAdapter extends BaseAdapter<DepartmentInfo, DepartmentRecycleListAdapter.DepartmentVH> {
    private ListDepartmentItemBinding mViewDatabinding;
    private RoomRecycleListAdapterClick mListener;

    public DepartmentRecycleListAdapter(Context context, List<DepartmentInfo> list, RoomRecycleListAdapterClick roomRecycleListAdapterClick) {
        super(context);
        this.mList = list;
        this.mListener = roomRecycleListAdapterClick;
    }

    @Override
    public DepartmentVH onCreateVH(ViewGroup parent, int viewType) {
        mViewDatabinding = DataBindingUtil.inflate(inflater, R.layout.list_department_item, parent, false);///我也没反应过来为什么？
        return new DepartmentVH(mViewDatabinding);
    }

    @Override
    public void onBindVH(DepartmentVH viewHolder, final int position) {
        TextView name = (TextView) viewHolder.itemView.findViewById(R.id.depart_name);
        TextView status = (TextView) viewHolder.itemView.findViewById(R.id.status);
        DepartmentInfo departmentInfo =  mList.get(position);
        name.setText(departmentInfo.getName());
        status.setText(departmentInfo.isInTheMeetting() ? "会议中" : "无会议");
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.GridRecycleItemLongClick(mList.get(position).getName());
                return true;
            }
        });
    }

    class DepartmentVH extends BaseViewHolder {

        public DepartmentVH(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
        }
    }
}

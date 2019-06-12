package com.aorise.companymeeting.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aorise.companymeeting.R;
import com.aorise.companymeeting.base.LogT;
import com.aorise.companymeeting.base.MeettingInfo;

import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/5/28.
 */
public class MeettingContentAdapter extends BaseAdapter<MeettingInfo, BaseViewHolder> {
    private MeettingContentItemDelete contentItemDelete;

    public MeettingContentAdapter(Context context, List<MeettingInfo> contentList, MeettingContentItemDelete contentItemDelete) {
        super(context);
        this.mList = contentList;
        this.contentItemDelete = contentItemDelete;
    }

    @Override
    public BaseViewHolder onCreateVH(ViewGroup parent, int viewType) {
        ViewDataBinding view = DataBindingUtil.inflate(inflater, R.layout.meetting_content_list_item, null, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindVH(BaseViewHolder viewHolder, final int position) {
        TextView department = (TextView) viewHolder.itemView.findViewById(R.id.room_dpartment);
        TextView startTime = (TextView) viewHolder.itemView.findViewById(R.id.start_list_time);
        TextView endTime = (TextView) viewHolder.itemView.findViewById(R.id.end_list_time);
        TextView content = (TextView) viewHolder.itemView.findViewById(R.id.meetting_item_content);
        final MeettingInfo data = mList.get(position);
        department.setText(data.getDepartmentInfo());
        startTime.setText(String2Date(data.getStart_time()));
        endTime.setText(String2Date(data.getEnd_time()));
        content.setText(data.getContent());
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                contentItemDelete.onItemLongClick(data);
                return true;
            }
        });
    }

    public interface MeettingContentItemDelete {

        void onItemLongClick(MeettingInfo content);
    }

    public String String2Date(String date) {
        LogT.d(" String2Date " + date);
        String hour = date.substring(0, 2);
        String minutes = date.substring(3, 5);
        return hour + "时" + minutes + "分";
    }
}

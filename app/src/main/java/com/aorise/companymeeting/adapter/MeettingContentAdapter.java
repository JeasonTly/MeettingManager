package com.aorise.companymeeting.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aorise.companymeeting.R;
import com.aorise.companymeeting.base.MeettingContent;

import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/5/28.
 */
public class MeettingContentAdapter extends BaseAdapter<MeettingContent, BaseViewHolder> {

    public MeettingContentAdapter(Context context, List<MeettingContent> contentList) {
        super(context);
        this.mList = contentList;
    }

    @Override
    public BaseViewHolder onCreateVH(ViewGroup parent, int viewType) {
        ViewDataBinding view = DataBindingUtil.inflate(inflater, R.layout.meetting_content_list_item, null, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindVH(BaseViewHolder viewHolder, int position) {
        TextView startDate = (TextView) viewHolder.itemView.findViewById(R.id.meetting_date);
        TextView startTime = (TextView) viewHolder.itemView.findViewById(R.id.start_list_time);
        TextView endTime = (TextView) viewHolder.itemView.findViewById(R.id.end_list_time);
        TextView content = (TextView) viewHolder.itemView.findViewById(R.id.meetting_item_content);
        MeettingContent data = mList.get(position);
        startDate.setText(data.getStart_year() + "年" + data.getStart_month() + "月" + data.getStart_day() + "日");
        startTime.setText(data.getStart_hour()+"时"+data.getStart_minutes() +"分");
        endTime.setText(data.getEnd_hour()+"时"+data.getEnd_minutes() +"分");
        content.setText(data.getContent());
    }

}

package com.aorise.companymeeting.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aorise.companymeeting.MainActivity;
import com.aorise.companymeeting.R;
import com.aorise.companymeeting.base.MeettingContent;
import com.aorise.companymeeting.sqlite.DatabaseHelper;

import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/5/28.
 */
public class MeettingContentAdapter extends BaseAdapter<MeettingContent, BaseViewHolder> {
    private MeettingContentItemDelete contentItemDelete;
    public MeettingContentAdapter(Context context, List<MeettingContent> contentList ,MeettingContentItemDelete contentItemDelete) {
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
        TextView startDate = (TextView) viewHolder.itemView.findViewById(R.id.meetting_date);
        TextView startTime = (TextView) viewHolder.itemView.findViewById(R.id.start_list_time);
        TextView endTime = (TextView) viewHolder.itemView.findViewById(R.id.end_list_time);
        TextView content = (TextView) viewHolder.itemView.findViewById(R.id.meetting_item_content);
        final MeettingContent data = mList.get(position);
        startDate.setText(data.getStart_year() + "年" + data.getStart_month() + "月" + data.getStart_day() + "日");
        startTime.setText(data.getStart_hour()+"时"+data.getStart_minutes() +"分");
        endTime.setText(data.getEnd_hour()+"时"+data.getEnd_minutes() +"分");
        content.setText(data.getContent());
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                contentItemDelete.onItemLongClick(data);
                return true;
            }
        });
    }
    public interface MeettingContentItemDelete{

        void onItemLongClick(MeettingContent content);
    }
}

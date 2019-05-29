package com.aorise.companymeeting.adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Tuliyuan.
 * Date: 2019/1/29.
 */
public class BaseViewHolder<VB extends ViewDataBinding> extends RecyclerView.ViewHolder {
    private VB vb;
    public BaseViewHolder(VB vb ) {
        super(vb.getRoot());
        this.vb = vb;
    }
    public VB getVBinding(){
        if(vb ==null){
            System.out.println("Aorise Error: BaseViewHolder VB NOT INIT");
            return  null;
        }else{
            return vb;
        }
    }
}

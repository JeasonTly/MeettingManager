package com.aorise.companymeeting;

import android.app.Application;
import com.hjq.toast.ToastUtils;


/**
 * Created by Tuliyuan.
 * Date: 2019/5/17.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.init(this); //初始化吐司框
    }
}

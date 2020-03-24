package com.qudoufeng.vehiclesdk.ui.view;


import com.qudoufeng.vehiclesdk.base.IBaseView;
import com.qudoufeng.vehiclesdk.model.AppConfigResp;

/**
 * Created by WanYang on 2018/11/30/030.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */
public interface UpdateViewI extends IBaseView {
    void loadUpdateInfo(AppConfigResp resp);
    void loadFailed(Throwable e);
}

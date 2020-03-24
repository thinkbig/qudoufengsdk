package com.qudoufeng.vehiclesdk.ui.view;


import com.qudoufeng.vehiclesdk.base.IBaseView;
import com.qudoufeng.vehiclesdk.model.VipResp;

/**
 * Created by WanYang on 2019/1/18/018.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */
public interface VipInfoViewI extends IBaseView {
    void loadUserVipInfo(VipResp resp);

    void onVipError(String s);
}

package com.qudoufeng.vehiclesdk.ui.view;

import com.qudoufeng.vehiclesdk.base.IBaseView;
import com.qudoufeng.vehiclesdk.model.LoginResp;

/**
 * Created by WanYang on 2018/7/9/009.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */

public interface LoginViewI extends IBaseView {
    // 成功登录
    void login(LoginResp resp);
    // 要显示的登录二维码
    void qrcode(String acode);
}

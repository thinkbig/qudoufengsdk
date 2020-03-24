package com.qudoufeng.vehiclesdk.model;

/**
 * Created by WanYang on 2018/7/9/009.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */

public class LoginResp {
    public Long         userid;
    public String       token;

    public String       acode;  // 二维码登录验证码

    public UserProfile  profile;
    public VipResp.VipInfo vipinfo;
}

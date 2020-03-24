package com.qudoufeng.vehiclesdk.model;

import java.io.Serializable;

/**
 * Created by WanYang on 2018/7/9/009.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */

public class LoginReq implements Serializable {
    public String type = "name";
    public String phone;       // user account
    public String password;
    public String vtoken;
    public String vcode;
}
